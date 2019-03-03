package com.ibm.tw.cloud.db.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.tw.cloud.db.domain.CDBEndpoint;
import com.ibm.tw.cloud.db.domain.Connection;
import com.ibm.tw.cloud.db.domain.Deployable;
import com.ibm.tw.cloud.db.domain.Deployment;
import com.ibm.tw.cloud.db.domain.ListAllMyDatabaseResult;
import com.ibm.tw.cloud.db.domain.RedisConnectionResult;
import com.ibm.tw.cloud.db.domain.Task;
import com.ibm.tw.cloud.db.domain.User;
import com.ibm.tw.cloud.db.service.CloudDatabaseService;
import com.ibm.tw.cloud.iam.IBMCloudTokenGenerator;

@Service
public final class CloudDatabaseServiceImpl implements CloudDatabaseService {
	private static final Logger logger = LoggerFactory.getLogger(CloudDatabaseService.class);
	
	@Autowired
	protected IBMCloudTokenGenerator tokenGenerator;
	
	@Override
	public List<Deployable> listAll(CDBEndpoint endpoint, String apiKey) {
		if (apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey cannot be null nor empty.");
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s", endpoint, "deployables");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
		
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		logger.debug("uri: {}, method: {}, authorization: {}, requestBody: {}",
				uri, HttpMethod.GET, 
				entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getBody());
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		logger.debug("listAllResponse: {}", response.getBody());
		ObjectMapper objMapper = new ObjectMapper();
		ListAllMyDatabaseResult result = null;
		try {
			result = objMapper.readValue(response.getBody(), ListAllMyDatabaseResult.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result.getDeployables();
	}

	@Override
	public Deployment getDeploymentDetails(CDBEndpoint endpoint, String apiKey, String deploymentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task createUser(CDBEndpoint endpoint, String apiKey, String deploymentId, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task updateUserPassword(CDBEndpoint endpoint, String apiKey, String deploymentId, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task deleteUser(CDBEndpoint endpoint, String apiKey, String deploymentId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnectionInfo(CDBEndpoint endpoint, String apiKey, String deploymentId, String userId) {
		if (apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey cannot be null nor empty.");
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		deploymentId = deploymentId.replace(":", "%3A");
		deploymentId = deploymentId.replace("/", "%2F");
				
		String url = String.format("https://%s/%s/%s/%s/%s/%s", 
				endpoint, "deployments", "{id}", "users", "{userid}", "connections");
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", deploymentId);
		uriVariables.put("userid", "tsangcc");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
		
	
		HttpEntity<String> entity = new HttpEntity<>("", headers);		
		ResponseEntity<String> response;
		DefaultUriBuilderFactory uriTemplateHandler = new DefaultUriBuilderFactory();
		uriTemplateHandler.setEncodingMode(EncodingMode.NONE);
		restTemplate.setUriTemplateHandler(uriTemplateHandler);
		try {		
			logger.info("uri: {}", url);
			response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, uriVariables);
		} catch (RestClientException e1) {
			logger.error("", e1);
			throw new IllegalStateException(e1.getCause());
		}
		logger.debug("Connection info: {}", response.getBody());
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		Connection result = new RedisConnectionResult();
		try {
			result = objMapper.readValue(response.getBody(), RedisConnectionResult.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result;
	}

}
