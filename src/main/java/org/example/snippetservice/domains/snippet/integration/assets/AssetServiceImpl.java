package org.example.snippetservice.domains.snippet.integration.assets;

import org.springframework.web.client.RestTemplate;

public class AssetServiceImpl implements AssetServiceApi {

    private final String assetsServiceUrl;
    private final RestTemplate restTemplate;

    public AssetServiceImpl(String assetsServiceUrl, RestTemplate restTemplate) {
        this.assetsServiceUrl = assetsServiceUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public void getAsset(String userId, String assetName) {
        String url = assetsServiceUrl + "snippet-" + userId + "-" + assetName;
        this.restTemplate.getForObject(url, String.class);
    }

    @Override
    public String createAsset(String userId, String fileName, String content) {
        String url = assetsServiceUrl + "snippet-" + userId + "-" + fileName;
        this.restTemplate.postForObject(url, content, String.class);
        return "Snippet created";
    }

    @Override
    public String deleteAsset(String userId, String assetName) {
        String url = assetsServiceUrl + "snippet-" + userId + "-" + assetName;
        this.restTemplate.delete(url);
        return "Snippet deleted";
    }
}
