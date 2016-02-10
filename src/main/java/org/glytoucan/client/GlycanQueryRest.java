package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

	private ResponseEntity<GlycanList> submit(HttpEntity<?> requestEntity, String cmd, Map<String, Object> queryParamsMap) {
		restTemplate.setErrorHandler(new ErrorHandler());
		logger.debug("request:>"+ env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH) + cmd);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl((String) env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH) + cmd);;

		for (String key : queryParamsMap.keySet()) {
			builder.queryParam(key, queryParamsMap.get(key));
		}
		
//        .queryParam("msisdn", msisdn)
//        .queryParam("email", email)
//        .queryParam("clientVersion", clientVersion)
//        .queryParam("clientType", clientType)
//        .queryParam("issuerName", issuerName)
//        .queryParam("applicationName", applicationName);

//		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, GlycanList.class);
		
//		return restTemplate.exchange(
//				, HttpMethod.GET, requestEntity, GlycanList.class);
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
		HttpEntity<?> requestEntity = new HttpEntity(getHeaders(env));
		final ResponseEntity<GlycanList> responseEntity = submit(requestEntity, cmd, gmap);
		gmap.put(GlycanSpec.MESSAGE, responseEntity.getBody());
		return gmap;
	}
}