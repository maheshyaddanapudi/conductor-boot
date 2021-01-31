package com.netflix.conductorboot.dtos.request.embedded.oauth2.admin;

public class ClientResetPasswordWithClienIdRequest {
	
	private String clientId;
	private String clientSecret;
	
	public ClientResetPasswordWithClienIdRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientResetPasswordWithClienIdRequest(String clientId, String clientSecret) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public String toString() {
		return "ClientInputResetPasswordDTO [clientId=" + clientId + ", clientSecret=" + clientSecret + "]";
	}
}
