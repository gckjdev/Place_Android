package com.orange.place.constants;

import com.orange.utils.UtilConstants;

public class Constants {

	// server side
	// public static final String SERVER_HTTP = "http://192.168.1.162:8000";
	public static final String SERVER_HTTP = "http://192.168.0.67:8000";
	public static final String CONTEXT_PATH = "/api/i";

	// system
	public static final String PREF_NAME = "PLACE_PREF";
	public static final String LOG_TAG = "OrangePlace";
	public static final String APP_NAME = "PLACE";
	public static final String OS = "Android";

	public static final int ERROR_RESP_UNKOWN = 70000;
	public static final int ERROR_RESP_NULL = 70001;
	public static final int ERROR_RESP_PARSE = 70002;
	public static final int ERROR_RESP_NO_RETURN_CODE = 70003;

	// this set constant in Utils, but not mandatory
	public static void initUtilsConstants() {
		UtilConstants.LOG_TAG = LOG_TAG;
	}
}