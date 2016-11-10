package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AntiSpringJavaRest {

  public String removeId(String sequence, String id, String contributorId, String apiKey) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
    map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, id);
    map.put(GlycanRegisterRest.USERNAME, contributorId);
    map.put(GlycanRegisterRest.API_KEY, apiKey);
    map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REMOVE_PARTNER_ACCESSION_CMD);

    String result = submit(map);

    return result;
  }

  public String register(String sequence, String contributorId, String apiKey) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
    map.put(GlycanRegisterRest.USERNAME, contributorId);
    map.put(GlycanRegisterRest.API_KEY, apiKey);
    map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REGISTER_CMD);

    String result = submit(map);

    return result;
  }
  
  public String register(String sequence, String id, String contributorId, String apiKey)  {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
    map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, id);
    map.put(GlycanRegisterRest.USERNAME, contributorId);
    map.put(GlycanRegisterRest.API_KEY, apiKey);
    map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REGISTER_CMD);
    String result = submit(map);

    return result;
  }
  
  public String register(String sequence) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);

		String result = submit(map);

		return result;
	}
	
	public String register(String sequence, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
    map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REGISTER_CMD);
		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, id);

		String result = submit(map);

		return result;
	}

	private String submit(Map<String, Object> map) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

		GlycanClientRegisterSpec glycanSpec = ctx.getBean(GlycanRegisterRest.class);
		Map<String, Object> results = glycanSpec.registerStructure(map);

		Message messageResult = (Message) results.get(GlycanRegisterRest.MESSAGE);
		return messageResult.getMessage();
	}
}