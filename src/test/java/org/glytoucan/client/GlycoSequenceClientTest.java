package org.glytoucan.client;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.glytoucan.client.soap.GlycoSequenceSearchResponse;
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
    Assert.assertTrue(response.getDescription().contains("GlcNAc(b1-2/4)Man(a1-3)[GlcNAc(b1-2/4)Man(a1-6)]Man(b1-4)GlcNAc(b1-4)GlcNAc"));
    Assert.assertTrue(response.getDescription().contains("WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4"));
  }
  
  @Test
  public void testDetailRequestG97036DW() throws Exception {
    
    GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest("G97036DW");
    Assert.assertNotNull(response);
    
    logger.debug(response);
    logger.debug(response.getDescription());
    Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
    Assert.assertEquals("G97036DW", response.getAccessionNumber());
    Assert.assertTrue(response.getDescription().contains("org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException"));
  }
  
  @Test
  public void testTextSearchWurcs() throws Exception {
    
    GlycoSequenceSearchResponse response = glycoSequenceClient.textSearchRequest("WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4");
    Assert.assertNotNull(response);
    
    logger.debug(response);
    logger.debug(response.getAccessionNumber());
    Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
    Assert.assertEquals("G00030MO", response.getAccessionNumber());
  }

  
  @Test
  public void testTextSearchIupac() throws Exception {
    
    GlycoSequenceSearchResponse response = glycoSequenceClient.textSearchRequest("Glc");
    Assert.assertNotNull(response);
    
    logger.debug(response);
    logger.debug(response.getAccessionNumber());
    Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
    Assert.assertEquals("G15021LG", response.getAccessionNumber());
  }
  
  @Test
  public void testGInvalid() throws Exception {
    
    GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest("GTESTING");
    Assert.assertNotNull(response);
    
    logger.debug(response);
    logger.debug(response.getDescription());
    Assert.assertEquals(new BigInteger("-100"),response.getResponseMessage().getErrorCode());
    Assert.assertEquals("GTESTING", response.getAccessionNumber());
  }
}
