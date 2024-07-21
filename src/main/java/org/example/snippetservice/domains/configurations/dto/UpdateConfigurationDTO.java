package org.example.snippetservice.domains.configurations.dto;

public class UpdateConfigurationDTO {
	public String name;
	public String content;

	public UpdateConfigurationDTO() {
	}

	public UpdateConfigurationDTO(String name, String content) {
		this.name = name;
		this.content = content;
	}
}
