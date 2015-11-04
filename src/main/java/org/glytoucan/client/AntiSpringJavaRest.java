package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanSpec;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AntiSpringJavaRest {

	public String register(String sequence) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);

		String result = submit(map);

		return result;
	}
	
	public String register(String sequence, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		map.put(GlycanSpec.PUBLIC_DATABASE_STRUCTURE_ID, id);

		String result = submit(map);

		return result;
	}

	private String submit(Map<String, Object> map) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

		GlycanSpec glycanSpec = ctx.getBean(GlycanRest.class);
		Map<String, Object> results = glycanSpec.registerStructure(map);

		Message messageResult = (Message) results.get(GlycanSpec.MESSAGE);
		return messageResult.getMessage();
	}
}