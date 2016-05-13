package org.glytoucan.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  private static final Log logger = LogFactory.getLog(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner detailRequest(GlycoSequenceClient glycoSequenceClient) {
    return args -> {
      if (args.length > 0) {
        GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest(args[0]);
        logger.debug(response);
      }
    };
  }
}
