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
	
	public static int followPlace(Context context, String placeId){
		Uri uri = UriHelper.createFollowPlaceUri(PrefHelper.getUserId(context), placeId);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		return resultCode;
	}

	public static int unfollowPlace(Context context, String placeId){
		Uri uri = UriHelper.createUnfollowPlaceUri(PrefHelper.getUserId(context), placeId);
		JSONObject json = HttpUtils.httpGet(uri);
		
		int resultCode = JsonHelper.getResultCode(json);
		return resultCode;
	}

	public static void getFollowedPlacesFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getFollowedPlaces(list);
	}

	public static void getNearbyPlacesFromDB(Context context, List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
		sqlLiteHelper.getNearbyPlaces(list);
	}
	
	public static int getRelatedPostsFromServer(Context context, String postId) {
		if (postId == null) {
			Log.e(Constants.LOG_TAG, "The postId is null, no request to server!");
			return Constants.ERROR_POSTID_UNKNOWN;
		}

		Uri uri = UriHelper.createGetRelatedPostsUri(PrefHelper.getUserId(context), postId);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			GlobalVarHelper.storeRelatedPosts(JsonHelper.getReturnDataArray(json), postId); // only stores temporarily
		}

		return resultCode;
	}

	public static int getFollowedPlacesFromServer(Context context) {
		Uri uri = UriHelper.createGetFollowedPlacesUri(PrefHelper.getUserId(context));
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
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
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
			sqlLiteHelper.storeNearbyPlaces(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}

	public static int newPlace(Context context, String name, String desc, Location location, double radius,
			String postType) {
		if (location == null || name == null || "".equals(name.trim()) || desc == null || "".equals(desc.trim())) {
			Log.e(Constants.LOG_TAG, "The location/name/desc null or empty, no request to server!");
			return Constants.ERROR_LOCATION_UNKNOWN;
		}

		Uri uri = UriHelper.createPlaceUri(PrefHelper.getUserId(context), name, desc, location.getLongitude(),
				location.getLatitude(), radius, postType);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			// TODO: anything need to do?
		}

		return resultCode;
	}
}
