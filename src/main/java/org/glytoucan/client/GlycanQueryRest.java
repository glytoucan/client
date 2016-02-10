package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.request.GlycanQueryRequest;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GlycanQueryRest implements GlycanQuerySpec {

	private static final Log logger = LogFactory.getLog(GlycanQueryRest.class);

	RestTemplate restTemplate = new RestTemplate();

	HashMap<String, Object> env;

	public HashMap<String, Object> getEnv() {
		return env;
	}

	public void setEnv(HashMap<String, Object> env) {
		this.env = env;
	}

	public GlycanQueryRest() {
	}

	private ResponseEntity<GlycanList> submit(HttpEntity<?> requestEntity, String cmd) {
		restTemplate.setErrorHandler(new ErrorHandler());
		logger.debug("request:>"+ env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH) + cmd);
		return restTemplate.exchange((String) env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH)
				+ cmd, HttpMethod.GET, requestEntity, GlycanList.class);
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

//		String auth = map.get(GlycanSpec.USERNAME) + ":" + map.get(GlycanSpec.API_KEY);
//		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
//		headers.add(GlycanSpec.AUTHORIZATION_HEADER, GlycanSpec.AUTH_BASIC_HEADER + new String(encodedAuthorisation));

		return headers;
	}

	@Override
	public Map<String, Object> getListStructures(Map<String, Object> gmap) {
		String cmd = GlycanSpec.LIST_CMD;
		GlycanQueryRequest req = new GlycanQueryRequest();
		req.setPayload("full");
		req.setLimit("100");
		req.setOffset("100");
		HttpEntity<?> requestEntity = new HttpEntity(req, getHeaders(env));
		final ResponseEntity<GlycanList> responseEntity = submit(requestEntity, cmd);
		gmap.put(GlycanSpec.MESSAGE, responseEntity.getBody());
		return gmap;
	}
}