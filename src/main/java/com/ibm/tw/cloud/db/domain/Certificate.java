package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Certificate {
	@JsonProperty private String name;
	@JsonProperty private String certificate_base64;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificate_base64() {
		return certificate_base64;
	}
	public void setCertificate_base64(String certificate_base64) {
		this.certificate_base64 = certificate_base64;
	}
	
	
}
