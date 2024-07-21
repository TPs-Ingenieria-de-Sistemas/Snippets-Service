package org.example.snippetservice.domains.snippet.service;

import java.util.List;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.springframework.http.ResponseEntity;

public interface SnippetService {
    ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Boolean isUpdating);
    ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(String userId, String name);
    ResponseEntity<String> deleteSnippet(String userId, String name);
    List<SnippetDTO> getUserSnippets(String userId);
    ResponseEntity<SnippetDTO> updateSnippet(String userId, String name, String newName, String content);
    ResponseEntity<String> updateSnippetStatus(String userId, String name, SnippetStatus status);
}
