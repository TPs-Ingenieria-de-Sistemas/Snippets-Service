package org.example.snippetservice.domains.commons.bucket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AzuriteBucket implements BucketAPI {
	RestTemplate restTemplate = new RestTemplate();
	String assetServiceUrl = "http://asset_service:8080/v1/asset/group-5/";

	@Override
	public ResponseEntity<String> store(String key, String value) {
		try {
			this.restTemplate.postForObject(assetServiceUrl + key, value, String.class);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> get(String key) {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delete(String key) {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> update(String key, String value) {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
