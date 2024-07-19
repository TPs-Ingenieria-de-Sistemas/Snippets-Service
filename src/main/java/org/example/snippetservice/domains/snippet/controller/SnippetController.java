package org.example.snippetservice.domains.snippet.controller;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.UpdateSnippetDTO;
import org.example.snippetservice.domains.snippet.service.SnippetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<SnippetDTO> updateSnippet(@PathVariable Long userId, @PathVariable String name, @Valid @RequestBody UpdateSnippetDTO newSnippet) {
        return this.snippetService.updateSnippet(userId, name, newSnippet.newName, newSnippet.content);
    }

    @GetMapping("/by_user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<SnippetDTO>> getUserSnippets(@PathVariable Long userId) {
        return new ResponseEntity<>(this.snippetService.getUserSnippets(userId), HttpStatus.OK);
    }

    private boolean validateCreateSnippetDTO(CreateSnippetDTO createSnippetDTO) {
        return createSnippetDTO.userId != null && createSnippetDTO.name != null && createSnippetDTO.content != null && !createSnippetDTO.name.isEmpty();
    }
}
