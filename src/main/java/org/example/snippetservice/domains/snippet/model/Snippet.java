package org.example.snippetservice.domains.snippet.model;

import jakarta.persistence.*;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;

@Entity
@Table(name = "snippet", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String name;

    @Column
    private String content;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Snippet() {
    }

    public Snippet(CreateSnippetDTO snippetDTO) {
        this.userId = snippetDTO.userId;
        this.name = snippetDTO.name;
        this.content = snippetDTO.content;
    }
}
