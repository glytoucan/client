package org.glytoucan.client;

import java.util.Map;

public interface GlycanSpec {

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String MESSAGE = "MESSAGE";
	public static final String CONTENT_TYPE = "CONTENT_TYPE";
	public static final String MEDIA_TYPE = "MEDIA_TYPE";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTH_BASIC_HEADER = "Basic ";
	public static final String HOSTNAME = "HOSTNAME";
	public static final String REGISTRATION = "REGISTRATION";
	public static final String CONTEXT_PATH = "CONTEXT_PATH";

	Map<String, Object> registerStructure(Map<String, Object> map);
}