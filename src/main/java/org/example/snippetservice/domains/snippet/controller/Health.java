package org.example.snippetservice.domains.snippet.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class Health {
	@GetMapping
	public String health() {
		return "OK";
	}
}
