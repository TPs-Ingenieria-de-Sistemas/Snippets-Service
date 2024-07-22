package org.example.snippetservice.domains.snippet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.example.snippetservice.domains.snippet.integration.assets.AssetServiceApi;
import org.example.snippetservice.domains.snippet.integration.permits.SnippetPermissionsApi;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SnippetServiceImpl implements SnippetService {
	private static final Logger logger = LoggerFactory.getLogger(SnippetServiceImpl.class);

	private final SnippetRepository snippetRepository;
	/*
	 * private final RestTemplate restTemplate = new RestTemplate();
	 */
	// private final String assetServiceUrl =
	// "http://localhost:8080/v1/asset/group-5/";
	/*
	 * private final String assetServiceUrl =
	 * "http://asset_service:8080/v1/asset/group-5/";
	 */
	/*
	 * private final String permitsUrl = "http://snippet-permit:8085/"; // o permits
	 * con s??
	 */
	private final SnippetPermissionsApi permissionsApi;
	private final AssetServiceApi assetServiceApi;

	public SnippetServiceImpl(SnippetRepository snippetRepository, SnippetPermissionsApi permissionsApi,
			AssetServiceApi assetServiceApi) {
		this.snippetRepository = snippetRepository;
		this.permissionsApi = permissionsApi;
		this.assetServiceApi = assetServiceApi;
	}

	@Override
	public ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Jwt jwt, Boolean isUpdating) {
		String userId = jwt.getSubject();

        if(nameHasDash(createSnippetDTO.name)){
            logger.warn("Snippet name cannot contain dashes for user: {}, name: {}", userId, createSnippetDTO.name);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

		logger.info("Creating snippet for user: {}", userId);

		Optional<Snippet> snippetOptional = this.snippetRepository.findByUserIdAndName(userId,
				createSnippetDTO.name);
		if (snippetOptional.isPresent()) {
			logger.warn("Snippet already exists for user: {}, name: {}", userId,
					createSnippetDTO.name);
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}

		Snippet snippet = new Snippet();
		snippet.setUserId(userId);
		snippet.setName(createSnippetDTO.name);
		snippet.setContent(createSnippetDTO.content);
		snippet.setLanguage(createSnippetDTO.language);

		try {
			logger.info("Storing snippet in bucket for user: {}, name: {}", userId,
					createSnippetDTO.name);
			assetServiceApi.createAsset(userId, createSnippetDTO.name,
					createSnippetDTO.content);
			logger.info("Snippet stored in bucket for user: {}, name: {}", userId,
					createSnippetDTO.name);
		} catch (Exception e) {
			logger.error("Error storing snippet in bucket for user: {}, name: {}", userId,
					createSnippetDTO.name, e);
			snippet.setContent("COULD NOT STORE IN BUCKET");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (!isUpdating) {
			try {
				logger.info("Creating permit for snippet: {}", snippet.getName());
				this.permissionsApi.createPermission(snippet.getName());
				logger.info("Permit created for snippet: {}", snippet.getName());
			} catch (Exception e) {
				logger.error("Error creating permit for snippet: {}", snippet.getName(), e);
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		this.snippetRepository.save(snippet);
		logger.info("Snippet saved to database for user: {}, name: {}", userId, createSnippetDTO.name);

		return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(Long snippetId, Jwt jwt) {
		logger.info("Fetching snippet: {}", snippetId);

		try {
			Snippet snippet = this.snippetRepository.findById(snippetId).orElseThrow();
			logger.info("Snippet found in database for snippetId: {}", snippetId);

			try {
				ResponseEntity<Boolean> hasPermission = this.permissionsApi.hasPermission(snippet.getName(), 4);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					logger.warn("Permission denied for user: {}, name: {}", jwt.getSubject(), snippet.getName());
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				logger.error("Error checking permission for user: {}, name: {}", jwt.getSubject(), snippet.getName(), e);
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			try {
				assetServiceApi.getAsset(snippet.getUserId(), snippet.getName());
				/*
				 * this.restTemplate.getForObject(assetServiceUrl + "snippet-" +
				 * userId.toString() + "-" + name, String.class);
				 */
				return new ResponseEntity<>(new SnippetDTO(snippet), HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error fetching snippet from bucket for user: {}, name: {}", snippet.getUserId(), snippet.getName(), e);
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Snippet not found for with id: ", snippetId, e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// TODO: Implement delete permits (?) para mí sí
	@Override
	public ResponseEntity<String> deleteSnippet(Long snippetId, Jwt jwt) {
		logger.info("Deleting snippet: {}", snippetId);

		try {
			Snippet snippet = this.snippetRepository.findById(snippetId).orElseThrow();

			try {
				assetServiceApi.deleteAsset(snippet.getUserId(), snippet.getName());
				/*
				 * this.restTemplate.delete(assetServiceUrl + "snippet-" + userId.toString() +
				 * "-" + name);
				 */
				this.snippetRepository.delete(snippet);
				logger.info("Snippet deleted for user: {}, name: {}", snippet.getUserId(), snippet.getName());
			} catch (Exception e) {
				logger.error("Error deleting snippet for user: {}, name: {}", snippet.getUserId(), snippet.getName(), e);
				return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Snippet not found: {}", snippetId, e);
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
	}

	// TODO: Implement permits? No me parece, pero no estaría de más.
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
	public ResponseEntity<SnippetDTO> updateSnippet(Long snippetId, String newName, String content,
			Jwt jwt) {
		logger.info("Updating snippet for {}", snippetId);

		try {
			Optional<Snippet> oldContentAux = this.snippetRepository.findById(snippetId);
			if (oldContentAux.isEmpty()) {
				logger.warn("Snippet not found for update, {}", snippetId);
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			SnippetDTO oldContent = getSnippetByUserIdAndName(oldContentAux.get().getId(), jwt).getBody();
			if (oldContent == null) {
				logger.warn("Snippet not found for update, {}", snippetId);
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			try {
				ResponseEntity<Boolean> hasPermission = this.permissionsApi.hasPermission(oldContent.name, 2);
				if (Boolean.FALSE.equals(hasPermission.getBody())) {
					logger.warn("Permission denied for updating snippet, {}", snippetId);
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				logger.error("Error checking permission for updating snippet, {}", snippetId, e);
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			this.deleteSnippet(oldContent.id, jwt);

			SnippetDTO newSnippetDTO = new SnippetDTO();
			newSnippetDTO.id = oldContent.id;
			newSnippetDTO.name = oldContent.name;
			newSnippetDTO.content = content;
			newSnippetDTO.language = oldContent.language;
			newSnippetDTO.status = oldContent.status;

			if (newName != null) {
				newSnippetDTO.name = newName;
			}
			if (content != null) {
				newSnippetDTO.content = content;
			}

			String deleteResult = assetServiceApi.deleteAsset(oldContent.userId, oldContent.name);
			String result = assetServiceApi.createAsset(newSnippetDTO.userId, newSnippetDTO.name, newSnippetDTO.content);

			return ResponseEntity.status(HttpStatus.OK).body(newSnippetDTO);
		} catch (Exception e) {
			logger.error("Error updating snippet for {}", snippetId, e);
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
    private boolean nameHasDash(String name) {
        return name.contains("-");
    }
}
