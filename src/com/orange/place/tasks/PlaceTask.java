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
import com.orange.place.helper.GlobalVarHelper;
import com.orange.place.helper.JsonHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.place.helper.UriHelper;
import com.orange.utils.HttpUtils;

public class PlaceTask {

	public static void getFollowedPlacesFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getFollowedPlaces(list);
	}

	public static void getNearbyPlacesFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getNearbyPlaces(list);
	}

	public static void getPlacePostsFromDB(Context context, List<Map<String, Object>> list, String placeId) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getPlacePosts(list, placeId);
	}

	public static int getRelatedPostsFromServer(Context context, String postId) {
		if (postId == null) {
			Log.e(Constants.LOG_TAG, "The postId is null, no request to server!");
			return Constants.ERROR_PLACEID_UNKNOWN;
		}
		Uri uri = UriHelper.createGetRelatedPostsUri(PrefHelper.getUserId(context), postId);
		JSONObject json = HttpUtils.httpGet(uri);
		
		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			GlobalVarHelper.storeRelatedPosts(JsonHelper.getReturnDataArray(json), postId); // only stores temporarily 
		}
		
		return resultCode;
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
	
	public static int getFollowedPlacesFromServer(Context context) {
		Uri uri = UriHelper.createGetFollowedPlacesUri(PrefHelper.getUserId(context));
		JSONObject json = HttpUtils.httpGet(uri);
		
		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeFollowedPlaces(JsonHelper.getReturnDataArray(json));
		}
		
		return resultCode;
	}
	
	public static int getNearbyPlacesFromServer(Context context, Location location) {
		if (location == null) {
			Log.e(Constants.LOG_TAG, "The location is null, no request to server!");
			return Constants.ERROR_LOCATION_UNKNOWN;
		}

		Uri uri = UriHelper.createGetNearbyPlacesUri(PrefHelper.getUserId(context), location.getLongitude(),
				location.getLatitude());
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeNearbyPlaces(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}
}
