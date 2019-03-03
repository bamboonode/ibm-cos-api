package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedisCli implements Connection {
	@JsonProperty private String type;
	@JsonProperty private String[] composed;
	@JsonProperty private Environment environment;
	@JsonProperty private String bin;
	@JsonProperty private String[][] arguments;
	@JsonProperty private Certificate certificate;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getComposed() {
		return composed;
	}
	public void setComposed(String[] composed) {
		this.composed = composed;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String[][] getArguments() {
		return arguments;
	}
	public void setArguments(String[][] arguments) {
		this.arguments = arguments;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public Environment getEnvironment() {
		return environment;
	}
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
