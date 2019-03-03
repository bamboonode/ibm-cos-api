package com.ibm.tw.cloud.db.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rediss implements Connection {

	@JsonProperty private String type;
	@JsonProperty private String[] composed;
	@JsonProperty private String scheme;
	@JsonProperty private List<Host> hosts = new ArrayList<>();
	@JsonProperty private String path;
	@JsonProperty private List<QueryOption> query_options = new ArrayList<>();
	@JsonProperty private Authentication authentication;
	@JsonProperty private Certificate certificate;
	@JsonProperty private int database = 0;
	
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
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public List<Host> getHosts() {
		return hosts;
	}
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Authentication getAuthentication() {
		return authentication;
	}
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public int getDatabase() {
		return database;
	}
	public void setDatabase(int database) {
		this.database = database;
	}
	public List<QueryOption> getQuery_options() {
		return query_options;
	}
	public void setQuery_options(List<QueryOption> query_options) {
		this.query_options = query_options;
	}
	
	

}
