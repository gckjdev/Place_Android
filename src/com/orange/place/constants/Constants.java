package com.orange.place.constants;

import com.orange.place.R;
import com.orange.place.constant.DBConstants;
import com.orange.utils.UtilConstants;

public class Constants {

	// server side
	public static final String SERVER_HTTP = "http://192.168.1.163:8000";
	// public static final String SERVER_HTTP = "http://192.168.0.67:8000";
	public static final String CONTEXT_PATH = "/api/i";

	// sqlite database
	public static final String TABLE_NEARBY_PLACE = "Nearby_Place";
	public static final String TABLE_FOLLOWED_PLACE = "Followed_Place";
	public static final String TABLE_PLACE_POST = "Place_Post";
	public static final String TABLE_NEARBY_POST = "Nearby_Post";
	public static final String TABLE_FOLLOWED_POST = "Followed_Post";
	public static final String TABLE_REPLIED_POST = "Replied_Post";

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

	// list view mapping
	public static String[] postsViewFrom = new String[] { "UserImage", DBConstants.F_TEXT_CONTENT,
			DBConstants.F_USERID, DBConstants.F_CREATE_DATE, DBConstants.C_TOTAL_RELATED };
	public static int[] postsViewTo = new int[] { R.id.user_image, R.id.post_content, R.id.user_id, R.id.post_time,
			R.id.post_related };
	public static String[] placeViewFrom = new String[] { DBConstants.F_NAME, DBConstants.F_DESC, DBConstants.F_USERID };
	public static int[] placeViewTo = new int[] { R.id.place_name, R.id.place_desc, R.id.user_id };

	// this set constant in Utils, but not mandatory
	public static void initUtilsConstants() {
		UtilConstants.LOG_TAG = LOG_TAG;
	}
}
