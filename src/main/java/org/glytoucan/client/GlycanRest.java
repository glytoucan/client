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
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		restTemplate.setErrorHandler(new ErrorHandler());
		logger.debug("request:>"+ env.get(GlycanSpec.HOSTNAME) + (String) env.get(GlycanSpec.CONTEXT_PATH) + cmd);
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

  public String getImageWurcs(String hostname, String sequence) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    Map<String,Object> data = new HashMap<String,Object>();
    data.put("structure", sequence);
    data.put("encoding", "wurcs");
    return getImage(hostname, sequence, data);
  }
  
  public String getImage(String hostname, String sequence) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    Map<String,Object> data = new HashMap<String,Object>();
    data.put("structure", sequence);
    data.put("encoding", "glycoct");
    return getImage(hostname, sequence, data);
  }

	 public String getImage(String hostname, String sequence, Map<String, Object> data) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//   String url = hostname + "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
   String url = hostname + "/glycans/image/glycan?format=png&notation=cfg&style=extended";
 
   SSLContextBuilder builder = new SSLContextBuilder();
     builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
             builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
     CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(
             sslsf).build();
   HttpPost post = new HttpPost(url);
   logger.debug("url:>" + url);

   // add header
   post.setHeader("Content-Type", "application/json");

   List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
   
   ObjectMapper mapper = new ObjectMapper();
//   String output = mapper.writeValueAsString(data);
   String output = "{\"format\":\"" + data.get("encoding") + "\",\"sequence\":\"" + sequence + "\"}"; 
//   logger.debug("output:"+output);
   logger.debug("output:"+output);
   
   post.setEntity(new StringEntity(output));
   
//   HttpClient client = HttpClientBuilder.create().build();

   HttpResponse response = client.execute(post);
   int code = response.getStatusLine().getStatusCode();
   logger.debug("status code:"+code);
   if (code != 200) {
     return null;
   }

   BufferedReader rd = new BufferedReader(
           new InputStreamReader(response.getEntity().getContent()));

//   StringBuilder result = new StringBuilder();
//   String line = "";
//   while ((line = rd.readLine()) != null) {
//     logger.debug("line:>" + line);
//     result.append(line);
//   }
       BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());

   String image = encodeToString(bufimage, "png"); 
   
   return "data:image/png;base64," + image;
 }
 
 String getRegisteredImage(String hostname) throws IOException {
   String url = hostname + "/glycans/image/glycan?format=png&notation=cfg&style=extended'";
 
   HttpClient client = HttpClientBuilder.create().build();
   HttpGet request = new HttpGet(url);
   HttpResponse response = client.execute(request);
   int code = response.getStatusLine().getStatusCode();
   logger.debug("status code:"+code);
   if (code != 200) {
     return null;
   }
   BufferedReader rd = new BufferedReader(
       new InputStreamReader(response.getEntity().getContent()));

   StringBuilder result = new StringBuilder("data:image/png;base64,");
   String line = "";
   while ((line = rd.readLine()) != null) {
     result.append(DatatypeConverter.printBase64Binary(line.getBytes()) + "\n");
   }
   return result.toString();
 }
 
   /**
    * Decode string to image
    * @param imageString The string to decode
    * @return decoded image
    */
   public static BufferedImage decodeToImage(String imageString) {

       BufferedImage image = null;
       byte[] imageByte;
       try {
           imageByte = Base64.decodeBase64(imageString);
           ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
           image = ImageIO.read(bis);
           bis.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return image;
   }

   /**
    * Encode image to string
    * @param image The image to encode
    * @param type jpeg, bmp, ...
    * @return encoded string
    */
   public static String encodeToString(BufferedImage image, String type) {
       String imageString = null;
       ByteArrayOutputStream bos = new ByteArrayOutputStream();

       try {
           ImageIO.write(image, type, bos);
           byte[] imageBytes = bos.toByteArray();

           imageString = Base64.encodeBase64String(imageBytes);

           bos.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return imageString;
   }
   
}