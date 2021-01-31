package com.netflix.conductorboot.dtos.internal.resource.server;

import java.util.Arrays;

public class ResourceRoleMappingDTO {

	private String endpoint;
	private String[] http_methods;
	private String[] roles;
	
	public ResourceRoleMappingDTO() {
		super();
	}

	public ResourceRoleMappingDTO(String endpoint, String[] http_methods, String[] roles) {
		super();
		this.endpoint = endpoint;
		this.http_methods = http_methods;
		this.roles = roles;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String[] getHttp_methods() {
		return http_methods;
	}

	public void setHttp_methods(String[] http_methods) {
		this.http_methods = http_methods;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "ResourceRoleMappingDTO [endpoint=" + endpoint + ", http_methods=" + Arrays.toString(http_methods)
				+ ", roles=" + Arrays.toString(roles) + "]";
	}
}
