package com.ibm.tw.cloud.os.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Contents {
	@JacksonXmlProperty(localName = "Key")
	private String key;
	
	@JacksonXmlProperty(localName = "LastModified")
	private String lastModified;
	
	@JacksonXmlProperty(localName = "ETag")
	private String eTag;
	
	@JacksonXmlProperty(localName = "Size")
	private long size;
	
	@JacksonXmlProperty(localName = "Owner")
	private Owner owner;
	
	@JacksonXmlProperty(localName = "StorageClass")
	private String storageClass;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
}
