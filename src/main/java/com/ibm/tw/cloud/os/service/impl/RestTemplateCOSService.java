package com.ibm.tw.cloud.os.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ibm.tw.cloud.iam.IBMCloudTokenGenerator;
import com.ibm.tw.cloud.os.domain.Bucket;
import com.ibm.tw.cloud.os.domain.DeleteMultipleObjectsXMLRequest;
import com.ibm.tw.cloud.os.domain.ListAllMyBucketsResult;
import com.ibm.tw.cloud.os.domain.ListBucketResult;
import com.ibm.tw.cloud.os.service.CloudObjectStorageService;

@Component("restTemplate")
public class RestTemplateCOSService implements CloudObjectStorageService {
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateCOSService.class);
	private static final String endpoint = System.getenv("IBM_S3_PUBLIC_ENDPOINT");
	private static final String resourceId = System.getenv("IBM_S3_RESOURCE_ID");
	private static final String apiKey = System.getenv("IBM_S3_API_KEY");
	
	@Autowired
	IBMCloudTokenGenerator tokenGenerator;
	
	private void validateEnv() {
		if (endpoint == null) throw new IllegalStateException("System Environment variable IBM_S3_PUBLIC_ENDPOINT has not been set.");
		if (resourceId == null) throw new IllegalStateException("System Environment variable IBM_S3_RESOURCE_ID has not been set.");
		if (apiKey == null) throw new IllegalStateException("System Environment variable IBM_S3_API_KEY has not been set.");
	}
	
	@Override
	public List<Bucket> listBuckets() {
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/", endpoint);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		headers.add("ibm-service-instance-id", resourceId);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		logger.debug("listBucketResponse: {}", response.getBody());
		XmlMapper xmlMapper = new XmlMapper();
		ListAllMyBucketsResult result = null;
		try {
			result = xmlMapper.readValue(response.getBody(), ListAllMyBucketsResult.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result.getBuckets();
	}

	@Override
	public ListBucketResult listObjects(String bucketname) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname must have a value.");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s", endpoint, bucketname);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		logger.debug("listObjectsResponse: {}", response.getBody());
		XmlMapper xmlMapper = new XmlMapper();
		ListBucketResult result = null;
		try {
			result = xmlMapper.readValue(response.getBody(), ListBucketResult.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result;
	}

	@Override
	public void createBucket(String bucketname) {
		if (bucketname == null) throw new IllegalArgumentException("bucketname cannot be null");
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s", endpoint, bucketname);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		headers.add("ibm-service-instance-id", resourceId);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		logger.debug("CreateBucketResponse: {}", response.getBody());
	}

	@Override
	public void createTextFile(String bucketname, String itemName, String fileText) {
		if (bucketname == null) throw new IllegalArgumentException("bucketname cannot be null");
		if (itemName == null) throw new IllegalArgumentException("itemName cannot be null");

		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s/%s", endpoint, bucketname, itemName);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		headers.setContentType(MediaType.TEXT_PLAIN);
		HttpEntity<String> entity = new HttpEntity<>(fileText, headers);
		
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		logger.debug("CreateTextFileResponse: {}", response.getBody());
	}

	@Override
	public void downloadObject(String bucketname, String objectName, String filename) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname cannot be null");
		if (objectName == null || objectName.isEmpty()) throw new IllegalArgumentException("objectName cannot be null");
		if (filename == null || filename.isEmpty()) throw new IllegalArgumentException("filename cannot be null");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		
		String uri = String.format("https://%s/%s/%s", endpoint, bucketname, objectName);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		
		ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				Files.write(Paths.get(filename), response.getBody());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		} else {
			throw new IllegalStateException(String.format("Http Response Error Code %s", response.getStatusCode())); 
		}
	}

	@Override
	public void uploadFile(String bucketname, String objectKey, File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObject(String bucketname, String objectKey) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname cannot be null");
		if (objectKey == null || objectKey.isEmpty()) throw new IllegalArgumentException("objectKey cannot be null");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s/%s", endpoint, bucketname, objectKey);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
		logger.debug("removeObjectResponse: {}", response.getBody());
	}

	@Override
	public void removeBucket(String bucketname) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname cannot be null");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s/", endpoint, bucketname);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
		logger.debug("removeBucketResponse: {}", response.getBody());
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeObject(String bucketname, String... objectKeys) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname cannot be null");
		if (objectKeys == null || objectKeys.length == 0) throw new IllegalArgumentException("objectKey cannot be null");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey);
		String token = tokenGenerator.getToken();
		RestTemplate restTemplate = new RestTemplate();
		
		String uri = String.format("https://%s/%s?delete", endpoint, bucketname);
		
		DeleteMultipleObjectsXMLRequest.Delete deleteObjectIds = 
				new DeleteMultipleObjectsXMLRequest.Delete();
		
		for (String objectKey : objectKeys) {
			deleteObjectIds.add(new DeleteMultipleObjectsXMLRequest.BucketObject(objectKey));
		}
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
		String xmlDoc = "";
		String md5base64Hash = "";
		String data;
		try {
			xmlDoc = xmlMapper.writeValueAsString(deleteObjectIds);
			data = xmlDoc;
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(xmlDoc.getBytes());
			byte[] digest = messageDigest.digest();
			md5base64Hash = new String(Base64.getEncoder().encode(digest));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getCause());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getCause());
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
		headers.add(HttpHeaders.CONTENT_TYPE, String.format("%s;%s", MediaType.TEXT_PLAIN, "charset=utf-8"));
		headers.add("Content-MD5", md5base64Hash);
		HttpEntity<String> entity = new HttpEntity<>(data, headers);
		logger.debug("DeleteObjectIds: {}", xmlDoc);
		logger.debug("uri: {}, method: {}, authorization: {}, service-id: {}, requestBody: {}",
				uri, entity.getHeaders().getAccessControlRequestMethod(), entity.getHeaders().get(HttpHeaders.AUTHORIZATION),
				entity.getHeaders().get("ibm-service-instance-id"),
				entity.getBody());
		restTemplate.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return !response.getStatusCode().equals(HttpStatus.OK);
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				
				try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), Charset.forName("UTF-8")))) {	
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line);
					}
				}
				logger.error(stringBuilder.toString());
			}
			
		});
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		
		logger.debug("removeObjectResponse: {}", response.getBody());
	}

}
