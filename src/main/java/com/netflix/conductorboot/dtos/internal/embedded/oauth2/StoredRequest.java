package com.netflix.conductorboot.dtos.internal.embedded.oauth2;

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
"resourceIds",
"authorities",
"approved",
"responseTypes",
"extensions",
"clientId",
"scope",
"requestParameters"
})
public class StoredRequest {

@JsonProperty("resourceIds")
private List<String> resourceIds = null;
@JsonProperty("authorities")
private List<Object> authorities = null;
@JsonProperty("approved")
private Boolean approved;
@JsonProperty("responseTypes")
private List<Object> responseTypes = null;
@JsonProperty("extensions")
private Extensions extensions;
@JsonProperty("clientId")
private String clientId;
@JsonProperty("scope")
private List<String> scope = null;
@JsonProperty("requestParameters")
private RequestParameters requestParameters;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("resourceIds")
public List<String> getResourceIds() {
return resourceIds;
}

@JsonProperty("resourceIds")
public void setResourceIds(List<String> resourceIds) {
this.resourceIds = resourceIds;
}

@JsonProperty("authorities")
public List<Object> getAuthorities() {
return authorities;
}

@JsonProperty("authorities")
public void setAuthorities(List<Object> authorities) {
this.authorities = authorities;
}

@JsonProperty("approved")
public Boolean getApproved() {
return approved;
}

@JsonProperty("approved")
public void setApproved(Boolean approved) {
this.approved = approved;
}

@JsonProperty("responseTypes")
public List<Object> getResponseTypes() {
return responseTypes;
}

@JsonProperty("responseTypes")
public void setResponseTypes(List<Object> responseTypes) {
this.responseTypes = responseTypes;
}

@JsonProperty("extensions")
public Extensions getExtensions() {
return extensions;
}

@JsonProperty("extensions")
public void setExtensions(Extensions extensions) {
this.extensions = extensions;
}

@JsonProperty("clientId")
public String getClientId() {
return clientId;
}

@JsonProperty("clientId")
public void setClientId(String clientId) {
this.clientId = clientId;
}

@JsonProperty("scope")
public List<String> getScope() {
return scope;
}

@JsonProperty("scope")
public void setScope(List<String> scope) {
this.scope = scope;
}

@JsonProperty("requestParameters")
public RequestParameters getRequestParameters() {
return requestParameters;
}

@JsonProperty("requestParameters")
public void setRequestParameters(RequestParameters requestParameters) {
this.requestParameters = requestParameters;
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