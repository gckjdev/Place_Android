package com.orange.place.helper;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.util.Log;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.constants.Constants;

public class UriHelper {
	private static final String LOG_CREATED_URI = "Created uri: ";

	private static Uri.Builder getHttpBaseUriBuilder() {
		Builder uriBase = Uri.parse(Constants.SERVER_HTTP).buildUpon();
		uriBase.path(Constants.CONTEXT_PATH);
		return uriBase;
	}

	public static Uri createRegUri(String loginId, String deviceId) {
		Uri.Builder uriBase = getHttpBaseUriBuilder();

		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_REGISTRATION);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_DEVICEMODEL, Build.MODEL);
		uriBase.appendQueryParameter(ServiceConstant.PARA_DEVICEOS, Constants.OS + ":" + Build.VERSION.SDK_INT);
		uriBase.appendQueryParameter(ServiceConstant.PARA_LOGINIDTYPE, String.valueOf(DBConstants.LOGINID_OWN));
		uriBase.appendQueryParameter(ServiceConstant.PARA_LOGINID, loginId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_NICKNAME, loginId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_DEVICEID, deviceId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, "Created uri for registration: " + uri.toString());
		return uri;
	}

	public static Uri createDeviceLoginUri(String deviceId) {
		Builder uriBase = getHttpBaseUriBuilder();

		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_DEVICELOGIN);
		uriBase.appendQueryParameter(ServiceConstant.PARA_DEVICEID, deviceId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_NEED_RETURN_USER, "1");

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, "Created uri for registration: " + uri.toString());
		return uri;
	}
	
	public static Uri createGetRelatedPostsUri(String userId, String postId) {
		Builder uriBase = getHttpBaseUriBuilder();
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETPOSTRELATEDPOST);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_POSTID, postId);
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetPlacePostsUri(String userId, String placeId) {
		Builder uriBase = getHttpBaseUriBuilder();
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETPLACEPOST);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_PLACEID, placeId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetNearbyPostsUri(String userId, double longtitude, double latitude) {
		Builder uriBase = getHttpBaseUriBuilder();
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETNEARBYPOSTS);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_LONGTITUDE, String.valueOf(longtitude));
		uriBase.appendQueryParameter(ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetFollowedPostsUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETUSERFOLLOWPOSTS);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetRepliedPostsUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETMYPOSTS);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetFollowedPlacesUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();
		
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETUSERFOLLOWPLACE);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
	
	public static Uri createGetNearbyPlacesUri(String userId, double longtitude, double latitude) {
		Builder uriBase = getHttpBaseUriBuilder();
		
		uriBase.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_GETNEARBYPLACE);
		uriBase.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		uriBase.appendQueryParameter(ServiceConstant.PARA_USERID, userId);
		uriBase.appendQueryParameter(ServiceConstant.PARA_LONGTITUDE, String.valueOf(longtitude));
		uriBase.appendQueryParameter(ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));
		
		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
}
