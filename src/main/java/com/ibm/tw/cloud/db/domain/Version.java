package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Version {
	@JsonProperty
	private String version;
	@JsonProperty
	private String status;
	@JsonProperty
	private boolean is_preferred;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isIs_preferred() {
		return is_preferred;
	}
	public void setIs_preferred(boolean is_preferred) {
		this.is_preferred = is_preferred;
	}
}
