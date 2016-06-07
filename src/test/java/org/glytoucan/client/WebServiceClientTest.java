package org.glytoucan.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.glycoinfo.vision.util.Encoding;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WebServiceClientTest.class})
public class WebServiceClientTest {
	private static final String hostname = "http://test.api.glytoucan.org";
  public static Logger logger = (Logger) LoggerFactory
			.getLogger(WebServiceClientTest.class);

  
  @Autowired
  GlycanClientQuerySpec gsClient;
  
@Test
	public void image() throws Exception {
		String image = gsClient.getImage(hostname, "RES\\n"
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
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTest() throws Exception {
		String image = gsClient.getImage(hostname, "RES\\n"
				+ "1b:x-dgal-HEX-1:5\\n"
				+ "2b:x-dman-HEX-1:5\\n"
				+ "3s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n"
				+ "2:1d(2+1)3n");
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTestmanglc() throws Exception {
		String image = gsClient.getImage(hostname, "RES\\n"
				+ "1b:x-dman-HEX-1:5\\n"
				+ "2b:x-dglc-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n");
		Assert.assertNotNull(image);
		logger.debug(image);
	}
	
	 @Test
	  public void imageTestG00026MO() throws Exception {
	    String image = gsClient.getImage(hostname, "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
	    Assert.assertNotNull(image);
	    logger.debug(image);
	  }
	
//	@Test
	public void testRead() throws IOException {
	    /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("/home/aoki/workspace/rdf.glytoucan/src/test/resources/image.png"));
        BufferedImage newImg;
        String imgstr;
        GlycanRest gsClient = new GlycanRest();
        imgstr = Encoding.encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = Encoding.decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("/tmp/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
	}
	
//String url = hostname + "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
	@Test
	public void testRegistered() throws IOException {
    String image = gsClient.getRegisteredImage(hostname, "G00055MO", "png", "cfg", "extended");
    Assert.assertNotNull(image);
    logger.debug(image);
	}
	
}