package com.netflix.conductorboot.dtos.request.embedded.oauth2.admin;

import java.util.Arrays;

public class UserWithClientIDRequest {

	private String username;
	private String email;
	private String client;
	private String[] roles;
	
	public UserWithClientIDRequest() {
		super();
		// TODO Auto-generated constructor stub
	}



	public UserWithClientIDRequest(String username, String email, String client, String[] roles) {
		super();
		this.username = username;
		this.email = email;
		this.client = client;
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

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}



	public String[] getRoles() {
		return roles;
	}



	public void setRoles(String[] roles) {
		this.roles = roles;
	}



	@Override
	public String toString() {
		return "UserInputWithClientIDDTO [username=" + username + ", email=" + email + ", client=" + client + ", roles="
				+ Arrays.toString(roles) + "]";
	}
	
	
}
