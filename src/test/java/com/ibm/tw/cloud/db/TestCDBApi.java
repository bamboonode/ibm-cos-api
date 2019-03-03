package com.ibm.tw.cloud.db;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.ibm.tw.cloud.db.domain.CDBEndpoint;
import com.ibm.tw.cloud.db.domain.Connection;
import com.ibm.tw.cloud.db.domain.Deployable;
import com.ibm.tw.cloud.db.service.CloudDatabaseService;

@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource(locations="classpath:test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCDBApi {
	private static final Logger logger = LoggerFactory.getLogger(TestCDBApi.class);
	
	@Autowired
	private CloudDatabaseService cdbService;
	
	@Value("${db.apikey}")
	private String apiKey;
	
	//@Test
	public void _1_successfullyGetDeployableDatabases() {
		List<Deployable> result = null;
		try {
			result = cdbService.listAll(CDBEndpoint.AU_SYD, apiKey);
		} catch (RestClientException e) {
			logger.error("Error: {}", e);
			fail();
		}
		logger.info("Deployable databases: {}", result);
	}
	
	@Test
	public void _2_successfullyGetRedisConnectionInfo() {
		Connection result = null;
		String deploymentId = "crn:v1:bluemix:public:databases-for-redis:au-syd:a/8c898b6db8924ed4942a8bb11f8fc29e:408c95a2-f97d-4029-ae12-d302e72ebb89::";
		
		try {
			result = cdbService.getConnectionInfo(CDBEndpoint.AU_SYD, apiKey, deploymentId, "tsangcc");
		} catch (RestClientException e) {
			logger.error("Error: {}", e);
			fail();
		}
		logger.info("Redis Connection Info: {}", result);
	}
}
