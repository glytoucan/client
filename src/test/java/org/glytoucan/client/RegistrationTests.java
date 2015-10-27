package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
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
}
