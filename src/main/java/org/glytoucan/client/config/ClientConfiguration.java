package org.glytoucan.client.config;

import org.glytoucan.client.GlycoSequenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import(ServerConfiguration.class)
public class ClientConfiguration {

  @Autowired
  String defaultUri;

  @Autowired
  Jaxb2Marshaller marshaller;

  @Bean(name = "glycoSequenceClient")
  public GlycoSequenceClient glycoSequenceClient() {
    GlycoSequenceClient client = new GlycoSequenceClient();
    client.setDefaultUri(defaultUri);
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}