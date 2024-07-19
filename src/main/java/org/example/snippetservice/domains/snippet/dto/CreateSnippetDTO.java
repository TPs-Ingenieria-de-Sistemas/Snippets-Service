package org.example.snippetservice.domains.snippet.dto;

import java.util.UUID;

public class CreateSnippetDTO {
    public UUID userId;
    public String name;
    public String content;
    public String language;
}
