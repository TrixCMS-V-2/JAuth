package fr.antoineok.jauth.jsons;

public class JsonToken {
	
	private String username, accessToken;

	public JsonToken(String username, String accessToken) {
		this.username = username;
		this.accessToken = accessToken;
	}

	public String getUsername() {
		return username;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
