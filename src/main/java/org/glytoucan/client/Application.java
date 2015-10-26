package org.glytoucan.client;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Log logger = LogFactory.getLog(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(
				Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity requestEntity = new HttpEntity(getHeaders("aoki" + ":"
				+ "asdf"));
		final ResponseEntity<Message> responseEntity = restTemplate.exchange(
				"http://localhost:8080/test", HttpMethod.GET, requestEntity,
				Message.class);
		logger.debug(">" + responseEntity.getBody().toString());
	}

	static HttpHeaders getHeaders(String auth) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
		headers.add("Authorization", "Basic "
				+ new String(encodedAuthorisation));

		return headers;
	}

}
