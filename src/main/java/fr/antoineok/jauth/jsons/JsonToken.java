package fr.antoineok.jauth.jsons;

public class JsonToken {
	
	private String username, token;

	public JsonToken(String username, String token) {
		this.username = username;
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}
}
