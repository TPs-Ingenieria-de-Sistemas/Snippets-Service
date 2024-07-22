package org.example.snippetservice.domains.snippet.integration.permits;

import org.springframework.http.ResponseEntity;

public interface SnippetPermissionsApi {

	ResponseEntity<String> createPermission(String fileName);
	ResponseEntity<Boolean> hasPermission(String fileName, int permissions);
	// TODO: DELETE SNIPPETS PERMISSIONS.
}
