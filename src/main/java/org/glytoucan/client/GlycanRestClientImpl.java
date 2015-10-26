//package org.glytoucan.client;
//
//import java.awt.image.BufferedImage;
//import java.util.Arrays;
//
//import org.apache.commons.codec.binary.Base64;
//import org.glyspace.registry.database.GlycanEntity;
//import org.glyspace.registry.service.search.CombinationSearch;
//import org.glyspace.registry.view.Confirmation;
//import org.glyspace.registry.view.Glycan;
//import org.glyspace.registry.view.GlycanInputList;
//import org.glyspace.registry.view.GlycanList;
//import org.glyspace.registry.view.GlycanResponse;
//import org.glyspace.registry.view.GlycanResponseList;
//import org.glyspace.registry.view.User;
//import org.glyspace.registry.view.search.CompositionSearchInput;
//import org.glytoucan.client.exception.ClientException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component(value="glycanRestClient")
//@SuppressWarnings({ "rawtypes", "unchecked" })
//public class GlycanRestClientImpl implements GlycanRestClient{
//	
//	String username;
//	String password;
//	String rootUrl;
//	
//	@Autowired
//	RestTemplate restTemplate;
//	
//	// This is relative to the rootUrl (defined in the configuration file)
//	private final static String glycanServiceUrl = "/glycans";
//	
//	static HttpHeaders getHeaders(String auth) {
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_XML);
//	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
//
//	    byte[] encodedAuthorisation = Base64.encodeBase64(auth.getBytes());
//	    headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
//
//	    return headers;
//	 }
//
//	@Override
//	public GlycanEntity getGlycan(String accessionNumber) throws ClientException {
//		GlycanEntity glycan = this.restTemplate.getForObject(rootUrl + glycanServiceUrl + "/" + accessionNumber, GlycanEntity.class);
//		return glycan;
//	}
//
//	/**
//	 * @param idOnly, if true it returns only the list of (String) accession numbers for the glycans, otherwise it returns the list of GlycanEntity objects
//	 */
//	@Override
//	public GlycanList getGlycanList(boolean idOnly) {
//		GlycanList responseEntity;
//		if (idOnly)
//			responseEntity = this.restTemplate.getForObject(rootUrl + glycanServiceUrl + "/list", GlycanList.class);
//		else 
//			responseEntity = this.restTemplate.getForObject(rootUrl + glycanServiceUrl + "/list?payload=full", GlycanList.class);
//		return responseEntity;
//	}
//
//	
//	/**
//	 * @param Glycan
//	 * @return the accession number for the new added glycan
//	 */
//	@Override
//	public GlycanResponse addGlycan(Glycan glycan) throws ClientException{
//		// send the authorization header
//		HttpEntity requestEntity = new HttpEntity(glycan,
//		        getHeaders(username + ":" + password));
//		final ResponseEntity<GlycanResponse> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/add", HttpMethod.POST, requestEntity, GlycanResponse.class);
//		return responseEntity.getBody();
//	}
//	
//	@Override
//	public GlycanList substructureSearch(Glycan glycan)
//			throws ClientException {
//		GlycanList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/search/substructure", glycan, GlycanList.class);
//		return result;
//	}
//	
//
//	/**
//	 * @param username the username to set
//	 */
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	/**
//	 * @param password the password to set
//	 */
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	/**
//	 * @param rootURL the rootURL to set
//	 */
//	public void setRootUrl(String rootURL) {
//		this.rootUrl = rootURL;
//	}
//
//	/**
//	 * @param restTemplate the restTemplate to set
//	 */
//	public void setRestTemplate(RestTemplate restTemplate) {
//		this.restTemplate = restTemplate;
//	}
//
//	@Override
//	public GlycanList compositionSearch(CompositionSearchInput input)
//			throws ClientException {
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_XML);
//	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
//		HttpEntity requestEntity = new HttpEntity(input,
//		        headers);
//		final ResponseEntity<GlycanList> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/search/composition", HttpMethod.POST, requestEntity, GlycanList.class);
//		return responseEntity.getBody();
//	}
//
//	@Override
//	public GlycanList searchByUser(User user) throws ClientException {
//		GlycanList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/search/user", user, GlycanList.class);
//		return result;
//	}
//
//	@Override
//	public GlycanEntity exactStructureSearch(Glycan glycan)
//			throws ClientException {
//		GlycanEntity result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/search/exact", glycan, GlycanEntity.class);
//		return result;
//	}
//
//	@Override
//	public GlycanResponseList checkGlycanStructures(GlycanInputList glycans)
//			throws ClientException {
//		GlycanResponseList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/check/list", glycans, GlycanResponseList.class);
//		return result;
//	}
//
//	@Override
//	public GlycanResponseList addGlycanStructures(GlycanInputList glycans)
//			throws ClientException {
//		// send the authorization header
//		HttpEntity requestEntity = new HttpEntity(glycans,
//		        getHeaders(username + ":" + password));
//		final ResponseEntity<GlycanResponseList> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/add/list", HttpMethod.POST, requestEntity, GlycanResponseList.class);
//		return responseEntity.getBody();
//	}
//
//	@Override
//	public Confirmation deleteGlycan(String accessionNumber)
//			throws ClientException {
//		// send the authorization header
//		HttpEntity requestEntity = new HttpEntity(
//		        getHeaders(username + ":" + password));
//		final ResponseEntity<Confirmation> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/" + accessionNumber + "/delete", HttpMethod.DELETE, requestEntity, Confirmation.class);
//		return responseEntity.getBody();
//	}
//
//	@Override
//	public GlycanList motifSearch(String motifName)
//			throws ClientException {
//		GlycanList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/search/motif", motifName, GlycanList.class);
//		return result;
//	}
//
//	@Override
//	public BufferedImage getGlycanImage(String accessionNumber, String format, String notation, String style)
//			throws ClientException {
//		
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setAccept(Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG));
//	    HttpEntity requestEntity = new HttpEntity(headers);
//		return restTemplate.exchange(rootUrl + glycanServiceUrl + "/" + accessionNumber + "/image?format=" + format + "&notation=" + notation + "&style=" + style,  HttpMethod.GET,  requestEntity, BufferedImage.class).getBody();
//	}
//
//	@Override
//	public String getGlycanSVGImage(String accessionNumber, String notation,
//			String style) throws ClientException {
//	    HttpEntity requestEntity = new HttpEntity(getHeaders(""));
//		return restTemplate.exchange(rootUrl + glycanServiceUrl + "/" + accessionNumber + "/image?format=svg&notation=" + notation + "&style=" + style,  HttpMethod.GET,  requestEntity, String.class).getBody();
//	}
//
//	@Override
//	public GlycanList complexSearch(CombinationSearch search)
//			throws ClientException {
//		GlycanList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/search/complex", search, GlycanList.class);
//		return result;
//	}
//
//	@Override
//	public Confirmation deleteGlycansByAccessionNumbers(
//			GlycanList accessionNumbers) throws ClientException {
//		// send the authorization header
//		HttpEntity requestEntity = new HttpEntity(accessionNumbers,
//		        getHeaders(username + ":" + password));
//		final ResponseEntity<Confirmation> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/delete/list", HttpMethod.DELETE, requestEntity, Confirmation.class);
//		return responseEntity.getBody();
//	}
//
//	@Override
//	public GlycanList getGlycansByAccessionNumbers(GlycanList accessionNumbers)
//			throws ClientException {
//		GlycanList result = this.restTemplate.postForObject(rootUrl + glycanServiceUrl + "/list/accessionNumber", accessionNumbers, GlycanList.class);
//		return result;
//	}
//
//	@Override
//	public GlycanList searchPendingByUser(User user)
//			throws ClientException {
//		HttpEntity requestEntity = new HttpEntity(user, getHeaders(username + ":" + password));
//		final ResponseEntity<GlycanList> responseEntity = this.restTemplate.exchange (rootUrl + glycanServiceUrl + "/search/user/pending", HttpMethod.POST, requestEntity, GlycanList.class);
//		return responseEntity.getBody();
//	}
//}
