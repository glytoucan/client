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
import org.glycoinfo.vision.util.Encoding;
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
    gmap.put(GlycanClientRegisterSpec.MESSAGE, responseEntity.getBody());
    return gmap;
  }

  @Override
  public String getImage(String hostname, String sequence)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("structure", sequence);
    data.put("encoding", "glycoct");
    return getImage(hostname, sequence, data);
  }

  @Override
  public String getImage(String hostname, String sequence, Map<String, Object> data)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    // String url = hostname +
    // "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
    String url = hostname + "/glycans/image/glycan?format=png&notation=cfg&style=extended";

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

    ObjectMapper mapper = new ObjectMapper();
    // String output = mapper.writeValueAsString(data);
    String output = "{\"format\":\"" + data.get("encoding") + "\",\"sequence\":\"" + sequence + "\"}";
    // logger.debug("output:"+output);
    logger.debug("output:" + output);

    post.setEntity(new StringEntity(output));

    // HttpClient client = HttpClientBuilder.create().build();

    HttpResponse response = client.execute(post);
    int code = response.getStatusLine().getStatusCode();
    logger.debug("status code:" + code);
    if (code != 200) {
      return null;
    }

    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    // StringBuilder result = new StringBuilder();
    // String line = "";
    // while ((line = rd.readLine()) != null) {
    // logger.debug("line:>" + line);
    // result.append(line);
    // }
    BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());

    String image = Encoding.encodeToString(bufimage, "png");

    return "data:image/png;base64," + image;
  }

  /**
   * 
   * API hostname and accession number to retrieve a registered glycan image.
   * 
   * @param hostname
   * @param accessionNumber
   * @return
   * @throws IOException
   */
  @Override
  public String getRegisteredImage(String hostname, String accessionNumber, String imageFormat, String notation,
      String style) throws IOException {
    String url = hostname + "/" + accessionNumber + "/glycans/image/glycan?format=" + imageFormat + "&notation="
        + notation + "&style=" + style;

    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    HttpResponse response = client.execute(request);
    int code = response.getStatusLine().getStatusCode();
    logger.debug("status code:" + code);
    if (code != 200) {
      return null;
    }
    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    StringBuilder result = new StringBuilder("data:image/png;base64,");
    String line = "";
    while ((line = rd.readLine()) != null) {
      result.append(DatatypeConverter.printBase64Binary(line.getBytes()) + "\n");
    }
    return result.toString();
  }
}