package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@SpringApplicationConfiguration(classes = {Application.class, GlycanTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTests {
	
	private static final Log logger = LogFactory
			.getLog(SimpleTests.class);
	
	@Autowired
	GlycanSpec glycanRest;
	
	@Autowired
	GlycanQuerySpec glycanQueryRest;

	// GlycanSpec glycanRest = Spring.getBeanOfType(GlycanSpec.class);
	@Test(expected=HttpClientErrorException.class)
	public void testSimpleProtected() {
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanSpec.USERNAME, "aoki");
		map.put(GlycanSpec.API_KEY, "a");
		map.put(GlycanSpec.HOSTNAME, "http://test.api.glytoucan.org");
		map.put(GlycanSpec.CONTEXT_PATH, "/test");
		
		Map<String, Object> results = glycanRest.registerStructure(map);
		
		logger.debug(results);
	}

	// GlycanSpec glycanRest = Spring.getBeanOfType(GlycanSpec.class);
	@Test
	public void testSimpleNotProtected() {
		Map<String, Object>  map = new HashMap<String, Object>();
//		map.put(GlycanSpec.USERNAME, "aoki");
//		map.put(GlycanSpec.API_KEY, "a");
		
		Map<String, Object> results = glycanQueryRest.getListStructures(map);
		GlycanList list = (GlycanList)results.get(GlycanSpec.MESSAGE);
		
		logger.debug(list);
	}
}

