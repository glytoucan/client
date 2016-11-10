package org.glytoucan.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.model.Message;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GlycanRegisterRestTest {
	
  private static String myid="14e1d868cf50557143032041eef95cc7271b8c3a0bdc5a52fb849cdf29ef4aff";
  private static String mykey="9746901e895ae933d0f15797c377c119b3d2c8f22a85f99a24574655dbdc7739";
  private static String adminid="815e7cbca52763e5c3fbb5a4dccc176479a50e2367f920843c4c35dca112e33d";
  private static String adminkey="b83f8b8040a584579ab9bf784ef6275fe47b5694b1adeb82e076289bf17c2632";
  
  
	private static final Log logger = LogFactory
			.getLog(GlycanRegisterRestTest.class);
	
	@Autowired
	GlycanClientRegisterSpec glycanRest;

	// these work normally but not with dummy data in test properties.
//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testOnlyRegistration() {
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, "RES\n"
				+ "1b:b-dglc-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(4+1)3d");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
	}
	
//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testG00052MO() {
String sequence = "RES\n"
		+ "1b:b-dglc-HEX-1:5\n"
		+ "2s:n-acetyl\n"
		+ "3b:a-lgal-HEX-1:5|6:d\n"
		+ "4b:b-dgal-HEX-1:5\n"
		+ "5b:a-lgal-HEX-1:5|6:d\n"
		+ "LIN\n"
		+ "1:1d(2+1)2n\n"
		+ "2:1o(3+1)3d\n"
		+ "3:1o(4+1)4d\n"
		+ "4:4o(2+1)5d";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00052MO");
	}
	
//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testRegistrationWithId() {
String sequence = "RES\n"
		+ "1b:b-dglc-HEX-1:5\n"
		+ "2s:n-acetyl\n"
		+ "3b:b-dgal-HEX-1:5\n"
		+ "4b:a-lgal-HEX-1:5|6:d\n"
		+ "5b:a-lgal-HEX-1:5|6:d\n"
		+ "LIN\n"
		+ "1:1d(2+1)2n\n"
		+ "2:1o(3+1)3d\n"
		+ "3:3o(2+1)4d\n"
		+ "4:1o(4+1)5d\n";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "123");
    map.put(GlycanRegisterRest.USERNAME, myid);
    map.put(GlycanRegisterRest.API_KEY, mykey);

		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "G00048MO");
	}
	
//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testNewwithCRLFNoId() {
        String sequence = "RES\r\n"
        		+ "1b:x-llyx-PEN-1:5\r\n"
        		+ "2b:x-dgal-HEX-1:5\r\n"
        		+ "3b:x-dglc-HEX-1:5\r\n"
        		+ "4b:x-dglc-HEX-1:5\r\n"
        		+ "LIN\r\n"
        		+ "1:1o(-1+1)2d\r\n"
        		+ "2:2o(-1+1)3d\r\n"
        		+ "3:3o(-1+1)4d";
		/*RES
	1b:b-dglc-HEX-1:5
	2s:n-acetyl
	3b:a-lgal-HEX-1:5|6:d
	4b:b-dgal-HEX-1:5
	5b:a-lgal-HEX-1:5|6:d
	LIN
	1:1d(2+1)2n
	2:1o(3+1)3d
	3:1o(4+1)4d
	4:4o(2+1)5d*/
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
//		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertEquals("G67118QU", msg.getMessage());
	}
	

//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testInvalidGlycoct() {
		String sequence = "RES\n"
        		+ "1b:o-dgal-HEX-0:0|1:aldi|1:d\n"
        		+ "2b:x-dglc-HEX-1:5\n"
        		+ "3b:x-dgal-HEX-1:5\n"
        		+ "4s:n-acetyl\n"
        		+ "LIN\n"
        		+ "1:1o(-1+1)2d\n"
        		+ "2:2o(-1+1)3d\n"
        		+ "3:2d(2+1)4n";
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
    map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REGISTER_CMD);
    map.put(GlycanRegisterRest.USERNAME, myid);
    map.put(GlycanRegisterRest.API_KEY, mykey);

    //		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertEquals(msg.getMessage(), "RES\n1b:o-dgal-HEX-0:0|1:aldi|1:d\n2b:x-dglc-HEX-1:5\n3b:x-dgal-HEX-1:5\n4s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2o(-1+1)3d\n3:2d(2+1)4n not accepted");
		Assert.assertTrue(msg.getError().contains("Error in GlycoCT validation:>Deoxy on C1 impossible"));
	}
	
