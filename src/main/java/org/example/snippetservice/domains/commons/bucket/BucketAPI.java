package org.example.snippetservice.domains.commons.bucket;

import org.springframework.http.ResponseEntity;

public interface BucketAPI {
	ResponseEntity<String> store(String key, String value);

	ResponseEntity<String> get(String key);

	ResponseEntity<String> delete(String key);

	ResponseEntity<String> update(String key, String value);
}
