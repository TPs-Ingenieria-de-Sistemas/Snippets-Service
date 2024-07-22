package org.example.snippetservice.domains.snippet.integration.permits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SnippetPermissionConfig {

    private final RestTemplate restTemplate;
    private final String permissionsServiceUrl;

    @Autowired
    public SnippetPermissionConfig(
            RestTemplate restTemplate,
            @Value("${permissionsService}") String permissionsServiceUrl
            ) {
        this.restTemplate = restTemplate;
        this.permissionsServiceUrl = permissionsServiceUrl;
    }

    @Bean
    public SnippetPermissionsApi createSnippetPermissionsApi() {
        return new SnippetPermissionsImpl(permissionsServiceUrl, restTemplate);
    }

}
