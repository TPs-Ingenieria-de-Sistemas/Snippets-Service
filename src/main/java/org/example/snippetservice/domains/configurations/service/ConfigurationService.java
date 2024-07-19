package org.example.snippetservice.domains.configurations.service;

import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.UpdateConfigurationDTO;
import org.springframework.http.ResponseEntity;

public interface ConfigurationService {
    ResponseEntity<ConfigurationDTO> createConfiguration(CreateConfigurationDTO createConfigurationDTO);
    ResponseEntity<ConfigurationDTO> getConfiguration(Long userId, String name);
    ResponseEntity<ConfigurationDTO> updateConfiguration(Long userId, String name, String newName, String newContent);
    ResponseEntity<String> deleteConfiguration(Long userId);
}
