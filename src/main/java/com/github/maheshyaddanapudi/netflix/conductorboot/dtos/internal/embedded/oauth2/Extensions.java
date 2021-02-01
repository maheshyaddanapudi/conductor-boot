package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.embedded.oauth2;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Extensions {

@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}