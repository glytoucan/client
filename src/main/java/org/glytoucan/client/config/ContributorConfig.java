package org.glytoucan.client.config;

import org.glytoucan.client.ContributorRest;
import org.glytoucan.client.GlycanConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlycanConfig.class)
public class ContributorConfig {
  
  @Bean
  ContributorRest contributorRest() {
    return new ContributorRest();
  }

}
