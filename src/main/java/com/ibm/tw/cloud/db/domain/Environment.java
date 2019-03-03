package com.ibm.tw.cloud.db.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Environment {
	@JsonProperty private Map<String, String> properties = new HashMap<>();

	@JsonAnySetter
    public void setProperties(String key, String value) {
        properties.put(key, value);
    }
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
	
	
}
