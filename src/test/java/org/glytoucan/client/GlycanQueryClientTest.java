package org.glytoucan.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.glycoinfo.vision.util.Encoding;
import org.glytoucan.client.config.GlycanQueryConfig;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {GlycanQueryClientTest.class, GlycanQueryConfig.class})
@EnableAutoConfiguration
public class GlycanQueryClientTest {
  public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycanQueryClientTest.class);

  
  @Autowired
  GlycanClientQuerySpec gsClient;
  
@Test
	public void image() throws Exception {
  HashMap<String, Object> data = new HashMap<String, Object>();
  data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
  data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
  data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
  data.put(GlycanClientQuerySpec.SEQUENCE, "RES\\n"
        + "1b:x-dglc-HEX-x:x\\n"
        + "2b:b-dgal-HEX-1:5\\n"
        + "3b:a-dgal-HEX-1:5\\n"
        + "4b:b-dgal-HEX-1:5\\n"
        + "5s:n-acetyl\\n"
        + "LIN\\n"
        + "1:1o(4+1)2d\\n"
        + "2:2o(3+1)3d\\n"
        + "3:3o(3+1)4d\\n"
        + "4:4d(2+1)5n");

		String image = gsClient.getImageBase64(data);
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTest() throws Exception {
	  HashMap<String, Object> data = new HashMap<String, Object>();
	  data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
	  data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
	  data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
	  data.put(GlycanClientQuerySpec.SEQUENCE, "RES\\n"
        + "1b:x-dgal-HEX-1:5\\n"
        + "2b:x-dman-HEX-1:5\\n"
        + "3s:n-acetyl\\n"
        + "LIN\\n"
        + "1:1o(-1+1)2d\\n"
        + "2:1d(2+1)3n");
		String image = gsClient.getImageBase64(data);
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTestmanglc() throws Exception {
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
    data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
    data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
    data.put(GlycanClientQuerySpec.SEQUENCE, "RES\\n"
        + "1b:x-dman-HEX-1:5\\n"
        + "2b:x-dglc-HEX-1:5\\n"
        + "LIN\\n"
        + "1:1o(-1+1)2d\\n");
		String image = gsClient.getImageBase64(data);
		Assert.assertNotNull(image);
		logger.debug(image);
	}
	
	 @Test
	  public void imageTestG00026MO() throws Exception {
	    HashMap<String, Object> data = new HashMap<String, Object>();
	    data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
	    data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
	    data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
	    data.put(GlycanClientQuerySpec.SEQUENCE, "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
	    String image = gsClient.getImageBase64(data);

	    Assert.assertNotNull(image);
	    logger.debug(image);
	  }

	 
	     @Test
	     public void imageTestLinear() throws Exception {
	       HashMap<String, Object> data = new HashMap<String, Object>();
	       data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
	       data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
	       data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
	       data.put(GlycanClientQuerySpec.SEQUENCE, "WURCS=2.0/4,13,12/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a1221m-1x_1-5][a2112h-1x_1-5]/1-1-2-2-1-3-4-1-4-2-1-4-1/a?-b1_b?-c1_c?-d1_c?-j1_d?-e1_d?-h1_e?-f1_e?-g1_h?-i1_j?-k1_j?-m1_k?-l1");
	       String image = gsClient.getImageBase64(data);

	       Assert.assertNotNull(image);
	       logger.debug(image);
	     }
	     
       @Test
       public void imageTestAmbiguous() throws Exception {
         HashMap<String, Object> data = new HashMap<String, Object>();
         data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
         data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
         data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
         data.put(GlycanClientQuerySpec.SEQUENCE, "WURCS=2.0/4,11,10/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5]/1-1-2-2-1-3-2-1-4-1-3/a?-b1_a?-i1_b?-c1_c?-d1_c?-g1_d?-e1_e?-f1_g?-h1_j?-k1_d?-g?-j1");
         String image = gsClient.getImageBase64(data);

         Assert.assertNotNull(image);
         logger.debug(image);
       }
	     
	         
	         
//	@Test
	public void testRead() throws IOException {
	    /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("/home/aoki/workspace/rdf.glytoucan/src/test/resources/image.png"));
        BufferedImage newImg;
        String imgstr;
        GlycanRegisterRest gsClient = new GlycanRegisterRest();
        imgstr = Encoding.encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = Encoding.decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("/tmp/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
	}
	
//String url = hostname + "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
	@Test
	public void testRegistered() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
    data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
    data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
    data.put(GlycanClientQuerySpec.ID, "G00055MO");
    String image = gsClient.getImageBase64(data);
    Assert.assertNotNull(image);
    logger.debug(image);
	}
	
}