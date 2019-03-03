package com.ibm.tw.cloud.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedisConnectionInfo implements Connection {
	@JsonProperty
	private Rediss rediss;
	
	@JsonProperty
	private RedisCli cli;
	
	public Rediss getRediss() {
		return rediss;
	}
	public void setRediss(Rediss rediss) {
		this.rediss = rediss;
	}
	public RedisCli getCli() {
		return cli;
	}
	public void setCli(RedisCli cli) {
		this.cli = cli;
	}
	
	
}
