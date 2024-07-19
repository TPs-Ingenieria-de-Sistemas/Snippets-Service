package org.example.snippetservice.domains.snippet.repository;

import org.example.snippetservice.domains.snippet.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Optional<Snippet> findByUserIdAndName(Long userId, String name);
    List<Snippet> findAllByUserId(Long userId);
}
