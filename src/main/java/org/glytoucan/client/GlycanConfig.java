package org.glytoucan.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlycanConfig {
	
	@Bean
	public GlycanSpec glycanSpec() {
		return new GlycanRest();
	}
}
