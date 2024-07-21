package org.example.snippetservice.domains.configurations.repository;

import java.util.Optional;
import java.util.UUID;
import org.example.snippetservice.domains.configurations.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
	Optional<Configuration> findByUserId(UUID userId);

	Optional<Configuration> findByUserIdAndName(UUID userId, String name);
}
