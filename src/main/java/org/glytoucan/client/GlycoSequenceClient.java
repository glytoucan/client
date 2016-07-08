package org.glytoucan.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.soap.GlycoSequenceDetailRequest;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.glytoucan.client.soap.GlycoSequenceSearchResponse;
import org.glytoucan.client.soap.GlycoSequenceTextSearchRequest;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class GlycoSequenceClient extends WebServiceGatewaySupport {
  private static final Log logger = LogFactory.getLog(GlycoSequenceClient.class);

  public GlycoSequenceDetailResponse detailRequest(String accessionNumber) {
    logger.debug("client querying :>" + accessionNumber + "<");
    logger.debug("client accessiong default URI:>" + getDefaultUri() + "<");
    GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
    request.setAccessionNumber(accessionNumber);

    return (GlycoSequenceDetailResponse) getWebServiceTemplate().marshalSendAndReceive(request);
  }

  
  public GlycoSequenceSearchResponse textSearchRequest(String sequence) {
    logger.debug("client querying :>" + sequence + "<");
    logger.debug("client accessiong default URI:>" + getDefaultUri() + "<");
    GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
    request.setSequence(sequence);

    return (GlycoSequenceSearchResponse) getWebServiceTemplate().marshalSendAndReceive(request);
  }
  
}
