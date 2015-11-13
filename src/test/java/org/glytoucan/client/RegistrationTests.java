package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanSpec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@SpringApplicationConfiguration(classes = {Application.class, GlycanConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationTests {
	
	private static final Log logger = LogFactory
			.getLog(RegistrationTests.class);
	
	@Autowired
	GlycanSpec glycanRest;

	// these work normally but not with dummy data in test properties.
	@Test(expected=HttpClientErrorException.class)
	public void testOnlyRegistration() {
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, "RES\n"
				+ "1b:b-dglc-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(4+1)3d");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
	}
	
	@Test(expected=HttpClientErrorException.class)
//	@Test
	public void testG00052MO() {
String sequence = "RES\n"
		+ "1b:b-dglc-HEX-1:5\n"
		+ "2s:n-acetyl\n"
		+ "3b:a-lgal-HEX-1:5|6:d\n"
		+ "4b:b-dgal-HEX-1:5\n"
		+ "5b:a-lgal-HEX-1:5|6:d\n"
		+ "LIN\n"
		+ "1:1d(2+1)2n\n"
		+ "2:1o(3+1)3d\n"
		+ "3:1o(4+1)4d\n"
		+ "4:4o(2+1)5d";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanSpec.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00052MO");
	}
	
	@Test(expected=HttpClientErrorException.class)
//	@Test
	public void testRegistrationWithId() {
String sequence = "RES\n"
		+ "1b:b-dglc-HEX-1:5\n"
		+ "2s:n-acetyl\n"
		+ "3b:b-dgal-HEX-1:5\n"
		+ "4b:a-lgal-HEX-1:5|6:d\n"
		+ "5b:a-lgal-HEX-1:5|6:d\n"
		+ "LIN\n"
		+ "1:1d(2+1)2n\n"
		+ "2:1o(3+1)3d\n"
		+ "3:3o(2+1)4d\n"
		+ "4:1o(4+1)5d\n";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		map.put(GlycanSpec.PUBLIC_DATABASE_STRUCTURE_ID, "323");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanSpec.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00048MO");
	}
	
	@Test(expected=HttpClientErrorException.class)
//	@Test
	public void testNewwithCRLF() {
        String sequence = "RES\r\n"
        		+ "1b:x-llyx-PEN-1:5\r\n"
        		+ "2b:x-dgal-HEX-1:5\r\n"
        		+ "3b:x-dglc-HEX-1:5\r\n"
        		+ "4b:x-dglc-HEX-1:5\r\n"
        		+ "LIN\r\n"
        		+ "1:1o(-1+1)2d\r\n"
        		+ "2:2o(-1+1)3d\r\n"
        		+ "3:3o(-1+1)4d";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		map.put(GlycanSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanSpec.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00052MO");
	}
	

//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testInvalidGlycoct() {
        String sequence = "RES\n"
        		+ "1b:o-dgal-HEX-0:0|1:aldi|1:d\n"
        		+ "2b:x-dglc-HEX-1:5\n"
        		+ "3b:x-dgal-HEX-1:5\n"
        		+ "4s:n-acetyl\n"
        		+ "LIN\n"
        		+ "1:1o(-1+1)2d\n"
        		+ "2:2o(-1+1)3d\n"
        		+ "3:2d(2+1)4n";
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		map.put(GlycanSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanSpec.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00052MO");
	}
	
	
}
