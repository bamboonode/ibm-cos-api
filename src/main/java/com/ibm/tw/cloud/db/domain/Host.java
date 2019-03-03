package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Host {
	@JsonProperty private String hostname;
	@JsonProperty private int port;
	@JsonProperty private String protocol;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	
}
