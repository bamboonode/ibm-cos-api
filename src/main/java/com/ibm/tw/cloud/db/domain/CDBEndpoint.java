package com.ibm.tw.cloud.db.domain;

public enum CDBEndpoint {
	US_SOUTH("api.us-south.databases.cloud.ibm.com/v4/ibm"),
	EU_DE("api.eu-de.databases.cloud.ibm.com/v4/ibm"),
	JP_TOK("api.jp-tok.databases.cloud.ibm.com/v4/ibm"),
	AU_SYD("api.au-syd.databases.cloud.ibm.com/v4/ibm"),
	US_EAST("api.us-east.databases.cloud.ibm.com/v4/ibm"),
	EU_GB("api.eu-gb.databases.cloud.ibm.com/v4/ibm");
	
	private final String endpoint;

	CDBEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String toString() {
		return endpoint;
	}
}
