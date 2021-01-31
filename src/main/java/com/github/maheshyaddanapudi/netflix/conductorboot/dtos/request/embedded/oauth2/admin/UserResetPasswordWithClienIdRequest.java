package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.request.embedded.oauth2.admin;

public class UserResetPasswordWithClienIdRequest {

	private String clientId;
	private String username;
	private String password;
	
	public UserResetPasswordWithClienIdRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResetPasswordWithClienIdRequest(String clientId, String username, String password) {
		super();
		this.clientId = clientId;
		this.username = username;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserInputResetPasswordDTO [clientId=" + clientId + ", username=" + username + ", password=" + password
				+ "]";
	}
	
	
}
