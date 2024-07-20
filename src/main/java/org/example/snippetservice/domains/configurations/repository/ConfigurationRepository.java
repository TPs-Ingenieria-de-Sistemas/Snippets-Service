package org.example.snippetservice.domains.configurations.repository;

import org.example.snippetservice.domains.configurations.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByUserId(UUID userId);
    Optional<Configuration> findByUserIdAndName(UUID userId, String name);
}
