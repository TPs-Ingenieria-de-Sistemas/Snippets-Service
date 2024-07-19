package org.example.snippetservice.domains.snippet.service;

import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SnippetService {
    ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Boolean isUpdating);
    String getSnippetByUserIdAndName(Long userId, String name);
    ResponseEntity<String> deleteSnippet(Long userId, String name);
    List<SnippetDTO> getUserSnippets(Long userId);
    ResponseEntity<SnippetDTO> updateSnippet(Long userId, String name, String newName, String content);
}
