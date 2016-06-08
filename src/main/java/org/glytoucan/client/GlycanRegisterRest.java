package org.glytoucan.client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.glytoucan.model.GlycanRequest;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlycanRegisterRest implements GlycanClientRegisterSpec {

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
		
		String sequence = (String) gmap.get(GlycanClientRegisterSpec.SEQUENCE);
		String dbId = (String) gmap.get(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID);
		
		GlycanRequest req = new GlycanRequest();
		req.setPublicDatabaseStructureId(dbId);
		req.setSequence(sequence);

		String cmd = GlycanClientRegisterSpec.REGISTER_CMD;
		@SuppressWarnings({ "unchecked", "rawtypes" }) // for some reason
														// setting this to
														// HttpHeader fails auth
		HttpEntity<?> requestEntity = new HttpEntity(req, getHeaders(env));
		final ResponseEntity<Message> responseEntity = submit(requestEntity, cmd);
		gmap.put(GlycanClientRegisterSpec.MESSAGE, responseEntity.getBody());
		return gmap;
	}

	private ResponseEntity<Message> submit(HttpEntity<?> requestEntity, String cmd) {
		restTemplate.setErrorHandler(new ErrorHandler());
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

		String auth = map.get(GlycanClientRegisterSpec.USERNAME) + ":" + map.get(GlycanClientRegisterSpec.API_KEY);
		byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
		headers.add(GlycanClientRegisterSpec.AUTHORIZATION_HEADER, GlycanClientRegisterSpec.AUTH_BASIC_HEADER + new String(encodedAuthorisation));

		return headers;
	}


}