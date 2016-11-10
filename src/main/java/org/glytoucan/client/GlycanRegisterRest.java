package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanRequest;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

public class GlycanRegisterRest extends AuthenticatedApi implements GlycanClientRegisterSpec {

	private static final Log logger = LogFactory.getLog(GlycanRegisterRest.class);

	RestTemplate restTemplate = new RestTemplate();

	HashMap<String, Object> env;

	public HashMap<String, Object> getEnv() {
		return env;
	}

	public void setEnv(HashMap<String, Object> env) {
		this.env = env;
	}

	public GlycanRegisterRest() {
	}

	@Override
	public Map<String, Object> registerStructure(Map<String, Object> gmap) {
		
		if (gmap.containsKey(USERNAME))
			env.put(USERNAME, gmap.get(USERNAME));
		if (gmap.containsKey(API_KEY))
			env.put(API_KEY, gmap.get(API_KEY));
		
		String sequence = (String) gmap.get(GlycanClientRegisterSpec.SEQUENCE);
		String dbId = (String) gmap.get(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID);
                String cmd = (String) gmap.get(GlycanClientRegisterSpec.REGISTER_CMD);
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("dbId:>" + dbId + "<");
		
		GlycanRequest req = new GlycanRequest();
		req.setPublicDatabaseStructureId(dbId);
		req.setSequence(sequence);

		@SuppressWarnings({ "unchecked", "rawtypes" }) // for some reason
														// setting this to
														// HttpHeader fails auth
		HttpEntity<?> requestEntity = new HttpEntity(req, getHeaders(env));
		final ResponseEntity<Message> responseEntity = submit(requestEntity, cmd);
		gmap.put(MESSAGE, responseEntity.getBody());
		return gmap;
	}

	private ResponseEntity<Message> submit(HttpEntity<?> requestEntity, String cmd) {
		restTemplate.setErrorHandler(new ErrorHandler());
		if (StringUtils.isBlank(cmd))
		  cmd = GlycanClientRegisterSpec.REGISTER_CMD;
		logger.debug("request:>"+ env.get(GlycanClientRegisterSpec.HOSTNAME) + (String) env.get(GlycanClientRegisterSpec.CONTEXT_PATH) + cmd);
		return restTemplate.exchange((String) env.get(GlycanClientRegisterSpec.HOSTNAME) + (String) env.get(GlycanClientRegisterSpec.CONTEXT_PATH)
				+ cmd, HttpMethod.POST, requestEntity, Message.class);
	}

	static HttpHeaders getHeaders(Map<String, Object> map) {
		HttpHeaders headers = new HttpHeaders();

		if (null != map.get(GlycanClientRegisterSpec.CONTENT_TYPE))
			headers.setContentType((MediaType) map.get(GlycanClientRegisterSpec.CONTENT_TYPE));
		else
			headers.setContentType(MediaType.APPLICATION_JSON);

		if (null != map.get(GlycanClientRegisterSpec.MEDIA_TYPE))
			headers.setAccept(Arrays.asList((MediaType) map.get(GlycanClientRegisterSpec.MEDIA_TYPE)));
		else
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		String auth = map.get(USERNAME) + ":" + map.get(API_KEY);
		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
		headers.add(GlycanClientRegisterSpec.AUTHORIZATION_HEADER, GlycanClientRegisterSpec.AUTH_BASIC_HEADER + new String(encodedAuthorisation));

		return headers;
	}
}