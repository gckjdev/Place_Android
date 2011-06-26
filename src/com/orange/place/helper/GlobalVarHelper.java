package com.orange.place.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.orange.place.OrangePlaceApplication;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;

public class GlobalVarHelper {
	private static final String LOG_ERROR_NO_DATA = "No data found for global var to store, will just ignore!";
	private static final String LOG_STORING_DATA_ERROR = "Storing data error!";

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getRelatedPosts(String postId) {
		Object obj = OrangePlaceApplication.getGlobalVar(Constants.KEY_RELATED_POSTS);
		List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
		return list;
	}

	public static int storeRelatedPosts(JSONArray jsonArr, String postId) {
		int len = jsonArr.length();
		if (len <= 0) {
			Log.w(Constants.LOG_TAG, LOG_ERROR_NO_DATA);
			return Constants.ERROR_RESP_DATA_EMPTY;
		}

		List<Map<String, Object>> relatedPosts = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			try {
				Map<String, Object> post = MappingHelper.mapJsonToMap_Post((JSONObject) jsonArr.get(i));
				relatedPosts.add(post);
			} catch (Exception e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}

		OrangePlaceApplication.setGlobalVar(Constants.KEY_RELATED_POSTS, relatedPosts);
		Log.w(Constants.LOG_TAG, "Stroing related posts into global var: " + relatedPosts);
		return ErrorCode.ERROR_SUCCESS;
	}
}
