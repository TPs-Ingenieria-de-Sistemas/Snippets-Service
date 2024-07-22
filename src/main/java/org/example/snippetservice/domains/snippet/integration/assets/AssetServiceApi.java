package org.example.snippetservice.domains.snippet.integration.assets;

import org.springframework.security.oauth2.jwt.Jwt;

public interface AssetServiceApi {
    void getAsset(String userId, String assetName);
    String createAsset(String userId, String fileName, String content);
    String deleteAsset(String userId, String assetName);
}
