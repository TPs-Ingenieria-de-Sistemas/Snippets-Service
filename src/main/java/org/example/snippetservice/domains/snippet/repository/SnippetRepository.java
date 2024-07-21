package org.example.snippetservice.domains.snippet.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Optional<Snippet> findByUserIdAndName(String userId, String name);
    List<Snippet> findAllByUserId(String userId);
}
