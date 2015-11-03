package org.glytoucan.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	private static final Log logger = LogFactory.getLog(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(
				Application.class, args);
		
		AntiSpringJavaRest glyRest = new AntiSpringJavaRest();
		String result = glyRest.register("RES\n" + "1b:b-dglc-HEX-1:5\n" + "2s:n-acetyl\n" + "3b:b-dgal-HEX-1:5\n" + "LIN\n"
				+ "1:1d(2+1)2n\n" + "2:1o(4+1)3d");
		logger.debug("result:>" + result);
	}
}
