package com.ibm.tw.cloud.db.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.tw.cloud.db.domain.CDBEndpoint;
import com.ibm.tw.cloud.db.domain.Connection;
import com.ibm.tw.cloud.db.domain.Deployable;
import com.ibm.tw.cloud.db.domain.Deployment;
import com.ibm.tw.cloud.db.domain.Task;
import com.ibm.tw.cloud.db.domain.User;

@Service
public interface CloudDatabaseService {
	public List<Deployable> listAll(CDBEndpoint cdbEndpoint, String apiKey);
	
	public Deployment getDeploymentDetails(CDBEndpoint cdbEndpoint, String apiKey, String deploymentId);
	
	public Task createUser(CDBEndpoint cdbEndpoint, String apiKey, String deploymentId, User user);
	
	public Task updateUserPassword(CDBEndpoint cdbEndpoint, String apiKey, String deploymentId, User user);
	
	public Task deleteUser(CDBEndpoint cdbEndpoint, String apiKey,String deploymentId, String userId);
	
	public Connection getConnectionInfo(CDBEndpoint cdbEndpoint, String apiKey, String deploymentID, String userId);
	
}
