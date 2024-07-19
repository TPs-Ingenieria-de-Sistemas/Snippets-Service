package org.example.snippetservice.domains.configurations.dto;

import org.example.snippetservice.domains.configurations.model.Configuration;

public class ConfigurationDTO {
    public Long id;
    public Long userId;
    public String name;
    public String content;

    public ConfigurationDTO() {
    }

    public ConfigurationDTO(Configuration configuration) {
        this.id = configuration.getId();
        this.userId = configuration.getUserId();
        this.name = configuration.getName();
        this.content = configuration.getContent();
    }
}
