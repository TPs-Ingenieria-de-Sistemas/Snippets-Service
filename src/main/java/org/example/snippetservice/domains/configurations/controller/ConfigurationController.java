package org.example.snippetservice.domains.configurations.controller;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.UpdateConfigurationDTO;
import org.example.snippetservice.domains.configurations.service.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets/configurations")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> storeConfiguration(@Valid @RequestBody CreateConfigurationDTO configuration) {
        if (!validateCreateSnippetDTO(configuration)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return this.configurationService.createConfiguration(configuration);
    }

    @GetMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> getConfiguration(@PathVariable Long userId, @PathVariable String name) {
        return this.configurationService.getConfiguration(userId, name);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteConfiguration(@PathVariable Long userId) {
        return this.configurationService.deleteConfiguration(userId);
    }

    @PutMapping("/{userId}/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> updateConfiguration(@PathVariable Long userId, @PathVariable String name, @Valid @RequestBody UpdateConfigurationDTO newConfiguration) {
        return this.configurationService.updateConfiguration(userId, name, newConfiguration.name, newConfiguration.content);
    }

    private boolean validateCreateSnippetDTO(CreateConfigurationDTO createConfigurationDTO) {
        return createConfigurationDTO.userId != null && createConfigurationDTO.name != null && createConfigurationDTO.content != null && !createConfigurationDTO.name.isEmpty();
    }
}
