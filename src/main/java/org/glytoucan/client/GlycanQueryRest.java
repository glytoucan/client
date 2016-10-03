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
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlycanQueryRest implements GlycanClientQuerySpec {

  public static String BASE64HEADER = "data:image/png;base64,";

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

  private ResponseEntity<GlycanList> submit(HttpEntity<?> requestEntity, String cmd,
      Map<String, Object> queryParamsMap) {
    restTemplate.setErrorHandler(new ErrorHandler());
    logger.debug("request:>" + env.get(GlycanClientRegisterSpec.HOSTNAME)
        + (String) env.get(GlycanClientRegisterSpec.CONTEXT_PATH) + cmd);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl((String) env.get(GlycanClientRegisterSpec.HOSTNAME)
        + (String) env.get(GlycanClientRegisterSpec.CONTEXT_PATH) + cmd);
    ;

    for (String key : queryParamsMap.keySet()) {
      builder.queryParam(key, queryParamsMap.get(key));
    }

    // .queryParam("msisdn", msisdn)
    // .queryParam("email", email)
    // .queryParam("clientVersion", clientVersion)
    // .queryParam("clientType", clientType)
    // .queryParam("issuerName", issuerName)
    // .queryParam("applicationName", applicationName);

    // HttpEntity<?> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, GlycanList.class);

    // return restTemplate.exchange(
    // , HttpMethod.GET, requestEntity, GlycanList.class);
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

    // String auth = map.get(GlycanSpec.USERNAME) + ":" +
    // map.get(GlycanSpec.API_KEY);
    // byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
    // headers.add(GlycanSpec.AUTHORIZATION_HEADER, GlycanSpec.AUTH_BASIC_HEADER
    // + new String(encodedAuthorisation));

    return headers;
  }

  @Override
  public Map<String, Object> getListStructures(Map<String, Object> gmap) {
    String cmd = GlycanClientRegisterSpec.LIST_CMD;
    HttpEntity<?> requestEntity = new HttpEntity(getHeaders(env));
    final ResponseEntity<GlycanList> responseEntity = submit(requestEntity, cmd, gmap);
    gmap.put(GlycanRegisterRest.MESSAGE, responseEntity.getBody());
    return gmap;
  }

  @Override
  public byte[] getImage(Map<String, Object> data)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    if (data.containsKey(GlycanClientQuerySpec.ID)) {
      return getRegisteredImage(data);
    }

    String params = getParams(data);
    // String url = hostname +
    // "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
    String url = env.get(GlycanClientQuerySpec.HOSTNAME) + "/glycans/image/glycan?" + getParams(data);
    logger.debug("request:>" + url);

    SSLContextBuilder builder = new SSLContextBuilder();
    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    HttpPost post = new HttpPost(url);
    logger.debug("url:>" + url);

    // add header
    post.setHeader("Content-Type", "application/json");

    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

    String output = "{\"format\":\"" + data.get("encoding") + "\",\"sequence\":\""
        + data.get(GlycanClientQuerySpec.SEQUENCE) + "\"}";
    logger.debug("output:" + output);

    post.setEntity(new StringEntity(output));

    // HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(post);
    int code = response.getStatusLine().getStatusCode();
    logger.debug("status code:" + code);
    if (code != 200) {
      return null;
    }
    
    BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    String imageFormat = (String) data.get(GlycanClientQuerySpec.IMAGE_FORMAT);
    ImageIO.write(bufimage, imageFormat, bos);
    byte[] imageBytes = bos.toByteArray();
    bos.close();


//    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//    StringBuilder result = new StringBuilder();
//    String line = "";
//
//    while ((line = rd.readLine()) != null) {
//      result.append(line + "\n");
//    }

//    return result.toString().getBytes();
    return imageBytes;
  }

  @Override
  public String getImageBase64(Map<String, Object> data)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    return BASE64HEADER + DatatypeConverter.printBase64Binary(getImage(data));
  }

  private String getParams(Map<String, Object> data) {
    String imageFormat = (String) data.get(GlycanClientQuerySpec.IMAGE_FORMAT);
    String notation = (String) data.get(GlycanClientQuerySpec.IMAGE_NOTATION);
    String style = (String) data.get(GlycanClientQuerySpec.IMAGE_STYLE);
    return "format=" + imageFormat + "&notation=" + notation + "&style=" + style;
  }

  /**
   * 
   * accession number to retrieve a registered glycan image.
   * 
   * @param hostname
   * @param accessionNumber
   * @return
   * @throws IOException
   */
  public byte[] getRegisteredImage(Map<String, Object> data) throws IOException {
    String url = env.get(GlycanClientQuerySpec.HOSTNAME) + (String) env.get(GlycanClientRegisterSpec.CONTEXT_PATH) + "/"
        + data.get(GlycanClientQuerySpec.ID) + "/image?" + getParams(data);
    logger.debug("request:>" + url);

    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    HttpResponse response = client.execute(request);
    int code = response.getStatusLine().getStatusCode();
    logger.debug("status code:" + code);
    if (code != 200) {
      return null;
    }

    BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    String imageFormat = (String) data.get(GlycanClientQuerySpec.IMAGE_FORMAT);
    ImageIO.write(bufimage, imageFormat, bos);
    byte[] imageBytes = bos.toByteArray();
    bos.close();

//    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//    StringBuilder result = new StringBuilder();
//    String line = "";
//
//    while ((line = rd.readLine()) != null) {
//      result.append(line);
//    }

    return imageBytes;
  }
}