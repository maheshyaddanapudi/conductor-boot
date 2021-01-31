package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.embedded.oauth2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"storedRequest",
"userAuthentication",
"authorities",
"details",
"authenticated"
})
public class CustomPrincipalJsonConverted {

@JsonProperty("storedRequest")
private StoredRequest storedRequest;
@JsonProperty("userAuthentication")
private UserAuthentication userAuthentication;
@JsonProperty("authorities")
private List<Authority_> authorities = null;
@JsonProperty("details")
private Details_ details;
@JsonProperty("authenticated")
private Boolean authenticated;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("storedRequest")
public StoredRequest getStoredRequest() {
return storedRequest;
}

@JsonProperty("storedRequest")
public void setStoredRequest(StoredRequest storedRequest) {
this.storedRequest = storedRequest;
}

@JsonProperty("userAuthentication")
public UserAuthentication getUserAuthentication() {
return userAuthentication;
}

@JsonProperty("userAuthentication")
public void setUserAuthentication(UserAuthentication userAuthentication) {
this.userAuthentication = userAuthentication;
}

@JsonProperty("authorities")
public List<Authority_> getAuthorities() {
return authorities;
}

@JsonProperty("authorities")
public void setAuthorities(List<Authority_> authorities) {
this.authorities = authorities;
}

@JsonProperty("details")
public Details_ getDetails() {
return details;
}

@JsonProperty("details")
public void setDetails(Details_ details) {
this.details = details;
}

@JsonProperty("authenticated")
public Boolean getAuthenticated() {
return authenticated;
}

@JsonProperty("authenticated")
public void setAuthenticated(Boolean authenticated) {
this.authenticated = authenticated;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}