package org.example.snippetservice.domains.configurations.service;

import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.UpdateConfigurationDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ConfigurationService {
    ResponseEntity<ConfigurationDTO> createConfiguration(CreateConfigurationDTO createConfigurationDTO);
    ResponseEntity<ConfigurationDTO> getConfiguration(UUID userId, String name);
    ResponseEntity<ConfigurationDTO> updateConfiguration(UUID userId, String name, String newName, String newContent);
    ResponseEntity<String> deleteConfiguration(UUID userId);
}
