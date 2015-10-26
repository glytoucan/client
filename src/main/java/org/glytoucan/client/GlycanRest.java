package org.glytoucan.client;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanMap;
import org.glytoucan.model.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

public class GlycanRest implements GlycanSpec {
	
	private static final Log logger = LogFactory.getLog(GlycanRest.class);

	public GlycanRest() {
	}
	
	@Override
	public GlycanMap registerStructure(GlycanMap gmap) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity requestEntity = new HttpEntity(getHeaders(gmap));
		final ResponseEntity<Message> responseEntity = restTemplate.exchange(
				gmap.getValue(GlycanSpec.HOSTNAME) + gmap.getValue(GlycanSpec.REGISTRATION), HttpMethod.GET, requestEntity,
				Message.class);
		logger.debug(">" + responseEntity.getBody().toString());
		
		Message msg = responseEntity.getBody();
		gmap.setValueObject(GlycanSpec.MESSAGE, msg);
		return gmap;
	}
	
	static HttpHeaders getHeaders(GlycanMap gmap) {
		
		HttpHeaders headers = new HttpHeaders();
		
		if (null != gmap.getValue(GlycanSpec.CONTENT_TYPE))
			headers.setContentType((MediaType)gmap.getValueObject(GlycanSpec.CONTENT_TYPE));
		else
			headers.setContentType(MediaType.APPLICATION_JSON);

		if (null != gmap.getValue(GlycanSpec.MEDIA_TYPE))
			headers.setAccept(Arrays.asList((MediaType)gmap.getValueObject(GlycanSpec.MEDIA_TYPE)));
		else
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		String auth = gmap.getValue(GlycanSpec.USERNAME) + ":"	+ gmap.getValue(GlycanSpec.PASSWORD);
		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
		headers.add(GlycanSpec.AUTHORIZATION_HEADER, GlycanSpec.AUTH_BASIC_HEADER
				+ new String(encodedAuthorisation));

		return headers;
	}

}
