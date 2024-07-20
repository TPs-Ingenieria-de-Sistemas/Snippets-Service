package org.example.snippetservice.domains.snippet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SnippetServiceImpl implements SnippetService {
	private final SnippetRepository snippetRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final String assetServiceUrl = "http://asset_service:8080/v1/asset/group-5/";
	private final String permitsUrl = "http://snippet-permit:" + System.getenv("PERMIT_PORT") + "/";

	public SnippetServiceImpl(SnippetRepository snippetRepository) {
		this.snippetRepository = snippetRepository;
	}

	@Override
	public ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Boolean isUpdating) {
		// Check if the snippet already exists
		Optional<Snippet> snippetOptional = this.snippetRepository.findByUserIdAndName(createSnippetDTO.userId,
				createSnippetDTO.name);
		if (snippetOptional.isPresent()) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}

		// Create the snippet
		Snippet snippet = new Snippet();
		snippet.setUserId(createSnippetDTO.userId);
		snippet.setName(createSnippetDTO.name);
		snippet.setContent(createSnippetDTO.content);
		snippet.setLanguage(createSnippetDTO.language);

		try {
			// Store in bucket
			this.restTemplate.postForObject(
					assetServiceUrl + "snippet-" + createSnippetDTO.userId.toString() + "-" + createSnippetDTO.name,
					createSnippetDTO.content, String.class);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Store in database
		this.snippetRepository.save(snippet);

		if (!isUpdating) {
			try {
				// Create permit
				this.restTemplate.postForObject(permitsUrl + "manage/as-owner/" + snippet.getName(), null,
						String.class);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(UUID userId, String name) {
		try {
			// Check in database
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
			try {
				// Check permission
				ResponseEntity<Boolean> hasPermission = this.restTemplate
						.getForEntity(permitsUrl + userId + "/" + name + "?permissions=R", Boolean.class);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}
			try {
				// Get from bucket
				this.restTemplate.getForObject(assetServiceUrl + "snippet-" + userId.toString() + "-" + name,
						String.class);
				return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> deleteSnippet(UUID userId, String name) {
		try {
			// Check if snippet exists
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
			try {
				// Delete from bucket
				this.restTemplate.delete(assetServiceUrl + "snippet-" + userId.toString() + "-" + name);
				// Delete from database
				this.snippetRepository.delete(snippet);
			} catch (Exception e) {
				return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
	}

	@Override
	public List<SnippetDTO> getUserSnippets(UUID userId) {
		List<Snippet> snippets = this.snippetRepository.findAllByUserId(userId);
		List<SnippetDTO> result = new ArrayList<>();
		for (Snippet snippet : snippets) {
			result.add(new SnippetDTO(snippet));
		}
		return result;
	}

	@Override
	public ResponseEntity<SnippetDTO> updateSnippet(UUID userId, String name, String newName, String content) {
		try {
			// Get old content
			SnippetDTO oldContent = this.getSnippetByUserIdAndName(userId, name).getBody();
			if (oldContent == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			try {
				// Check permission
				ResponseEntity<Boolean> hasPermission = this.restTemplate
						.getForEntity(permitsUrl + userId + "/" + name + "?permissions=W", Boolean.class);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			// Delete snippet and create new one with new content
			this.deleteSnippet(userId, name);

			CreateSnippetDTO createSnippetDTO = new CreateSnippetDTO();
			createSnippetDTO.userId = userId;
			createSnippetDTO.name = name;
			createSnippetDTO.content = oldContent.content;
			createSnippetDTO.language = oldContent.language;

			// Update name and content if new values are provided
			if (newName != null) {
				createSnippetDTO.name = newName;
			}
			if (content != null) {
				createSnippetDTO.content = content;
			}

			return this.createSnippet(createSnippetDTO, true);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> updateSnippetStatus(UUID userId, String name, SnippetStatus status) {
		try {
			Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
			snippet.setStatus(status);
			this.snippetRepository.save(snippet);
		} catch (Exception e) {
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("200 OK", HttpStatus.OK);
	}
}
