package org.example.snippetservice.domains.configurations.service;

import java.util.UUID;
import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.springframework.http.ResponseEntity;

public interface ConfigurationService {
	ResponseEntity<ConfigurationDTO> createConfiguration(CreateConfigurationDTO createConfigurationDTO);

	ResponseEntity<ConfigurationDTO> getConfiguration(UUID userId, String name);

	ResponseEntity<ConfigurationDTO> updateConfiguration(UUID userId, String name, String newName, String newContent);

	ResponseEntity<String> deleteConfiguration(UUID userId);
}
