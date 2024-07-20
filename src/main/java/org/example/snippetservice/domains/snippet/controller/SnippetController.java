package org.example.snippetservice.domains.snippet.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.dto.*;
import org.example.snippetservice.domains.snippet.service.SnippetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

	private final SnippetService snippetService;

	public SnippetController(SnippetService snippetService) {
		this.snippetService = snippetService;
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<SnippetDTO> storeSnippet(@Valid @RequestBody CreateSnippetDTO snippet) {
		if (!validateCreateSnippetDTO(snippet)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return this.snippetService.createSnippet(snippet, false);
	}

	@GetMapping("/{userId}/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<SnippetDTO> getSnippet(@PathVariable UUID userId, @PathVariable String name) {
		return this.snippetService.getSnippetByUserIdAndName(userId, name);
	}

	@DeleteMapping("/{userId}/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<String> deleteSnippet(@PathVariable String name, @PathVariable UUID userId) {
		return this.snippetService.deleteSnippet(userId, name);
	}

	@PutMapping("/{userId}/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<SnippetDTO> updateSnippet(@PathVariable UUID userId, @PathVariable String name,
			@Valid @RequestBody UpdateSnippetDTO newSnippet) {
		return this.snippetService.updateSnippet(userId, name, newSnippet.newName, newSnippet.content);
	}

	@GetMapping("/by_user/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<List<SnippetDTO>> getUserSnippets(@PathVariable UUID userId) {
		return new ResponseEntity<>(this.snippetService.getUserSnippets(userId), HttpStatus.OK);
	}

	private boolean validateCreateSnippetDTO(CreateSnippetDTO createSnippetDTO) {
		return createSnippetDTO.userId != null && createSnippetDTO.name != null && createSnippetDTO.content != null
				&& !createSnippetDTO.name.isEmpty() && createSnippetDTO.language != null
				&& !createSnippetDTO.language.isEmpty();
	}

	@PutMapping("/status/{userId}/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<String> updateSnippetStatus(@PathVariable UUID userId, @PathVariable String name,
			@Valid @RequestBody SnippetStatusInputDTO status) {
		if (Objects.equals(status.status, "PENDING")) {
			return this.snippetService.updateSnippetStatus(userId, name, SnippetStatus.PENDING);
		} else if (Objects.equals(status.status, "NOT_COMPLIANT")) {
			return this.snippetService.updateSnippetStatus(userId, name, SnippetStatus.NOT_COMPLIANT);
		} else if (Objects.equals(status.status, "COMPLIANT")) {
			return this.snippetService.updateSnippetStatus(userId, name, SnippetStatus.COMPLIANT);
		} else if (Objects.equals(status.status, "FAILED")) {
			return this.snippetService.updateSnippetStatus(userId, name, SnippetStatus.FAILED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
