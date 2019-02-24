package com.ibm.tw.cloud.iam;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class IBMCloudTokenGenerator {
	private static final Logger logger = LoggerFactory.getLogger(IBMCloudTokenGenerator.class);
	public static final String COS_AUTH_ENDPOINT = "iam.cloud.ibm.com/oidc/token";
	private String token = "";
	
	public String getToken() {
		return token;
	}

	public void generateToken(String apiKey) {
		if (apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey cannot be null or empty");
		RestTemplate restTemplate = new RestTemplate();
			
		String uri = String.format("https://%s", COS_AUTH_ENDPOINT);
		HttpHeaders headers = new HttpHeaders();
		
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		
		requestBody.add("apikey", apiKey);
		requestBody.add("response_type", "cloud_iam");
		requestBody.add("grant_type", "urn:ibm:params:oauth:grant-type:apikey");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		logger.debug("jsonResponse: {}", response.getBody());
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(response.getBody());
			this.token = node.get("access_token").asText();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
