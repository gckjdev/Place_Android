package com.orange.place.helper;

import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;

public class SqlConstants {

	public static final String SQL_CREATE_POST = " (" //
			+ DBConstants.F_POSTID + " TEXT PRIMARY KEY, " //
			+ DBConstants.F_USERID + " TEXT, " //
			+ DBConstants.F_PLACEID + " TEXT, " //
			+ DBConstants.F_LONGITUDE + " TEXT, " //
			+ DBConstants.F_LATITUDE + " TEXT, " //
			+ DBConstants.F_USER_LONGITUDE + " TEXT, " //
			+ DBConstants.F_USER_LATITUDE + " TEXT, " //
			+ DBConstants.F_TEXT_CONTENT + " TEXT, " //
			+ DBConstants.F_CONTENT_TYPE + " TEXT, " //
			+ DBConstants.F_TOTAL_VIEW + " TEXT, " //
			+ DBConstants.F_TOTAL_FORWARD + " TEXT, " //
			+ DBConstants.F_TOTAL_QUOTE + " TEXT, " //
			+ DBConstants.F_TOTAL_REPLY + " TEXT, " //
			+ DBConstants.F_CREATE_DATE + " TEXT, " //
			+ DBConstants.F_SRC_POSTID + " TEXT, " //
			+ DBConstants.F_NICKNAME + " TEXT, " //
			+ DBConstants.F_AVATAR + " TEXT, " //
			+ DBConstants.C_TOTAL_RELATED + " TEXT, " //
			+ DBConstants.F_IMAGE_URL + " TEXT, " //
			+ DBConstants.F_NAME + " TEXT" // don't have "," for last one !
			+ ");";
	public static final String SQL_TABLE_NEARBY_POST = "CREATE TABLE " + Constants.TABLE_NEARBY_POST + SQL_CREATE_POST;
	public static final String SQL_TABLE_FOLLOWED_POST = "CREATE TABLE " + Constants.TABLE_FOLLOWED_POST
			+ SQL_CREATE_POST;
	public static final String SQL_TABLE_REPLIED_POST = "CREATE TABLE " + Constants.TABLE_REPLIED_POST
			+ SQL_CREATE_POST;
	public static final String SQL_CREATE_PLACE_POST = "CREATE TABLE " + Constants.TABLE_PLACE_POST + SQL_CREATE_POST;

	private static final String SQL_CREATE_PLACE = " (" //
			+ DBConstants.F_PLACEID + " TEXT PRIMARY KEY, " //
			+ DBConstants.F_CREATE_DATE + " TEXT, " //
			+ DBConstants.F_RADIUS + " TEXT, " //
			+ DBConstants.F_POST_TYPE + " TEXT, " //
			+ DBConstants.F_DESC + " TEXT, " //
			+ DBConstants.F_NAME + " TEXT, " //
			+ DBConstants.F_LATITUDE + " TEXT, " //
			+ DBConstants.F_LONGITUDE + " TEXT, " //
			+ DBConstants.F_USERID + " TEXT" // don't have "," for last one !
			+ ");";
	public static final String SQL_CREATE_FOLLOWED_PLACE = "CREATE TABLE " + Constants.TABLE_FOLLOWED_PLACE
			+ SQL_CREATE_PLACE;
	public static final String SQL_CREATE_NEARBY_PLACE = "CREATE TABLE " + Constants.TABLE_NEARBY_PLACE
			+ SQL_CREATE_PLACE;

}
