package com.netflix.conductorboot.dtos.request.embedded.oauth2.admin;

import java.util.Arrays;

public class ClientRequest {
	
	private String clientId;
	private String[] scope;
	private int accessTokenValidityInSeconds;
	private int refreshTokenValidityInSeconds;
	
	public ClientRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientRequest(String clientId, String[] scope, int accessTokenValidityInSeconds,
			int refreshTokenValidityInSeconds) {
		super();
		this.clientId = clientId;
		this.scope = scope;
		this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
		this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getAccessTokenValidityInSeconds() {
		return accessTokenValidityInSeconds;
	}

	public void setAccessTokenValidityInSeconds(int accessTokenValidityInSeconds) {
		this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
	}

	public int getRefreshTokenValidityInSeconds() {
		return refreshTokenValidityInSeconds;
	}

	public void setRefreshTokenValidityInSeconds(int refreshTokenValidityInSeconds) {
		this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "NewClientDTO [clientId=" + clientId + ", scope="
				+ Arrays.toString(scope) + ", accessTokenValidityInSeconds=" + accessTokenValidityInSeconds
				+ ", refreshTokenValidityInSeconds=" + refreshTokenValidityInSeconds + "]";
	}
	
}
