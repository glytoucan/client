package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanRequest;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

public class GlycanRest implements GlycanSpec {

	private static final Log logger = LogFactory.getLog(GlycanRest.class);

	RestTemplate restTemplate = new RestTemplate();

	HashMap<String, Object> env;

	public HashMap<String, Object> getEnv() {
		return env;
	}

	public void setEnv(HashMap<String, Object> env) {
		this.env = env;
	}

	public GlycanRest() {
	}

	@Override
	public Map<String, Object> registerStructure(Map<String, Object> gmap) {
		
		String sequence = (String) gmap.get(GlycanSpec.SEQUENCE);
		String dbId = (String) gmap.get(GlycanSpec.PUBLIC_DATABASE_STRUCTURE_ID);
		
		GlycanRequest req = new GlycanRequest();
		req.setPublicDatabaseStructureId(dbId);
		req.setSequence(sequence);

		String cmd = GlycanSpec.REGISTER_CMD;
		@SuppressWarnings({ "unchecked", "rawtypes" }) // for some reason
														// setting this to
														// HttpHeader fails auth
		HttpEntity<?> requestEntity = new HttpEntity(req, getHeaders(env));
		final ResponseEntity<Message> responseEntity = submit(requestEntity, cmd);
		gmap.put(GlycanSpec.MESSAGE, responseEntity.getBody());
		return gmap;
	}

	private ResponseEntity<Message> submit(HttpEntity<?> requestEntity, String cmd) {
		return restTemplate.exchange((String) env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH)
				+ cmd, HttpMethod.POST, requestEntity, Message.class);
	}

	static HttpHeaders getHeaders(Map<String, Object> map) {
		HttpHeaders headers = new HttpHeaders();

		if (null != map.get(GlycanSpec.CONTENT_TYPE))
			headers.setContentType((MediaType) map.get(GlycanSpec.CONTENT_TYPE));
		else
			headers.setContentType(MediaType.APPLICATION_JSON);

		if (null != map.get(GlycanSpec.MEDIA_TYPE))
			headers.setAccept(Arrays.asList((MediaType) map.get(GlycanSpec.MEDIA_TYPE)));
		else
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		String auth = map.get(GlycanSpec.USERNAME) + ":" + map.get(GlycanSpec.API_KEY);
		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
		headers.add(GlycanSpec.AUTHORIZATION_HEADER, GlycanSpec.AUTH_BASIC_HEADER + new String(encodedAuthorisation));

		return headers;
	}
}