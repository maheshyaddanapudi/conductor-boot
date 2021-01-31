package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.request.embedded.oauth2.client;

public class ClientResetPasswordRequest {
	
	private String clientSecret;
	
	public ClientResetPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientResetPasswordRequest(String clientSecret) {
		super();
		this.clientSecret = clientSecret;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public String toString() {
		return "ClientInputResetPasswordDTO [clientSecret=" + clientSecret + "]";
	}
}
