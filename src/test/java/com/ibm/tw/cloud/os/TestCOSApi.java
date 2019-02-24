package com.ibm.tw.cloud.os;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.ibm.tw.cloud.iam.IBMCloudTokenGenerator;
import com.ibm.tw.cloud.os.domain.Bucket;
import com.ibm.tw.cloud.os.domain.ListBucketResult;
import com.ibm.tw.cloud.os.service.CloudObjectStorageService;
@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource(locations="classpath:test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCOSApi {

	private final Logger logger = LoggerFactory.getLogger(TestCOSApi.class);
	
	@Autowired
	@Qualifier("restTemplate")
	CloudObjectStorageService restTemplateCOSService;
	
	@Autowired
	IBMCloudTokenGenerator tokenGenerator;
	
	@Test
	public void _1_makeSureTheCOSServiceIsWired() {
		assertNotNull(restTemplateCOSService);
		assertNotNull(tokenGenerator);
		assertNotNull(System.getenv("IBM_S3_API_KEY"));
		// A system environment variable IBM_S3_API_KEY must be set to the value of the COS API Key.
		assertTrue(!System.getenv("IBM_S3_API_KEY").isEmpty());
		
		tokenGenerator.generateToken(System.getenv("IBM_S3_API_KEY"));
		assertTrue(!tokenGenerator.getToken().isEmpty());
		logger.info(tokenGenerator.getToken());
		logger.info(restTemplateCOSService.info());
		
	}

	@Test
	public void _2_aNewBucketIsCreated() {
		String bucketName = "com-ibm-tw-cos-testing";
		restTemplateCOSService.createBucket(bucketName);
		List<Bucket> buckets = restTemplateCOSService.listBuckets();
		Set<String> bucketNames = new HashSet<>();
		buckets.forEach(bucket -> {
			bucketNames.add(bucket.getName());
		});
		
		assertTrue(bucketNames.contains(bucketName));
	}
	
	@Test
	public void _3_twoNewTextFilesAreCreated() {
		String bucketName = "com-ibm-tw-cos-testing";
		String itemName1 = "text1.txt";
		String itemName2 = "text2.txt";
		restTemplateCOSService.createTextFile(bucketName, itemName1, "This is a test in text1.txt.");
		restTemplateCOSService.createTextFile(bucketName, itemName2, "This is a test in text2.txt.");
		ListBucketResult result = restTemplateCOSService.listObjects(bucketName);
		Set<String> objectNames = new HashSet<>();
		
		result.getContents().forEach(content -> {
			objectNames.add(content.getKey());
		});
		
		assertTrue(objectNames.contains(itemName1));
		assertTrue(objectNames.contains(itemName2));
	}
	
	@Test
	public void _4_allTextFilesAreDowloaded() {
		String bucketName = "com-ibm-tw-cos-testing";
		String itemName1 = "text1.txt";
		String itemName2 = "text2.txt";
		restTemplateCOSService.downloadObject(bucketName, itemName1,  itemName1);
		restTemplateCOSService.downloadObject(bucketName, itemName2,  itemName2);
		
		List<String> list = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Paths.get(itemName1))) {
			lines.forEach( line -> {
				list.add(line);
			});
			assertEquals(1, list.size());
			assertEquals("This is a test in text1.txt.", list.get(0));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		};
		
		list.clear();
		try (Stream<String> lines = Files.lines(Paths.get(itemName2))) {
			lines.forEach( line -> {
				list.add(line);
			});
			assertEquals(1, list.size());
			assertEquals("This is a test in text2.txt.", list.get(0));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		};
	}
	
	@Test
	public void _5_allTextFilesAreDeleted() {
		String bucketName = "com-ibm-tw-cos-testing";
		String itemName1 = "text1.txt";
		String itemName2 = "text2.txt";
		String[] args = {itemName1, itemName2};
		boolean failed = true;
		int retry = 0;
		int retryLimit = 10;
		
		ListBucketResult result = null;
		while (failed && retry < retryLimit) {
			try {
				restTemplateCOSService.removeObject(bucketName, args);
				result = restTemplateCOSService.listObjects(bucketName);
				failed = false;
			} catch (HttpClientErrorException e) {
				if (e.getMessage().equals("409 Conflict"));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
				retry++;
			}
		}
		assertTrue(!failed);
		assertNotNull(result);
		assertEquals(0, result.getContents().size());
	}
	
	@Test
	public void _6_TheBucketIsDeleted() {
		String bucketName = "com-ibm-tw-cos-testing";
		restTemplateCOSService.removeBucket(bucketName);
		List<Bucket> buckets = restTemplateCOSService.listBuckets();
		Set<String> bucketNames = new HashSet<>();
		buckets.forEach(bucket -> {
			bucketNames.add(bucket.getName());
		});
		
		assertTrue(!bucketNames.contains(bucketName));
	}
}