//	@Test(expected=HttpClientErrorException.class)
//	@Test
	public void testUnicarbDB() {
		
		String sequence = "RES\n"
				+ "1b:o-dglc-HEX-0:0|1:aldi\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dglc-HEX-1:5\n"
				+ "4s:n-acetyl\n"
				+ "5b:b-dman-HEX-1:5\n"
				+ "6b:a-dman-HEX-1:5\n"
				+ "7b:b-dglc-HEX-1:5\n"
				+ "8s:n-acetyl\n"
				+ "9b:b-dgal-HEX-1:5\n"
				+ "10b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n"
				+ "11s:n-acetyl\n"
				+ "12b:a-dman-HEX-1:5\n"
				+ "13b:a-dman-HEX-1:5\n"
				+ "14b:a-dman-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(4+1)3d\n"
				+ "3:3d(2+1)4n\n"
				+ "4:3o(4+1)5d\n"
				+ "5:5o(3+1)6d\n"
				+ "6:6o(2+1)7d\n"
				+ "7:7d(2+1)8n\n"
				+ "8:7o(4+1)9d\n"
				+ "9:9o(6+2)10d\n"
				+ "10:10d(5+1)11n\n"
				+ "11:5o(6+1)12d\n"
				+ "12:12o(3+1)13d\n"
				+ "13:12o(6+1)14d\n";
		
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertTrue(msg.getMessage().contains("not accepted"));
		Assert.assertTrue(msg.getError().contains("Error in GlycoCT validation:>Deoxy on C1 impossible"));
	}
	
//	ya29.CjBSAxcwfeasXDNOq3hqJ5PE5i9JSIrIdvifELRvbA8J2jHu-yAc7w6fMyy0nofCkkU

//	@Test(expected=HttpClientErrorException.class)
	@Test
	public void testGTCUser() {
		
		String sequence = "RES\n"
				+ "1b:b-dglc-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:a-lgal-HEX-1:5|6:d\n"
				+ "4b:b-dgal-HEX-1:5\n"
				+ "5b:a-lgal-HEX-1:5|6:d\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(3+1)3d\n"
				+ "3:1o(4+1)4d\n"
				+ "4:4o(2+1)5d";
		
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
		map.put(GlycanRegisterRest.USERNAME, adminid);
//		map.put(GlycanClientRegisterSpec.API_KEY, "ya29.CjBQAxtMyEE3m1yc46nnDSF_RJ7wzvgXZEgcRnoLMBP6FyXywDPqnyVA5iueZ-91L7s");
		map.put(GlycanRegisterRest.API_KEY, adminkey);
		
//		map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "999");
		
		Map<String, Object>  results = glycanRest.registerStructure(map);
		
		logger.debug(results);
		Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
		Assert.assertEquals("G00052MO", msg.getMessage());
		Assert.assertTrue(StringUtils.isBlank(msg.getError()));
	}
	
	
