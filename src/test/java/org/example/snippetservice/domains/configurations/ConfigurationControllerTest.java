package org.example.snippetservice.domains.configurations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.UUID;
import org.example.snippetservice.domains.configurations.controller.ConfigurationController;
import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.UpdateConfigurationDTO;
import org.example.snippetservice.domains.configurations.service.ConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ConfigurationControllerTest {

	@InjectMocks
	private ConfigurationController configurationController;

	@Mock
	private ConfigurationService configurationService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testStoreConfiguration() {
		CreateConfigurationDTO createConfigurationDTO = new CreateConfigurationDTO();
		createConfigurationDTO.userId = UUID.randomUUID();
		createConfigurationDTO.name = "testConfig";
		createConfigurationDTO.content = "content";

		ConfigurationDTO configurationDTO = new ConfigurationDTO();
		configurationDTO.id = 1L;
		configurationDTO.userId = createConfigurationDTO.userId;
		configurationDTO.name = "testConfig";
		configurationDTO.content = "content";

		when(configurationService.createConfiguration(any(CreateConfigurationDTO.class)))
				.thenReturn(new ResponseEntity<>(configurationDTO, HttpStatus.CREATED));

		ResponseEntity<ConfigurationDTO> response = configurationController.storeConfiguration(createConfigurationDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("testConfig", response.getBody().name);

		createConfigurationDTO.name = null;
		response = configurationController.storeConfiguration(createConfigurationDTO);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetConfiguration() {
		String content = "content";
		ConfigurationDTO configurationDTO = new ConfigurationDTO();
		configurationDTO.id = 1L;
		configurationDTO.userId = UUID.randomUUID();
		configurationDTO.name = "testConfig";
		configurationDTO.content = "content";

		when(configurationService.getConfiguration(configurationDTO.userId, "testConfig"))
				.thenReturn(new ResponseEntity<>(configurationDTO, HttpStatus.OK));

		ResponseEntity<ConfigurationDTO> response = configurationController.getConfiguration(configurationDTO.userId,
				"testConfig");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("content", Objects.requireNonNull(response.getBody()).content);
	}

	@Test
	public void testDeleteConfiguration() {
		UUID userId = UUID.randomUUID();
		when(configurationService.deleteConfiguration(userId))
				.thenReturn(new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT));

		ResponseEntity<String> response = configurationController.deleteConfiguration(userId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertEquals("204 No Content", response.getBody());
	}

	@Test
	public void testUpdateConfiguration() {
		UpdateConfigurationDTO updateConfigurationDTO = new UpdateConfigurationDTO();
		updateConfigurationDTO.name = "updatedConfig";
		updateConfigurationDTO.content = "updated content";

		ConfigurationDTO configurationDTO = new ConfigurationDTO();
		configurationDTO.id = 1L;
		configurationDTO.userId = UUID.randomUUID();
		configurationDTO.name = "updatedConfig";
		configurationDTO.content = "updated content";

		when(configurationService.updateConfiguration(configurationDTO.userId, "testConfig",
				updateConfigurationDTO.name, updateConfigurationDTO.content))
				.thenReturn(new ResponseEntity<>(configurationDTO, HttpStatus.OK));

		ResponseEntity<ConfigurationDTO> response = configurationController.updateConfiguration(configurationDTO.userId,
				"testConfig", updateConfigurationDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("updatedConfig", response.getBody().name);
	}
}
