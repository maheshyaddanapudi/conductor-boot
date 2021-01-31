package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.request.embedded.oauth2.client;

public class UserResetPasswordRequest {

	private String username;
	private String password;
	
	public UserResetPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResetPasswordRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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
		return "UserInputResetPasswordDTO [username=" + username + ", password=" + password
				+ "]";
	}
	
	
}
