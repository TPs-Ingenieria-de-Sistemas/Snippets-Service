package org.example.snippetservice.domains.snippet.service;

import java.util.List;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface SnippetService {
    ResponseEntity<SnippetDTO> createSnippet(CreateSnippetDTO createSnippetDTO, Jwt jwt, Boolean isUpdating);
    ResponseEntity<SnippetDTO> getSnippetByUserIdAndName(String userId, String name, Jwt jwt);
    ResponseEntity<String> deleteSnippet(String userId, String name, Jwt jwt);
    List<SnippetDTO> getUserSnippets(String userId);
    ResponseEntity<SnippetDTO> updateSnippet(String userId, String name, String newName, String content, Jwt jwt);
    ResponseEntity<String> updateSnippetStatus(String userId, String name, SnippetStatus status);
}
