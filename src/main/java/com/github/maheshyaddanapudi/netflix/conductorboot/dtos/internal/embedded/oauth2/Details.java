package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.embedded.oauth2;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"grant_type",
"username"
})
public class Details {

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