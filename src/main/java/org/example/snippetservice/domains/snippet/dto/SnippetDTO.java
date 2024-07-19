package org.example.snippetservice.domains.snippet.dto;

import org.example.snippetservice.domains.snippet.model.Snippet;

import java.util.UUID;

public class SnippetDTO {
    public Long id;
    public UUID userId;
    public String name;
    public String content;
    public String language;
    public SnippetStatus status;

    public SnippetDTO() {
    }

    public SnippetDTO(Snippet snippet) {
        this.id = snippet.getId();
        this.userId = snippet.getUserId();
        this.name = snippet.getName();
        this.content = snippet.getContent();
        this.language = snippet.getLanguage();
        this.status = snippet.getStatus();
    }
}
