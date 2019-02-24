package com.ibm.tw.cloud.os.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public class DeleteMultipleObjectsXMLRequest {
	@JacksonXmlRootElement(localName = "Delete")
	public static class Delete {
		
		@JacksonXmlProperty(localName = "Object")
		@JacksonXmlElementWrapper(localName = "Object", useWrapping = false)
		private List<BucketObject> objects = new ArrayList<>();
		
		public void add(BucketObject o) {
			objects.add(o);
		}
	}
	
	public static class BucketObject {
		@JacksonXmlProperty(localName = "Key")
		private final String key;
		
		public BucketObject(String key) {
			this.key = key;
		}
	}	
}
