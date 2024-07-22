package org.example.snippetservice.domains.snippet.integration.assets;

public interface AssetServiceApi {
	void getAsset(String userId, String assetName);
	String createAsset(String userId, String fileName, String content);
	String deleteAsset(String userId, String assetName);
}
