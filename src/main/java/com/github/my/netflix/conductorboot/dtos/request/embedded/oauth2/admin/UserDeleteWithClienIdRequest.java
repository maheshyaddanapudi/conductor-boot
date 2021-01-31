package com.github.my.netflix.conductorboot.dtos.request.embedded.oauth2.admin;

public class UserDeleteWithClienIdRequest {

	private String clientId;
	private String username;
	
	public UserDeleteWithClienIdRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDeleteWithClienIdRequest(String clientId, String username) {
		super();
		this.clientId = clientId;
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserInputDeleteDTO [clientId=" + clientId + ", username=" + username + "]";
	}
	
	
}
