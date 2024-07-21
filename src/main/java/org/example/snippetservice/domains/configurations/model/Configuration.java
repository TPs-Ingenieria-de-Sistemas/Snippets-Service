package org.example.snippetservice.domains.configurations.model;

import jakarta.persistence.*;
import java.util.UUID;
import org.example.snippetservice.domains.configurations.dto.CreateConfigurationDTO;

@Entity
@Table(name = "configuration", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"})})
public class Configuration {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private UUID userId;

	@Column
	private String name;

	@Column
	private String content;

	public Long getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Configuration() {
	}

	public Configuration(CreateConfigurationDTO configurationDTO) {
		this.userId = configurationDTO.userId;
		this.name = configurationDTO.name;
		this.content = configurationDTO.content;
	}
}
