package org.glytoucan.client;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlycanTestConfig {

	private static final Log logger = LogFactory.getLog(GlycanConfig.class);
	
    @Value("${api.hostname:http://test.api.glytoucan.org}")
    private String hostname;

    @Value("${api.glycan.context:/glycans}")
    private String context;

    @Value("${api.contributor.id}")
    private String username;

    @Value("${api.key}")
    private String hash;
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApiKey() {
		return hash;
	}

	public void setApiKey(String hash) {
		this.hash = hash;
	}

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
	public GlycanSpec glycanSpec() {
		HashMap<String, Object> env = new HashMap<String, Object>();

		env.put(GlycanSpec.HOSTNAME, hostname);
		logger.debug("hostname is:>" + hostname + "<");
		env.put(GlycanSpec.CONTEXT_PATH, context);
		logger.debug("context is:>" + context + "<");
		env.put(GlycanSpec.USERNAME, username);
		logger.debug("username is:>" + username + "<");
		env.put(GlycanSpec.API_KEY, getApiKey());
		logger.debug("apikey is:>" + getApiKey() + "<");
		
		GlycanRest gr = new GlycanRest();
		gr.setEnv(env);

		return gr;
	}
	
	@Bean
	public GlycanQuerySpec glycanQuerySpec() {
		HashMap<String, Object> env = new HashMap<String, Object>();

		env.put(GlycanSpec.HOSTNAME, hostname);
		logger.debug("hostname is:>" + hostname + "<");
		env.put(GlycanSpec.CONTEXT_PATH, "/glycans");
		logger.debug("context is:>" + context + "<");
		
		GlycanQueryRest gr = new GlycanQueryRest();
		gr.setEnv(env);

		return gr;
	}
}