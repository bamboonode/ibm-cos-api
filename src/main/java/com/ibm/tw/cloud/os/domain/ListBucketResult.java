package com.ibm.tw.cloud.os.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ListBucketResult {
	@JacksonXmlProperty(localName = "Name")
	private String name;
	
	@JacksonXmlProperty(localName = "Prefix")
	private String prefix;
	
	@JacksonXmlProperty(localName = "Marker")
	private String marker;
	
	@JacksonXmlProperty(localName = "MaxKeys")
	private String maxKeys;
	
	@JacksonXmlProperty(localName = "Delimiter")
	private String delimiter;
	
	@JacksonXmlProperty(localName = "IsTruncated")
	private boolean truncated;
	
	@JacksonXmlElementWrapper(localName = "Contents", useWrapping = false)
	@JacksonXmlProperty(localName = "Contents")
	private List<Contents> contents = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Contents> getContents() {
		return contents;
	}

	public void setContents(List<Contents> contents) {
		this.contents = contents;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getMaxKeys() {
		return maxKeys;
	}

	public void setMaxKeys(String maxKeys) {
		this.maxKeys = maxKeys;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}
	
}
