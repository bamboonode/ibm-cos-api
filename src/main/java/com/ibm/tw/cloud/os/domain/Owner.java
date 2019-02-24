package com.ibm.tw.cloud.os.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Owner {
	@JsonProperty("ID")
	private String id;
	
	@JsonProperty("DisplayName")
	private String displayName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
