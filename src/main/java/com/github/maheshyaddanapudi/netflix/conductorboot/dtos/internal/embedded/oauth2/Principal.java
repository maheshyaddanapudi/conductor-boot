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
"email",
"username",
"enabled",
"accountNonLocked",
"accountNonExpired",
"credentialsNonExpired",
"roles",
"client",
"insertTimestamp",
"updateTimestamp",
"id"
})
public class Principal {

@JsonProperty("email")
private String email;
@JsonProperty("username")
private String username;
@JsonProperty("enabled")
private Boolean enabled;
@JsonProperty("accountNonLocked")
private Boolean accountNonLocked;
@JsonProperty("accountNonExpired")
private Boolean accountNonExpired;
@JsonProperty("credentialsNonExpired")
private Boolean credentialsNonExpired;
@JsonProperty("roles")
private List<Role> roles = null;
@JsonProperty("client")
private String client;
@JsonProperty("insertTimestamp")
private String insertTimestamp;
@JsonProperty("updateTimestamp")
private String updateTimestamp;
@JsonProperty("id")
private Integer id;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("email")
public String getEmail() {
return email;
}

@JsonProperty("email")
public void setEmail(String email) {
this.email = email;
}

@JsonProperty("username")
public String getUsername() {
return username;
}

@JsonProperty("username")
public void setUsername(String username) {
this.username = username;
}

@JsonProperty("enabled")
public Boolean getEnabled() {
return enabled;
}

@JsonProperty("enabled")
public void setEnabled(Boolean enabled) {
this.enabled = enabled;
}

@JsonProperty("accountNonLocked")
public Boolean getAccountNonLocked() {
return accountNonLocked;
}

@JsonProperty("accountNonLocked")
public void setAccountNonLocked(Boolean accountNonLocked) {
this.accountNonLocked = accountNonLocked;
}

@JsonProperty("accountNonExpired")
public Boolean getAccountNonExpired() {
return accountNonExpired;
}

@JsonProperty("accountNonExpired")
public void setAccountNonExpired(Boolean accountNonExpired) {
this.accountNonExpired = accountNonExpired;
}

@JsonProperty("credentialsNonExpired")
public Boolean getCredentialsNonExpired() {
return credentialsNonExpired;
}

@JsonProperty("credentialsNonExpired")
public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
this.credentialsNonExpired = credentialsNonExpired;
}

@JsonProperty("roles")
public List<Role> getRoles() {
return roles;
}

@JsonProperty("roles")
public void setRoles(List<Role> roles) {
this.roles = roles;
}

@JsonProperty("client")
public String getClient() {
return client;
}

@JsonProperty("client")
public void setClient(String client) {
this.client = client;
}

@JsonProperty("insertTimestamp")
public String getInsertTimestamp() {
return insertTimestamp;
}

@JsonProperty("insertTimestamp")
public void setInsertTimestamp(String insertTimestamp) {
this.insertTimestamp = insertTimestamp;
}

@JsonProperty("updateTimestamp")
public String getUpdateTimestamp() {
return updateTimestamp;
}

@JsonProperty("updateTimestamp")
public void setUpdateTimestamp(String updateTimestamp) {
this.updateTimestamp = updateTimestamp;
}

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
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