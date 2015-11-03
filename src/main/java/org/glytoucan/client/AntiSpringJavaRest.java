package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.glytoucan.model.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class AntiSpringJavaRest {

	// private static final Log logger =
	// LogFactory.getLog(AntiSpringJavaRest.class);

	public String register(String sequence) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlycanSpec.SEQUENCE, sequence);
		// map.put(GlycanSpec.SEQUENCE, "RES\n" + "1b:b-dglc-HEX-1:5\n" +
		// "2s:n-acetyl\n" + "3b:b-dgal-HEX-1:5\n" + "LIN\n"
		// + "1:1d(2+1)2n\n" + "2:1o(4+1)3d");

		ApplicationContext ctx = SpringApplication.run(Application.class);
//		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

		GlycanSpec glycanSpec = ctx.getBean(GlycanRest.class);
		Map<String, Object> results = glycanSpec.registerStructure(map);
		// logger.debug(results);

		Message messageResult = (Message) results.get(GlycanSpec.MESSAGE);
		String result = messageResult.getMessage();

		return result;
	}
}
