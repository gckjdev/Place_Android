package com.orange.place.common;

import com.orange.utils.UtilConstants;

public class Constants {
	// system
	public static final String PREF_NAME = "PLACE_PREF";
	public static final String LOG_TAG = "OrangePlace";
	public static final String APP_NAME = "PLACE";
	public static final String OS = "Android";
	
	// server side
	public static final String SERVER_HTTP = "http://192.168.0.67:8000";
	public static final String CONTEXT_PATH = "/api/i";
	
	public static void initUtilsConstants() {
		UtilConstants.LOG_TAG = LOG_TAG;
		UtilConstants.PREF_NAME = PREF_NAME;
	}
}
