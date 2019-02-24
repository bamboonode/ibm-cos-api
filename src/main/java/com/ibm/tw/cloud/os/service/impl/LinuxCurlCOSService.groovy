package com.ibm.tw.cloud.os.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.ibm.tw.cloud.iam.IBMCloudTokenGenerator
import com.ibm.tw.cloud.os.domain.Bucket
import com.ibm.tw.cloud.os.domain.ListAllMyBucketsResult
import com.ibm.tw.cloud.os.domain.ListBucketResult
import com.ibm.tw.cloud.os.service.CloudObjectStorageService

@Component
class LinuxCurlCOSService implements CloudObjectStorageService {
	private static final Logger logger = LoggerFactory.getLogger(LinuxCurlCOSService.class);
	private static final String endpoint = System.getenv("IBM_S3_PUBLIC_ENDPOINT");
	private static final String resourceId = System.getenv("IBM_S3_RESOURCE_ID");
	private static final String apiKey = System.getenv("IBM_S3_API_KEY");
	
	@Autowired
	IBMCloudTokenGenerator tokenGenerator;

	@Override
	public List<Bucket> listBuckets() {
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey)
		def token = tokenGenerator.getToken()
		def url = new StringBuilder()
		url.append("curl \"https://").append(endpoint).append("/\"")
				.append(" -H \"Authorization: bearer ").append(token).append("\"")
				.append(" -H \"ibm-service-instance-id: ").append(resourceId).append("\"")

		def command = url.toString()
		logger.debug("command: {}", command)
		def process = ['bash', '-c', command].execute()
		def output = new StringBuilder()
		process.waitForProcessOutput(output, output)
		logger.debug(output.toString());
		if (process.exitValue() != 0) throw new IllegalStateException(output.toString())
		String xmlString = output.toString().substring(output.indexOf("<ListAllMyBucketsResult"));
		logger.debug("Result xml: {}", xmlString);
		def xmlMapper = new XmlMapper();
		ListAllMyBucketsResult result = xmlMapper.readValue(xmlString, ListAllMyBucketsResult.class)
		return result.getBuckets()
	}

	@Override
	public ListBucketResult listObjects(String bucketname) {
		if (bucketname == null || bucketname.isEmpty()) throw new IllegalArgumentException("bucketname must have a value.");
		
		validateEnv();
		if (tokenGenerator.getToken().isEmpty()) tokenGenerator.generateToken(apiKey)
		def token = tokenGenerator.getToken()
		def url = new StringBuilder()
		url.append("curl \"https://").append(endpoint).append("/").append(bucketname).append("\"")
				.append(" -H \"Authorization: bearer ").append(token).append("\"")

		def command = url.toString()
		logger.debug("command: {}", command)
		def process = ['bash', '-c', command].execute()
		def output = new StringBuilder()
		process.waitForProcessOutput(output, output)
		logger.debug(output.toString());
		if (process.exitValue() != 0) throw new IllegalStateException(output.toString())
		String xmlString = output.toString().substring(output.indexOf("<ListBucketResult"));
		logger.debug("Result xml: {}", xmlString);
		def xmlMapper = new XmlMapper();
		ListBucketResult result = xmlMapper.readValue(xmlString, ListBucketResult.class)
		return result;
	}

	private void validateEnv() {
		if (endpoint == null) throw new IllegalStateException("System Environment variable IBM_S3_PUBLIC_ENDPOINT has not been set.");
		if (resourceId == null) throw new IllegalStateException("System Environment variable IBM_S3_RESOURCE_ID has not been set.");
		if (apiKey == null) throw new IllegalStateException("System Environment variable IBM_S3_API_KEY has not been set.");
	}

	@Override
	public void createBucket(String bucketname) {
		if (bucketname == null) throw new IllegalArgumentException("bucketname cannot be null")
		validateEnv()
		if (tokenGenerator.getToken().isEmpty()) {
			tokenGenerator.generateToken(apiKey)
		}
		def token = tokenGenerator.getToken()

		def url = new StringBuilder()
		url.append("curl -X \"PUT\" \"https://").append(endpoint).append("/").append(bucketname).append("\"")
				.append(" -H \"Authorization: Bearer ").append(token).append("\"")
				.append(" -H \"ibm-service-instance-id: ").append(resourceId).append("\"")
		
		def command = url.toString()
		logger.debug("command: {}", command)
		def process = ['bash', '-c', command].execute()
		def output = new StringBuilder()
		process.waitForProcessOutput(output, output)
		if (process.exitValue() != 0) throw new IllegalStateException(output.toString())
	}

	@Override
	public void createTextFile(String bucketname, String itemName, String fileText) {
		if (bucketname == null) throw new IllegalArgumentException("bucketname cannot be null")
		if (itemName == null) throw new IllegalArgumentException("itemName cannot be null")

		validateEnv()
		if (tokenGenerator.getToken().isEmpty()) {
			tokenGenerator.generateToken(apiKey)
		}
		def token = tokenGenerator.getToken()

		def url = new StringBuilder()
		url.append("curl -X \"PUT\" \"https://").append(endpoint).append("/")
		   .append(bucketname).append("/").append(itemName).append("\"")
		   .append(" -H \"Authorization: Bearer ").append(token).append("\"")
		   .append(" -H \"Content-Type: text/html; charset=utf-8\"")
		   .append(" -d \"").append(fileText).append("\"");
		
		def command = url.toString()
		logger.debug("command: {}", command)
		def process = ['bash', '-c', command].execute()
		def output = new StringBuilder()
		process.waitForProcessOutput(output, output)
		if (process.exitValue() != 0) throw new IllegalStateException(output.toString())
	}

	@Override
	public void downloadObject(String bucketname, String objectName, String filename) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadFile(String bucketname, String objectKey, File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObject(String bucketname, String objectKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBucket(String bucketname) {
		// TODO Auto-generated method stub

	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeObject(String bucketname, String... objectKeys) {
		// TODO Auto-generated method stub
		
	}
}
