package org.example.snippetservice.domains.snippet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SnippetServiceImpl implements SnippetService {
	private static final Logger logger = LoggerFactory.getLogger(SnippetServiceImpl.class);

	private final SnippetRepository snippetRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final String assetServiceUrl = "http://asset_service:8081/v1/asset/group-5/";
	private final String permitsUrl = "http://snippet-permit:8081/";

	public SnippetServiceImpl(SnippetRepository snippetRepository) {
		this.snippetRepository = snippetRepository;
	}

	@Override
	public ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Boolean isUpdating) {
		logger.info("Creating snippet for user: {}", createSnippetDTO.userId);

		Optional<Snippet> snippetOptional = this.snippetRepository.findByUserIdAndName(createSnippetDTO.userId,
				createSnippetDTO.name);
		if (snippetOptional.isPresent()) {
			logger.warn("Snippet already exists for user: {}, name: {}", createSnippetDTO.userId,
					createSnippetDTO.name);
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}

		Snippet snippet = new Snippet();
		snippet.setUserId(createSnippetDTO.userId);
		snippet.setName(createSnippetDTO.name);
		snippet.setContent(createSnippetDTO.content);
		snippet.setLanguage(createSnippetDTO.language);

		try {
			this.restTemplate.postForObject(
					assetServiceUrl + "snippet-" + createSnippetDTO.userId.toString() + "-" + createSnippetDTO.name,
					createSnippetDTO.content, String.class);
		} catch (Exception e) {
			logger.error("Error storing snippet in bucket for user: {}, name: {}", createSnippetDTO.userId,
					createSnippetDTO.name, e);
			snippet.setContent("COULD NOT STORE IN BUCKET");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		this.snippetRepository.save(snippet);
		logger.info("Snippet saved to database for user: {}, name: {}", createSnippetDTO.userId, createSnippetDTO.name);

		if (!isUpdating) {
			try {
				this.restTemplate.postForObject(permitsUrl + "manage/as-owner/" + snippet.getName(), null,
						String.class);
			} catch (Exception e) {
				logger.error("Error creating permit for snippet: {}", snippet.getName(), e);
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(String userId, String name) {
		logger.info("Fetching snippet for user: {}, name: {}", userId, name);

		try {
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
			logger.info("Snippet found in database for user: {}, name: {}", userId, name);

			try {
				ResponseEntity<Boolean> hasPermission = this.restTemplate
						.getForEntity(permitsUrl + userId + "/" + name + "?permissions=R", Boolean.class);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					logger.warn("Permission denied for user: {}, name: {}", userId, name);
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				logger.error("Error checking permission for user: {}, name: {}", userId, name, e);
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			try {
				this.restTemplate.getForObject(assetServiceUrl + "snippet-" + userId.toString() + "-" + name,
						String.class);
				return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error fetching snippet from bucket for user: {}, name: {}", userId, name, e);
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Snippet not found for user: {}, name: {}", userId, name, e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> deleteSnippet(String userId, String name) {
		logger.info("Deleting snippet for user: {}, name: {}", userId, name);

		try {
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();

			try {
				this.restTemplate.delete(assetServiceUrl + "snippet-" + userId.toString() + "-" + name);
				this.snippetRepository.delete(snippet);
				logger.info("Snippet deleted for user: {}, name: {}", userId, name);
			} catch (Exception e) {
				logger.error("Error deleting snippet for user: {}, name: {}", userId, name, e);
				return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Snippet not found for user: {}, name: {}", userId, name, e);
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
	}

	@Override
	public List<SnippetDTO> getUserSnippets(String userId) {
		logger.info("Fetching snippets for user: {}", userId);

		List<Snippet> snippets = this.snippetRepository.findAllByUserId(userId);
		List<SnippetDTO> result = new ArrayList<>();
		for (Snippet snippet : snippets) {
			result.add(new SnippetDTO(snippet));
		}

		logger.info("Fetched {} snippets for user: {}", result.size(), userId);
		return result;
	}

	@Override
	public ResponseEntity<SnippetDTO> updateSnippet(String userId, String name, String newName, String content) {
		logger.info("Updating snippet for user: {}, name: {}", userId, name);

		try {
			SnippetDTO oldContent = this.getSnippetByUserIdAndName(userId, name).getBody();
			if (oldContent == null) {
				logger.warn("Snippet not found for update, user: {}, name: {}", userId, name);
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			try {
				ResponseEntity<Boolean> hasPermission = this.restTemplate
						.getForEntity(permitsUrl + userId + "/" + name + "?permissions=W", Boolean.class);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					logger.warn("Permission denied for updating snippet, user: {}, name: {}", userId, name);
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				logger.error("Error checking permission for updating snippet, user: {}, name: {}", userId, name, e);
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			this.deleteSnippet(userId, name);

			CreateSnippetDTO createSnippetDTO = new CreateSnippetDTO();
			createSnippetDTO.userId = userId;
			createSnippetDTO.name = name;
			createSnippetDTO.content = oldContent.content;
			createSnippetDTO.language = oldContent.language;

			if (newName != null) {
				createSnippetDTO.name = newName;
			}
			if (content != null) {
				createSnippetDTO.content = content;
			}

			return this.createSnippet(createSnippetDTO, true);
		} catch (Exception e) {
			logger.error("Error updating snippet for user: {}, name: {}", userId, name, e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> updateSnippetStatus(String userId, String name, SnippetStatus status) {
		logger.info("Updating snippet status for user: {}, name: {}", userId, name);

		try {
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
			snippet.setStatus(status);
			this.snippetRepository.save(snippet);

			logger.info("Snippet status updated for user: {}, name: {}", userId, name);
		} catch (Exception e) {
			logger.error("Error updating snippet status for user: {}, name: {}", userId, name, e);
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("200 OK", HttpStatus.OK);
	}
}
