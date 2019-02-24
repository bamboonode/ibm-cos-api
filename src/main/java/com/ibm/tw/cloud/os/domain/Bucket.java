package com.ibm.tw.cloud.os.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bucket {
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("CreationDate")
	private String createDate;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
