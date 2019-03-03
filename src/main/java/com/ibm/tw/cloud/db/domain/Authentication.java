package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authentication {
	@JsonProperty private String method;
	@JsonProperty private String username;
	@JsonProperty private String password;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
