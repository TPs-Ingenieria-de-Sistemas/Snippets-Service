package org.example.snippetservice.domains.snippet.integration.permits;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.jwt.Jwt;

public class SnippetPermissionsImpl implements SnippetPermissionsApi {

    private final String permitsUrl;
    private final RestTemplate restTemplate;

    public SnippetPermissionsImpl(String permitsUrl, RestTemplate restTemplate) {
        this.permitsUrl = permitsUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> createPermission(String fileName) {
        HttpHeaders headers = new HttpHeaders();

        Jwt id = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        headers.setBearerAuth(id.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        this.restTemplate.postForObject(permitsUrl + "manage/as-owner/" + fileName, entity, String.class);
        return ResponseEntity.ok("Snippet permission created");
    }

    @Override
    public ResponseEntity<Boolean> hasPermission(String fileName, int permissions) {
        HttpHeaders headers = new HttpHeaders();

        Jwt id = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        headers.setBearerAuth(id.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return this.restTemplate.exchange(permitsUrl + id.getClaim("sub") + "/" + fileName + "?permission=" + permissions, HttpMethod.GET, entity, Boolean.class);
    }

}
