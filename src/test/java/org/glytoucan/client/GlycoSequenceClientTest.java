package org.glytoucan.client;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringApplicationConfiguration(classes = { Application.class, GlycanTestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class GlycoSequenceClientTest {

  private static final Log logger = LogFactory.getLog(GlycoSequenceClientTest.class);
  
  @Autowired
  GlycoSequenceClient glycoSequenceClient;

  @Test
  public void testDetailRequest() throws Exception {
    GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest("G00030MO");
    Assert.assertNotNull(response);
    
    logger.debug(response);
    logger.debug(response.getDescription());
    Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
    Assert.assertEquals("G00030MO", response.getAccessionNumber());
    Assert.assertTrue(response.getDescription().contains("[beta-L-GlcpNAc-(1->2/4)-alpha-L-Manp-(1->6)-beta-L-GlcpNAc-(1->2/4)-alpha-L-Manp-(1->3)-beta-L-Manp-(1->4)]-beta-L-GlcpNAc-(1->4)-L-GlcNAc(?->"));
  }
}
