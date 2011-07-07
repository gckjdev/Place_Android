package com.orange.place.tasks;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.helper.JsonHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.place.helper.UriHelper;
import com.orange.utils.HttpUtils;

public class PostTask {

	public static void getNearbyPostsFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getNearbyPosts(list);
	}

	public static void getFollowedPostsFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getFollowedPosts(list);
	}

	public static void getRepliedPostsFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getRepliedPosts(list);
	}

	public static void getPlacePostsFromDB(Context context, List<Map<String, Object>> list, String placeId) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getPlacePosts(list, placeId);
	}

	public static int getPlacePostsFromServer(Context context, String placeId) {
		if (placeId == null) {
			Log.e(Constants.LOG_TAG, "The placeId is null, no request to server!");
			return Constants.ERROR_PLACEID_UNKNOWN;
		}
		Uri uri = UriHelper.createGetPlacePostsUri(PrefHelper.getUserId(context), placeId);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storePlacePosts(JsonHelper.getReturnDataArray(json), placeId);
		}

		return resultCode;
	}

	public static int getNearbyPostsFromServer(Context context, Location location) {
		if (location == null) {
			Log.e(Constants.LOG_TAG, "The location is null, no request to server!");
			return Constants.ERROR_LOCATION_UNKNOWN;
		}

		Uri uri = UriHelper.createGetNearbyPostsUri(PrefHelper.getUserId(context), location.getLongitude(),
				location.getLatitude());
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeNearbyPosts(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}

	public static int getFollowedPostsFromServer(Context context) {
		Uri uri = UriHelper.createGetFollowedPostsUri(PrefHelper.getUserId(context));
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeFollowedPosts(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}

	public static int getRepliedPostsFromServer(Context context) {
		Uri uri = UriHelper.createGetRepliedPostsUri(PrefHelper.getUserId(context));
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeRepliedPosts(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}

	public static int createPost(Context context, String placeId, String postContent, Location location,
			int contentType, String syncSns, String srcPostId, String replyPostId) {
		if (location == null || postContent == null || "".equals(postContent.trim())) {
			Log.e(Constants.LOG_TAG, "The location/postContent null or empty, no request to server!");
			return Constants.ERROR_LOCATION_UNKNOWN;
		}

		Uri uri = UriHelper.createPostUri(PrefHelper.getUserId(context), placeId, postContent,
				location.getLongitude(), location.getLatitude(), contentType, syncSns, srcPostId, replyPostId);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			// TODO, what todo?
		}

		return resultCode;
	}
}
