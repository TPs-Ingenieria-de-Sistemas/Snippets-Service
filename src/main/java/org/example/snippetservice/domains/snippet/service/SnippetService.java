package org.example.snippetservice.domains.snippet.service;

import java.util.List;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface SnippetService {
	ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Jwt jwt, Boolean isUpdating);
	ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(Long snippetId, Jwt jwt);
	ResponseEntity<String> deleteSnippet(Long snippetId, Jwt jwt);
	List<SnippetDTO> getUserSnippets(String userId);
	ResponseEntity<SnippetDTO> updateSnippet(Long snippetId, String newName, String content, Jwt jwt);
	ResponseEntity<String> updateSnippetStatus(String userId, String name, SnippetStatus status);
}
