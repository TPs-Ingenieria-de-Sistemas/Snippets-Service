package org.example.snippetservice.domains.configurations;

import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.model.Configuration;
import org.example.snippetservice.domains.configurations.repository.ConfigurationRepository;
import org.example.snippetservice.domains.configurations.service.ConfigurationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ConfigurationServiceImplTests {

    @InjectMocks
    private ConfigurationServiceImpl configurationService;

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createConfiguration_Conflict() {
        CreateConfigurationDTO dto = new CreateConfigurationDTO();
        dto.userId = 1L;
        dto.name = "config1";
        dto.content = "content";

        when(configurationRepository.findByUserId(dto.userId)).thenReturn(Optional.of(new Configuration()));

        ResponseEntity<ConfigurationDTO> response = configurationService.createConfiguration(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void getConfiguration_InternalServerError() {
        Long userId = 1L;
        String name = "config1";

        when(configurationRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.of(new Configuration()));
        doThrow(new RuntimeException()).when(restTemplate).getForObject(anyString(), any());

        ResponseEntity<ConfigurationDTO> response = configurationService.getConfiguration(userId, name);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void updateConfiguration_NotFound() {
        Long userId = 1L;
        String name = "config1";
        String newName = "newConfig";
        String newContent = "newContent";

        when(configurationRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.empty());

        ResponseEntity<ConfigurationDTO> response = configurationService.updateConfiguration(userId, name, newName, newContent);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteConfiguration_NotFound() {
        Long userId = 1L;

        when(configurationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = configurationService.deleteConfiguration(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}