package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.model.RegisterContributorResponse;
import org.glytoucan.model.GlycanRequest;
import org.glytoucan.model.Message;
import org.glytoucan.model.RegisterContributorRequest;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

public class ContributorRest extends AuthenticatedApi {

  public static String NAME="name";
  public static String EMAIL="email";
  public static String ID="id";

  private static final Log logger = LogFactory.getLog(ContributorRest.class);

  RestTemplate restTemplate = new RestTemplate();
  
  @Autowired
  GlycanConfig glycanConfig;

  private static String context = "/contributor";
  

  public Map<String, Object> register(Map<String, Object> gmap) {
    if (gmap.containsKey(USERNAME))
      glycanConfig.setUsername((String) gmap.get(USERNAME));
    if (gmap.containsKey(API_KEY))
      glycanConfig.setApiKey((String) gmap.get(API_KEY));
    
    RegisterContributorRequest req = new RegisterContributorRequest();
    req.setName((String) gmap.get(NAME));
    req.setEmail((String) gmap.get(EMAIL));

    String cmd = context + "/register";
    @SuppressWarnings({ "unchecked", "rawtypes" }) 
    HttpEntity<?> requestEntity = new HttpEntity(req, getHeaders());
    final ResponseEntity<RegisterContributorResponse> responseEntity = submit(requestEntity, cmd);
    RegisterContributorResponse response = responseEntity.getBody();
    gmap.put(NAME, response.getName());
    gmap.put(ID, response.getContributorId());
    gmap.put(MESSAGE, response);
    return gmap;
  }

  private ResponseEntity<RegisterContributorResponse> submit(HttpEntity<?> requestEntity, String cmd) {
    restTemplate.setErrorHandler(new ErrorHandler());
    logger.debug("request:>"+ glycanConfig.getHostname() + cmd);
    return restTemplate.exchange(glycanConfig.getHostname() + cmd, HttpMethod.POST, requestEntity, RegisterContributorResponse.class);
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    String auth = glycanConfig.getUsername() + ":" + glycanConfig.getApiKey();
    byte[] encodedAuthorisation = Base64Utils.encode(auth.getBytes());
    headers.add(GlycanClientRegisterSpec.AUTHORIZATION_HEADER, GlycanClientRegisterSpec.AUTH_BASIC_HEADER + new String(encodedAuthorisation));

    return headers;
  }
}
