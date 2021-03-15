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
"name",
"permissions",
"insertTimestamp",
"updateTimestamp",
"id"
})
public class Role {

@JsonProperty("name")
private String name;
@JsonProperty("permissions")
private List<Permission> permissions = null;
@JsonProperty("insertTimestamp")
private String insertTimestamp;
@JsonProperty("updateTimestamp")
private String updateTimestamp;
@JsonProperty("id")
private Integer id;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("name")
public String getName() {
return name;
}

@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

@JsonProperty("permissions")
public List<Permission> getPermissions() {
return permissions;
}

@JsonProperty("permissions")
public void setPermissions(List<Permission> permissions) {
this.permissions = permissions;
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