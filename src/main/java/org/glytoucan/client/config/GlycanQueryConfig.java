package org.glytoucan.client.config;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.GlycanQueryRest;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlycanQueryConfig {

	private static final Log logger = LogFactory.getLog(GlycanQueryConfig.class);

	@Value("${api.hostname:http://test.api.glytoucan.org}")
	private String hostname;

	@Value("${api.glycan.context:/glycans}")
	private String context;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Bean
	public GlycanQuerySpec glycanQuerySpec() {
		HashMap<String, Object> env = new HashMap<String, Object>();

		env.put(GlycanSpec.HOSTNAME, hostname);
		logger.debug("hostname is:>" + hostname + "<");
		env.put(GlycanSpec.CONTEXT_PATH, context);
		logger.debug("context is:>" + context + "<");
		// env.put(GlycanSpec.USERNAME, username);
		// logger.debug("username is:>" + username + "<");
		// env.put(GlycanSpec.API_KEY, getApiKey());
		// logger.debug("apikey is:>" + getApiKey() + "<");

		GlycanQueryRest gr = new GlycanQueryRest();
		gr.setEnv(env);

		return gr;
	}

}