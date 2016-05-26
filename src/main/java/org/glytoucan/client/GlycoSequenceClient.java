package org.glytoucan.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.soap.GlycoSequenceDetailRequest;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class GlycoSequenceClient extends WebServiceGatewaySupport {
  private static final Log logger = LogFactory.getLog(GlycoSequenceClient.class);

  public GlycoSequenceDetailResponse detailRequest(String accessionNumber) {
    logger.debug("client querying :>" + accessionNumber + "<");
    logger.debug("client accessiong default URI:>" + getDefaultUri() + "<");
    GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
    request.setAccessionNumber(accessionNumber);

    
    // logger.debug(response);
    // logger.debug(response.getDescription());
    // Assert.assertEquals(new
    // BigInteger("0"),response.getResponseMessage().getErrorCode());
    // Assert.assertEquals("G00055MO", response.getAccessionNumber());
    // Assert.assertTrue(response.getDescription().contains("Galb1-4GlcNAcb1-R"));

    return (GlycoSequenceDetailResponse) getWebServiceTemplate().marshalSendAndReceive(request);
  }
}
