package org.example.snippetservice.domains.snippet.integration.assets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AssetServiceConfig {

    @Value("${assetService}")
    private String assetsServiceUrl;

    @Bean
    public RestTemplate restTemplate1() {
        return new RestTemplate();
    }

    @Bean
    public AssetServiceApi createAssetServiceApi(RestTemplate restTemplate) {
        return new AssetServiceImpl(assetsServiceUrl, restTemplate);
    }
}
