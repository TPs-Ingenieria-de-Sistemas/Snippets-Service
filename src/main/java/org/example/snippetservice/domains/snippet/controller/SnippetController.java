package org.example.snippetservice.domains.snippet.controller;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
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
        SnippetDTO response = this.snippetService.createSnippet(snippet);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @GetMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> getSnippet(@PathVariable Long userId, @PathVariable String name) {
        String response = this.snippetService.getSnippetByUserIdAndName(userId, name);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteSnippet(@PathVariable String name, @PathVariable Long userId) {
        return this.snippetService.deleteSnippet(userId, name);
    }
}
