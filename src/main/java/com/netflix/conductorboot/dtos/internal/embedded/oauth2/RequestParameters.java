package com.netflix.conductorboot.dtos.internal.embedded.oauth2;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"grant_type",
"username"
})
public class RequestParameters {

@JsonProperty("grant_type")
private String grantType;
@JsonProperty("username")
private String username;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("grant_type")
public String getGrantType() {
return grantType;
}

@JsonProperty("grant_type")
public void setGrantType(String grantType) {
this.grantType = grantType;
}

@JsonProperty("username")
public String getUsername() {
return username;
}

@JsonProperty("username")
public void setUsername(String username) {
this.username = username;
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