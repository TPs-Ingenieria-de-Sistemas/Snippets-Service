package org.example.snippetservice.domains.configurations.repository;

import org.example.snippetservice.domains.configurations.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByUserId(Long userId);
    Optional<Configuration> findByUserIdAndName(Long userId, String name);
}
