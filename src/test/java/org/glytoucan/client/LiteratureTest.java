package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.config.ContributorConfig;
import org.glytoucan.client.config.LiteratureConfig;
import org.glytoucan.client.model.RegisterContributorResponse;
import org.glytoucan.client.model.RegisterLiteratureRequestResponse;
import org.glytoucan.client.model.ResponseMessage;
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

@SpringApplicationConfiguration(classes = { Application.class, LiteratureConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class LiteratureTest {

	private static final Log logger = LogFactory.getLog(LiteratureTest.class);

	@Autowired
	LiteratureRest rest;

	// these work normally but not with dummy data in test properties.
	@Test
	public void testOnlyRegistration() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LiteratureRest.USERNAME, "815e7cbca52763e5c3fbb5a4dccc176479a50e2367f920843c4c35dca112e33d");
		map.put(LiteratureRest.API_KEY, "b83f8b8040a584579ab9bf784ef6275fe47b5694b1adeb82e076289bf17c2632");
		map.put(LiteratureRest.ACCESSION_NUMBER, "G00029MO");
		map.put(LiteratureRest.PUBLICATION_ID, "9565568");
		map.put(LiteratureRest.REMOVE_FLAG, false);
		
		Map<String, Object> results = rest.register(map);

		ResponseMessage result = (ResponseMessage) results
				.get(LiteratureRest.MESSAGE);
		String accNum = (String) results.get(LiteratureRest.ACCESSION_NUMBER);
		String id = (String) results.get(LiteratureRest.PUBLICATION_ID);

		Assert.assertNotNull(result);
		Assert.assertNotNull(id);
		Assert.assertNotNull(accNum);
		Assert.assertEquals("G00029MO", accNum);
		Assert.assertEquals("9565568", id);

		logger.debug(results);
	}
}