package com.ibm.tw.cloud.exception;

import javax.xml.ws.http.HTTPException;

public class HttpResponseException extends HTTPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3174681502947423813L;
	private String response;

	public HttpResponseException(int statusCode, String response) {
		super(statusCode);
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}

}
