package com.ibm.tw.cloud.os.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ListAllMyBucketsResult {
	@JsonProperty("Owner")
	private Owner owner;
	
	@JsonProperty("Buckets")
	private List<Bucket> buckets;
	
	public Owner getOwner() {
		return owner;
	}
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	public List<Bucket> getBuckets() {
		return buckets;
	}
	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}
	
	
}
