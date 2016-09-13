package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	private static final Log logger = LogFactory.getLog(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	GlycanClientRegisterSpec glycanRest;

	@Bean
	CommandLineRunner detailRequest(GlycoSequenceClient glycoSequenceClient) {
		return args -> {
			if (args.length > 0) {
				String sequence = args[0];
				Map<String, Object>  map = new HashMap<String, Object>();
				map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
				
				Map<String, Object>  results = glycanRest.registerStructure(map);
				
				logger.debug(results);
				Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
				logger.debug(msg);
			}
		};
	}
}
