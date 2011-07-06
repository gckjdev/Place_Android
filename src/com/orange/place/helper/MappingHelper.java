package com.orange.place.helper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.orange.place.R;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.utils.JsonUtil;

public class MappingHelper {

	public static ContentValues mapJsonToCV_Place(JSONObject json) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.F_PLACEID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_PLACEID));
		cv.put(DBConstants.F_CREATE_DATE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CREATE_DATE));
		cv.put(DBConstants.F_RADIUS, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_RADIUS));
		cv.put(DBConstants.F_POST_TYPE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_POSTTYPE));
		cv.put(DBConstants.F_DESC, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_DESC));
		cv.put(DBConstants.F_NAME, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_NAME));
		cv.put(DBConstants.F_LATITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LATITUDE));
		cv.put(DBConstants.F_LONGITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LONGTITUDE));
		cv.put(DBConstants.F_USERID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CREATE_USERID));
		return cv;
	}

	public static Map<String, Object> mapCursorToMap_Place(Cursor cur) {
		Map<String, Object> place = new HashMap<String, Object>();
		place.put(DBConstants.F_PLACEID, cur.getString(cur.getColumnIndex(DBConstants.F_PLACEID)));
		place.put(DBConstants.F_CREATE_DATE, cur.getString(cur.getColumnIndex(DBConstants.F_CREATE_DATE)));
		place.put(DBConstants.F_RADIUS, cur.getString(cur.getColumnIndex(DBConstants.F_RADIUS)));
		place.put(DBConstants.F_POST_TYPE, cur.getString(cur.getColumnIndex(DBConstants.F_POST_TYPE)));
		place.put(DBConstants.F_DESC, cur.getString(cur.getColumnIndex(DBConstants.F_DESC)));
		place.put(DBConstants.F_NAME, cur.getString(cur.getColumnIndex(DBConstants.F_NAME)));
		place.put(DBConstants.F_LATITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_LATITUDE)));
		place.put(DBConstants.F_LONGITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_LONGITUDE)));
		place.put(DBConstants.F_USERID, cur.getString(cur.getColumnIndex(DBConstants.F_USERID)));
		return place;
	}

	public static Map<String, Object> mapJsonToMap_Post(JSONObject json) {
		Map<String, Object> post = new HashMap<String, Object>();
		post.put(DBConstants.F_POSTID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_POSTID));
		post.put(DBConstants.F_USERID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USERID));
		post.put(DBConstants.F_PLACEID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_PLACEID));
		post.put(DBConstants.F_LONGITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LONGTITUDE));
		post.put(DBConstants.F_LATITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LATITUDE));
		post.put(DBConstants.F_USER_LONGITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USER_LONGITUDE));
		post.put(DBConstants.F_USER_LATITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USER_LATITUDE));
		post.put(DBConstants.F_TEXT_CONTENT, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TEXT_CONTENT));
		post.put(DBConstants.F_CONTENT_TYPE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CONTENT_TYPE));
		post.put(DBConstants.F_IMAGE_URL, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_IMAGE_URL));
		post.put(DBConstants.F_TOTAL_VIEW, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_VIEW));
		post.put(DBConstants.F_TOTAL_FORWARD, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_FORWARD));
		post.put(DBConstants.F_TOTAL_QUOTE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_QUOTE));
		post.put(DBConstants.F_TOTAL_REPLY, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_REPLY));
		post.put(DBConstants.F_CREATE_DATE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CREATE_DATE));
		post.put(DBConstants.F_SRC_POSTID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_SRC_POSTID));
		post.put(DBConstants.F_NICKNAME, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_NICKNAME));
		post.put(DBConstants.F_AVATAR, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_AVATAR));
		post.put(DBConstants.C_TOTAL_RELATED, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_RELATED));
		post.put(DBConstants.F_NAME, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_NAME));
		post.put("UserImage", R.drawable.z_tmp_icon1); // change it !
		return post;
	}

	public static ContentValues mapJsonToCV_Post(JSONObject json) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.F_POSTID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_POSTID));
		cv.put(DBConstants.F_USERID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USERID));
		cv.put(DBConstants.F_PLACEID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_PLACEID));
		cv.put(DBConstants.F_LONGITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LONGTITUDE));
		cv.put(DBConstants.F_LATITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_LATITUDE));
		cv.put(DBConstants.F_USER_LONGITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USER_LONGITUDE));
		cv.put(DBConstants.F_USER_LATITUDE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_USER_LATITUDE));
		cv.put(DBConstants.F_TEXT_CONTENT, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TEXT_CONTENT));
		cv.put(DBConstants.F_CONTENT_TYPE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CONTENT_TYPE));
		cv.put(DBConstants.F_IMAGE_URL, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_IMAGE_URL));
		cv.put(DBConstants.F_TOTAL_VIEW, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_VIEW));
		cv.put(DBConstants.F_TOTAL_FORWARD, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_FORWARD));
		cv.put(DBConstants.F_TOTAL_QUOTE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_QUOTE));
		cv.put(DBConstants.F_TOTAL_REPLY, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_REPLY));
		cv.put(DBConstants.F_CREATE_DATE, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_CREATE_DATE));
		cv.put(DBConstants.F_SRC_POSTID, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_SRC_POSTID));
		cv.put(DBConstants.F_NICKNAME, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_NICKNAME));
		cv.put(DBConstants.F_AVATAR, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_AVATAR));
		cv.put(DBConstants.C_TOTAL_RELATED, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_TOTAL_RELATED));
		cv.put(DBConstants.F_NAME, JsonUtil.getStringOrNull(json, ServiceConstant.PARA_NAME));
		return cv;
	}

	public static Map<String, Object> mapCursorToMap_Post(Cursor cur) {
		Map<String, Object> post = new HashMap<String, Object>();
		post.put(DBConstants.F_POSTID, cur.getString(cur.getColumnIndex(DBConstants.F_POSTID)));
		post.put(DBConstants.F_USERID, cur.getString(cur.getColumnIndex(DBConstants.F_USERID)));
		post.put(DBConstants.F_PLACEID, cur.getString(cur.getColumnIndex(DBConstants.F_PLACEID)));
		post.put(DBConstants.F_LONGITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_LONGITUDE)));
		post.put(DBConstants.F_LATITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_LATITUDE)));
		post.put(DBConstants.F_USER_LONGITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_USER_LONGITUDE)));
		post.put(DBConstants.F_USER_LATITUDE, cur.getString(cur.getColumnIndex(DBConstants.F_USER_LATITUDE)));
		post.put(DBConstants.F_TEXT_CONTENT, cur.getString(cur.getColumnIndex(DBConstants.F_TEXT_CONTENT)));
		post.put(DBConstants.F_CONTENT_TYPE, cur.getString(cur.getColumnIndex(DBConstants.F_CONTENT_TYPE)));
		post.put(DBConstants.F_IMAGE_URL, cur.getString(cur.getColumnIndex(DBConstants.F_IMAGE_URL)));
		post.put(DBConstants.F_TOTAL_VIEW, cur.getString(cur.getColumnIndex(DBConstants.F_TOTAL_VIEW)));
		post.put(DBConstants.F_TOTAL_FORWARD, cur.getString(cur.getColumnIndex(DBConstants.F_TOTAL_FORWARD)));
		post.put(DBConstants.F_TOTAL_QUOTE, cur.getString(cur.getColumnIndex(DBConstants.F_TOTAL_QUOTE)));
		post.put(DBConstants.F_TOTAL_REPLY, cur.getString(cur.getColumnIndex(DBConstants.F_TOTAL_REPLY)));
		post.put(DBConstants.F_CREATE_DATE, cur.getString(cur.getColumnIndex(DBConstants.F_CREATE_DATE)));
		post.put(DBConstants.F_SRC_POSTID, cur.getString(cur.getColumnIndex(DBConstants.F_SRC_POSTID)));
		post.put(DBConstants.F_NICKNAME, cur.getString(cur.getColumnIndex(DBConstants.F_NICKNAME)));
		post.put(DBConstants.F_AVATAR, cur.getString(cur.getColumnIndex(DBConstants.F_AVATAR)));
		post.put(DBConstants.C_TOTAL_RELATED, cur.getString(cur.getColumnIndex(DBConstants.C_TOTAL_RELATED)));
		post.put(DBConstants.F_NAME, cur.getString(cur.getColumnIndex(DBConstants.F_NAME)));
		post.put("UserImage", R.drawable.z_tmp_icon1); // change it !
		return post;
	}
}
