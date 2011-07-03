package com.orange.place.helper;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.util.Log;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.constants.Constants;
import com.orange.utils.UriUtil;

public class UriHelper {
	private static final String LOG_CREATED_URI = "Created uri: ";

	private static Uri.Builder getHttpBaseUriBuilder() {
		Builder uriBase = Uri.parse(Constants.SERVER_HTTP).buildUpon();
		uriBase.path(Constants.CONTEXT_PATH);
		return uriBase;
	}

	public static Uri createRegUri(String loginId, String deviceId) {
		Uri.Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_REGISTRATION);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_DEVICEMODEL, Build.MODEL);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_DEVICEOS, Constants.OS + ":" + Build.VERSION.SDK_INT);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LOGINIDTYPE, String.valueOf(DBConstants.LOGINID_OWN));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LOGINID, loginId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_NICKNAME, loginId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_DEVICEID, deviceId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, "Created uri for registration: " + uri.toString());
		return uri;
	}

	public static Uri createDeviceLoginUri(String deviceId) {
		Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_DEVICELOGIN);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_DEVICEID, deviceId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_NEED_RETURN_USER, "1");

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, "Created uri for registration: " + uri.toString());
		return uri;
	}

	public static Uri createGetRelatedPostsUri(String userId, String postId) {
		Builder uriBase = getHttpBaseUriBuilder();
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETPOSTRELATEDPOST);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_POSTID, postId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetPlacePostsUri(String userId, String placeId) {
		Builder uriBase = getHttpBaseUriBuilder();
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETPLACEPOST);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_PLACEID, placeId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetNearbyPostsUri(String userId, double longtitude, double latitude) {
		Builder uriBase = getHttpBaseUriBuilder();
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETNEARBYPOSTS);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LONGTITUDE, String.valueOf(longtitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetFollowedPostsUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETUSERFOLLOWPOSTS);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetRepliedPostsUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETMEPOST);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetFollowedPlacesUri(String userId) {
		Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETUSERFOLLOWPLACE);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createGetNearbyPlacesUri(String userId, double longitude, double latitude) {
		Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_GETNEARBYPLACE);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LONGTITUDE, String.valueOf(longitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createNewPostUri(String userId, String placeId, String postContent, double longitude,
			double latitude, int contentType, String syncSns, String srcPostId, String replyPostId) {
		Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_CREATEPOST);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_PLACEID, placeId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LONGTITUDE, String.valueOf(longitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USER_LONGITUDE, String.valueOf(longitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USER_LATITUDE, String.valueOf(latitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_TEXT_CONTENT, postContent);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_CONTENT_TYPE, String.valueOf(contentType));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_SYNC_SNS, syncSns);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_SRC_POSTID, srcPostId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_REPLY_POSTID, replyPostId);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}

	public static Uri createNewPlacesUri(String userId, String placeName, String placeDesc, double longitude,
			double latitude, double radius, String postType) {
		Builder uriBase = getHttpBaseUriBuilder();

		UriUtil.appendQueryParameter(uriBase, ServiceConstant.METHOD, ServiceConstant.METHOD_CREATEPLACE);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_APPID, Constants.APP_NAME);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_USERID, userId);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LONGTITUDE, String.valueOf(longitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_LATITUDE, String.valueOf(latitude));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_NAME, placeName);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_RADIUS, String.valueOf(radius));
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_POSTTYPE, postType);
		UriUtil.appendQueryParameter(uriBase, ServiceConstant.PARA_DESC, placeDesc);

		Uri uri = uriBase.build();
		Log.d(Constants.LOG_TAG, LOG_CREATED_URI + uri.toString());
		return uri;
	}
}