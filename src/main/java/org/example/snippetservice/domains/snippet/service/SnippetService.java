package org.example.snippetservice.domains.snippet.service;

import java.util.List;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.springframework.http.ResponseEntity;

public interface SnippetService {
	ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Boolean isUpdating);

	ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(UUID userId, String name);

	ResponseEntity<String> deleteSnippet(UUID userId, String name);

	List<SnippetDTO> getUserSnippets(UUID userId);

	ResponseEntity<SnippetDTO> updateSnippet(UUID userId, String name, String newName, String content);

	ResponseEntity<String> updateSnippetStatus(UUID userId, String name, SnippetStatus status);
}
