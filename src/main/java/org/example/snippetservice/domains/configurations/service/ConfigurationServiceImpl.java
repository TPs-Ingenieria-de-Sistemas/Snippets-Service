package org.example.snippetservice.domains.configurations.service;

import java.util.Optional;
import java.util.UUID;
import org.example.snippetservice.domains.configurations.dto.ConfigurationDTO;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;
import org.example.snippetservice.domains.configurations.model.Configuration;
import org.example.snippetservice.domains.configurations.repository.ConfigurationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
	private final ConfigurationRepository configurationRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final String assetServiceUrl = "http://asset_service:8080/v1/asset/group-5/";

	public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}

	@Override
	public ResponseEntity<ConfigurationDTO> createConfiguration(CreateConfigurationDTO createConfigurationDTO) {
		Optional<Configuration> configuration = this.configurationRepository
				.findByUserId(createConfigurationDTO.userId);
		if (configuration.isPresent()) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}

		try {
			this.restTemplate.postForObject(assetServiceUrl + "configuration-"
					+ createConfigurationDTO.userId.toString() + "-" + createConfigurationDTO.name,
					createConfigurationDTO.content, String.class);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Configuration newConfiguration = new Configuration();
		newConfiguration.setUserId(createConfigurationDTO.userId);
		newConfiguration.setName(createConfigurationDTO.name);
		newConfiguration.setContent(createConfigurationDTO.content);

		this.configurationRepository.save(newConfiguration);

		return new ResponseEntity<>(new ConfigurationDTO(newConfiguration), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ConfigurationDTO> getConfiguration(UUID userId, String name) {
		try {
			Configuration configuration = this.configurationRepository.findByUserIdAndName(userId, name).orElseThrow();
			try {
				this.restTemplate.getForObject(assetServiceUrl + "configuration-" + userId.toString() + "-" + name,
						String.class);
				return new ResponseEntity<>(new ConfigurationDTO(configuration), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<ConfigurationDTO> updateConfiguration(UUID userId, String name, String newName,
			String newContent) {
		try {
			// Get old content
			ConfigurationDTO oldContent = this.getConfiguration(userId, name).getBody();
			if (oldContent == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			// Delete snippet and create new one with new content
			this.deleteConfiguration(userId);

			CreateConfigurationDTO createConfigurationDTO = new CreateConfigurationDTO();
			createConfigurationDTO.userId = userId;
			createConfigurationDTO.name = name;
			createConfigurationDTO.content = oldContent.content;

			if (newName != null) {
				createConfigurationDTO.name = newName;
			}
			if (newContent != null) {
				createConfigurationDTO.content = newContent;
			}

			return this.createConfiguration(createConfigurationDTO);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> deleteConfiguration(UUID userId) {
		try {
			Configuration configuration = this.configurationRepository.findByUserId(userId).orElseThrow();
			try {
				this.restTemplate
						.delete(assetServiceUrl + "configuration-" + userId.toString() + "-" + configuration.getName());
				this.configurationRepository.delete(configuration);
				return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
		}
	}
}
