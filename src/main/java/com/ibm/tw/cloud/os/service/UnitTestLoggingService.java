package com.ibm.tw.cloud.os.service;

import org.slf4j.Logger;

public interface UnitTestLoggingService extends Logger {

	public void printLoggingRemark();
	
	public String getTestcase();
	
	public void setTestcase(String testcase);
}
