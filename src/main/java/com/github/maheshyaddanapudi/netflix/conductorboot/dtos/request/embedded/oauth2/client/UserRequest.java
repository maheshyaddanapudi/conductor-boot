package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.request.embedded.oauth2.client;

import java.util.Arrays;

public class UserRequest {

	private String username;
	private String email;
	private String[] roles;
	
	public UserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRequest(String username, String email, String[] roles) {
		super();
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserInputDTO [username=" + username + ", email=" + email + ", roles=" + Arrays.toString(roles) + "]";
	}
}
