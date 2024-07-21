package org.example.snippetservice.domains.snippet.controller;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.snippet.dto.*;
import org.example.snippetservice.domains.snippet.service.SnippetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

    private static final Logger logger = LoggerFactory.getLogger(SnippetController.class);

    private final SnippetService snippetService;

    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<SnippetDTO> storeSnippet(@Valid @RequestBody CreateSnippetDTO snippet) {

        logger.info("Snippet id: {}", snippet.userId);
        logger.info("Snippet id type: {}", snippet.userId.getClass());

        if (!validateCreateSnippetDTO(snippet)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return this.snippetService.createSnippet(snippet, false);
    }

    @GetMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<SnippetDTO> getSnippet(@PathVariable String userId, @PathVariable String name) {
        return this.snippetService.getSnippetByUserIdAndName(userId, name);
    }

    @DeleteMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteSnippet(@PathVariable String name, @PathVariable String userId) {
        return this.snippetService.deleteSnippet(userId, name);
    }

    @PutMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<SnippetDTO> updateSnippet(@PathVariable String userId, @PathVariable String name, @Valid @RequestBody UpdateSnippetDTO newSnippet) {
        return this.snippetService.updateSnippet(userId, name, newSnippet.newName, newSnippet.content);
    }

    @GetMapping("/by_user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<SnippetDTO>> getUserSnippets(@PathVariable String userId) {
        return new ResponseEntity<>(this.snippetService.getUserSnippets(userId), HttpStatus.OK);
    }

    private boolean validateCreateSnippetDTO(CreateSnippetDTO createSnippetDTO) {
        return createSnippetDTO.userId != null && createSnippetDTO.name != null && createSnippetDTO.content != null && !createSnippetDTO.name.isEmpty() && createSnippetDTO.language != null && !createSnippetDTO.language.isEmpty();
    }

    @PutMapping("/status/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> updateSnippetStatus(@PathVariable String userId, @PathVariable String name, @Valid @RequestBody SnippetStatusInputDTO status) {
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
