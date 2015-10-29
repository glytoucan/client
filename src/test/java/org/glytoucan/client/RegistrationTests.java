package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringApplicationConfiguration(classes = {Application.class, GlycanConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationTests {
	
	private static final Log logger = LogFactory
			.getLog(RegistrationTests.class);
	
	@Autowired
	GlycanSpec glycanRest;
		
	@Test
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
	
	@Test
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
}
