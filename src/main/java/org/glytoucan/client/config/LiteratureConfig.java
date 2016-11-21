package org.glytoucan.client.config;

import org.glytoucan.client.LiteratureRest;
import org.glytoucan.client.GlycanConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlycanConfig.class)
public class LiteratureConfig {
  
  @Bean
  LiteratureRest LiteratureRest() {
    return new LiteratureRest();
  }
}