//@Test(expected=HttpClientErrorException.class)
 @Test
 public void testRegistrationWithIdRemove() {
String sequence = "RES\n"
   + "1b:b-dglc-HEX-1:5\n"
   + "2s:n-acetyl\n"
   + "3b:b-dgal-HEX-1:5\n"
   + "4b:a-lgal-HEX-1:5|6:d\n"
   + "5b:a-lgal-HEX-1:5|6:d\n"
   + "LIN\n"
   + "1:1d(2+1)2n\n"
   + "2:1o(3+1)3d\n"
   + "3:3o(2+1)4d\n"
   + "4:1o(4+1)5d\n";
   /*RES
 1b:b-dglc-HEX-1:5
 2s:n-acetyl
 3b:a-lgal-HEX-1:5|6:d
 4b:b-dgal-HEX-1:5
 5b:a-lgal-HEX-1:5|6:d
 LIN
 1:1d(2+1)2n
 2:1o(3+1)3d
 3:1o(4+1)4d
 4:4o(2+1)5d*/
   Map<String, Object>  map = new HashMap<String, Object>();
   map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
   map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "12345REMOVEME");
   map.put(GlycanRegisterRest.USERNAME, myid);
   map.put(GlycanRegisterRest.API_KEY, mykey);

   Map<String, Object>  results = glycanRest.registerStructure(map);
   
   logger.debug(results);
   Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
   Assert.assertEquals(msg.getMessage(), "G00048MO");
   
   map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REMOVE_PARTNER_ACCESSION_CMD);
   results = glycanRest.registerStructure(map);
 }
 
 @Test
 public void testRemoveAcc() {
String sequence = "RES\n"
   + "1b:b-dglc-HEX-1:5\n"
   + "2s:n-acetyl\n"
   + "3b:b-dgal-HEX-1:5\n"
   + "4b:a-lgal-HEX-1:5|6:d\n"
   + "5b:a-lgal-HEX-1:5|6:d\n"
   + "LIN\n"
   + "1:1d(2+1)2n\n"
   + "2:1o(3+1)3d\n"
   + "3:3o(2+1)4d\n"
   + "4:1o(4+1)5d\n";
   /*RES
 1b:b-dglc-HEX-1:5
 2s:n-acetyl
 3b:a-lgal-HEX-1:5|6:d
 4b:b-dgal-HEX-1:5
 5b:a-lgal-HEX-1:5|6:d
 LIN
 1:1d(2+1)2n
 2:1o(3+1)3d
 3:1o(4+1)4d
 4:4o(2+1)5d*/
   Map<String, Object>  map = new HashMap<String, Object>();
   map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
   map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "12345REMOVEME");
   map.put(GlycanRegisterRest.USERNAME, myid);
   map.put(GlycanRegisterRest.API_KEY, mykey);
   map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REMOVE_PARTNER_ACCESSION_CMD);

   Map<String, Object>  results = glycanRest.registerStructure(map);
   
   logger.debug(results);
   Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
   Assert.assertEquals(msg.getMessage(), "12345REMOVEME for RES\n" + 
       "1b:b-dglc-HEX-1:5\n" + 
       "2s:n-acetyl\n" + 
       "3b:b-dgal-HEX-1:5\n" + 
       "4b:a-lgal-HEX-1:5|6:d\n" + 
       "5b:a-lgal-HEX-1:5|6:d\n" + 
       "LIN\n" + 
       "1:1d(2+1)2n\n" + 
       "2:1o(3+1)3d\n" + 
       "3:3o(2+1)4d\n" + 
       "4:1o(4+1)5d\n" + 
       " removed.");
 }
 
 @Test
 public void testRemoveAcc2() {
   String sequence = "WURCS=2.0/3,4,3/[a2122h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-3/a3-b1_a4-d1_b2-c1";
   Map<String, Object>  map = new HashMap<String, Object>();
   map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
   map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "1234");
   map.put(GlycanRegisterRest.USERNAME, myid);
   map.put(GlycanRegisterRest.API_KEY, mykey);
   map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REMOVE_PARTNER_ACCESSION_CMD);

   Map<String, Object>  results = glycanRest.registerStructure(map);
   
   logger.debug(results);
   Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
   
   Assert.assertEquals(msg.getMessage(), "1234 for WURCS=2.0/3,4,3/[a2122h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-3/a3-b1_a4-d1_b2-c1 removed (if it existed).");
 }
 
 @Test
 public void testRemoveAccinvalidnum() {
   String sequence = "WURCS=2.0/3,4,3/[a2122h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-3/a3-b1_a4-d1_b2-c1";
   Map<String, Object>  map = new HashMap<String, Object>();
   map.put(GlycanClientRegisterSpec.SEQUENCE, sequence);
   map.put(GlycanClientRegisterSpec.PUBLIC_DATABASE_STRUCTURE_ID, "1234INVALID");
   map.put(GlycanRegisterRest.USERNAME, myid);
   map.put(GlycanRegisterRest.API_KEY, mykey);
   map.put(GlycanClientRegisterSpec.REGISTER_CMD, GlycanClientRegisterSpec.REMOVE_PARTNER_ACCESSION_CMD);

   Map<String, Object>  results = glycanRest.registerStructure(map);
   
   logger.debug(results);
   Message msg = (Message) results.get(GlycanRegisterRest.MESSAGE);
   
   Assert.assertEquals(msg.getMessage(), "1234INVALID for WURCS=2.0/3,4,3/[a2122h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-3/a3-b1_a4-d1_b2-c1 removed (if it existed).");
 }
}
