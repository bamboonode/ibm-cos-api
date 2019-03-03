package com.ibm.tw.cloud.db.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Deployable {
	@JsonProperty
	private String type;
	
	@JsonProperty
	private List<Version> versions = new ArrayList<>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Version> getVersions() {
		return versions;
	}
	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}	
}
