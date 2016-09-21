package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.config.ContributorConfig;
import org.glytoucan.client.model.RegisterContributorResponse;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@SpringApplicationConfiguration(classes = {Application.class, ContributorConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
public class ContributorTest {
	
	private static final Log logger = LogFactory
			.getLog(ContributorTest.class);
	
	@Autowired
	ContributorRest contributorRest;

	// these work normally but not with dummy data in test properties.
	@Test
	public void testOnlyRegistration() {
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(ContributorRest.NAME, "Administrator");
    map.put(ContributorRest.USERNAME, "1");
    map.put(ContributorRest.API_KEY, "JDUkMjAxNjA5MDUwOTM5MjMkVWZzaHNyRVFkMVl4Umx0MjJiczVyZFZVNDQ5bUJBVTBoQTdaeGpiUkRpMw==");
		Map<String, Object>  results = contributorRest.register(map);

		RegisterContributorResponse result = (RegisterContributorResponse) results.get(ContributorRest.MESSAGE);
    String id = (String)results.get(ContributorRest.ID);
    String name = (String)results.get(ContributorRest.NAME);
    
    Assert.assertNotNull(result);
    Assert.assertNotNull(id);
    Assert.assertEquals("Administrator", name);

		logger.debug(results);
	}

}
