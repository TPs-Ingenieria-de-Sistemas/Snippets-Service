package org.example.snippetservice.domains.snippet.service;

import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {
    private final SnippetRepository snippetRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String assetServiceUrl = "http://asset_service:8080/v1/asset/group-5/";

    public SnippetServiceImpl(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    @Override
    public SnippetDTO createSnippet(CreateSnippetDTO createSnippetDTO) {
        Optional<Snippet> snippetOptional = this.snippetRepository.findByUserIdAndName(createSnippetDTO.userId, createSnippetDTO.name);
        if (snippetOptional.isPresent()) {
            return null;
        }

        Snippet snippet = new Snippet();
        snippet.setUserId(createSnippetDTO.userId);
        snippet.setName(createSnippetDTO.name);
        snippet.setContent(createSnippetDTO.content);

        try {
            this.restTemplate.postForObject(assetServiceUrl + "snippet-" + createSnippetDTO.userId.toString() + "-" + createSnippetDTO.name, createSnippetDTO.content, String.class);
        } catch (Exception e) {
            return null;
        }

        this.snippetRepository.save(snippet);

        return new SnippetDTO(snippet);
    }

    @Override
    public SnippetDTO getSnippet(Long id) {
        Snippet snippet = this.snippetRepository.findById(id).orElseThrow();
        return new SnippetDTO(snippet);
    }

    @Override
    public String getSnippetByUserIdAndName(Long userId, String name) {
        try {
            this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
            try {
                return this.restTemplate.getForObject(assetServiceUrl + "snippet-" + userId.toString() + "-" + name, String.class);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> deleteSnippet(Long userId, String name) {
        try {
            Snippet snippet = this.snippetRepository.findByUserIdAndName(userId, name).orElseThrow();
            try {
                this.restTemplate.delete(assetServiceUrl + "snippet-" + userId.toString() + "-" + name);
                this.snippetRepository.delete(snippet);
            } catch (Exception e) {
                return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<SnippetDTO> getSnippets() {
        List<Snippet> snippets = this.snippetRepository.findAll();
        List<SnippetDTO> result = new ArrayList<>();
        for (Snippet snippet : snippets) {
            result.add(new SnippetDTO(snippet));
        }
        return result;
    }

    @Override
    public ResponseEntity<SnippetDTO> updateSnippet(Long userId, String name, String content) {
        try {
            ResponseEntity<String> response = this.deleteSnippet(userId, name);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            CreateSnippetDTO createSnippetDTO = new CreateSnippetDTO();
            createSnippetDTO.userId = userId;
            createSnippetDTO.name = name;
            createSnippetDTO.content = content;
            return new ResponseEntity<>(this.createSnippet(createSnippetDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
