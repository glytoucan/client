package org.glytoucan.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.model.RegisterContributorResponse;
import org.glytoucan.client.model.RegisterLiteratureRequestResponse;
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

public class LiteratureRest extends AuthenticatedApi {

  public static String ACCESSION_NUMBER="accession_number";
  public static String PUBLICATION_ID="publication_id";
  public static String CONTRIBUTOR_ID="contributor_id";
  public static String TITLE="publicat_title";
  public static String REMOVE_FLAG="remove_flag";

  private static final Log logger = LogFactory.getLog(LiteratureRest.class);

  RestTemplate restTemplate = new RestTemplate();
  
  @Autowired
  GlycanConfig glycanConfig;

  private static String context = "/literature";
  

  public Map<String, Object> register(Map<String, Object> gmap) {
    if (gmap.containsKey(USERNAME))
      glycanConfig.setUsername((String) gmap.get(USERNAME));
    if (gmap.containsKey(API_KEY))
      glycanConfig.setApiKey((String) gmap.get(API_KEY));
    
    RegisterLiteratureRequestResponse reqres = new RegisterLiteratureRequestResponse();
    reqres.setAccessionNumber((String) gmap.get(ACCESSION_NUMBER));
    reqres.setPublicationId((String) gmap.get(PUBLICATION_ID));
    reqres.setContributorId((String) gmap.get(CONTRIBUTOR_ID));
    reqres.setRemoveFlag(gmap.get(REMOVE_FLAG) != null? (Boolean)gmap.get(REMOVE_FLAG): false);

    String cmd = context + "/register";
    @SuppressWarnings({ "unchecked", "rawtypes" }) 
    HttpEntity<?> requestEntity = new HttpEntity(reqres, getHeaders());
    final ResponseEntity<RegisterLiteratureRequestResponse> responseEntity = submit(requestEntity, cmd);
    reqres = responseEntity.getBody();
    gmap.put(MESSAGE, reqres.getResponseMessage());
    return gmap;
  }

  private ResponseEntity<RegisterLiteratureRequestResponse> submit(HttpEntity<?> requestEntity, String cmd) {
    restTemplate.setErrorHandler(new ErrorHandler());
    logger.debug("request:>"+ glycanConfig.getHostname() + cmd);
    return restTemplate.exchange(glycanConfig.getHostname() + cmd, HttpMethod.POST, requestEntity, RegisterLiteratureRequestResponse.class);
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
