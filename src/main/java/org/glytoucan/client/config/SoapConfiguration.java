package org.glytoucan.client.config;

import org.glytoucan.client.GlycoSequenceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapConfiguration {
  
  @Value("${soap.api.glycosequence.uri:http://test.soap.api.glytoucan.org/ws}")
  String defaultUri;
  
  @Bean
  String defaultUri() {
    return defaultUri;
  }
  
	@Bean
	public Jaxb2Marshaller marshaller() throws Exception {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("org.glytoucan.client.soap");
//		marshaller.setPackagesToScan(ClassUtils.getPackageName(LogInsertRequest.class));
//		marshaller.afterPropertiesSet();
		return marshaller;
	}

	@Bean
	public GlycoSequenceClient glycoSequenceClient(Jaxb2Marshaller marshaller) {
		GlycoSequenceClient client = new GlycoSequenceClient();
		client.setDefaultUri(defaultUri);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}