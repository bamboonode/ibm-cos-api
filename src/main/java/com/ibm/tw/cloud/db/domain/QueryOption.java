package com.ibm.tw.cloud.db.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryOption {
	@JsonProperty
	private Map<String, String> options = new HashMap<>();

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
	
}
