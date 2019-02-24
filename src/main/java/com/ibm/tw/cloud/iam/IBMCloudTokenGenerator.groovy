package com.ibm.tw.cloud.iam

import org.springframework.stereotype.Component

import com.fasterxml.jackson.databind.ObjectMapper

@Component
class IBMCloudTokenGenerator {
	public static final String COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
	private String token = "";
	
	public String getToken() {
		return token;
	}

	public void generateToken(String apiKey) {
		def httpUrl = new StringBuilder();
		httpUrl.append("curl -X POST ").append(COS_AUTH_ENDPOINT)
		       .append(" -H 'Accept: application/json'")
			   .append(" -H 'Content-Type: application/x-www-form-urlencoded'")
			   .append(" --data-urlencode \"apikey=").append(apiKey).append("\"")
			   .append(" --data-urlencode \"response_type=cloud_iam\"")
			   .append(" --data-urlencode \"grant_type=urn:ibm:params:oauth:grant-type:apikey\"");
			   
		def command = httpUrl.toString()
		def process = ['bash', '-c', command].execute()
		def output = new StringBuilder()
		process.waitForProcessOutput(output, output);
		if (process.exitValue() != 0) throw new IllegalStateException(output.toString());
		int startPos = output.indexOf("{");
		def jsonObject = output.toString().substring(startPos);
		def mapper = new ObjectMapper();
		try {
			def node = mapper.readTree(jsonObject);
			this.token = node.get("access_token").asText();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
