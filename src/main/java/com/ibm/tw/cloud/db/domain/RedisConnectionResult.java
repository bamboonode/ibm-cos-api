package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedisConnectionResult implements Connection {
	@JsonProperty
	RedisConnectionInfo connection;

	public RedisConnectionInfo getConnection() {
		return connection;
	}

	public void setConnection(RedisConnectionInfo connection) {
		this.connection = connection;
	}
	
	
}
