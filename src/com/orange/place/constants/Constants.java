package com.orange.place.constants;

import com.orange.utils.UtilConstants;

public class Constants {

	// server side
	public static final String SERVER_HTTP = "http://192.168.1.160:8000";
	// public static final String SERVER_HTTP = "http://192.168.0.67:8000";
	public static final String CONTEXT_PATH = "/api/i";

	// sqlite database
	public static final String TABLE_NEARBY_PLACE = "Nearby_Place";
	public static final String TABLE_FOLLOWED_PLACE = "Followed_Place";
	public static final String TABLE_PLACE_POST = "Place_Post";

	// system
	public static final int SQLITE_DB_VERSION = 1;
	public static final String OS = "Android";
	public static final String APP_NAME = "PLACE";
	public static final String LOG_TAG = "OrangePlace";
	public static final String KEY_PLACE = "Key_Place";
	public static final String KEY_RELATED_POSTS = "Key_Related_Posts";
	public static final String PREF_NAME = "PLACE_PREF";
	public static final String SQLITE_DB_NAME = "PLACE_DB";

	public static final int ERROR_UNKOWN = 70000;
	public static final int ERROR_RESP_NULL = 70001;
	public static final int ERROR_RESP_PARSE = 70002;
	public static final int ERROR_RESP_CODE_EMPTY = 70003;
	public static final int ERROR_RESP_DATA_EMPTY = 70004;
	public static final int ERROR_LOCATION_UNKNOWN = 70005;
	public static final int ERROR_PLACEID_UNKNOWN = 70006;
	public static final int ERROR_SQLITE = 70006;

	// this set constant in Utils, but not mandatory
	public static void initUtilsConstants() {
		UtilConstants.LOG_TAG = LOG_TAG;
	}
}
