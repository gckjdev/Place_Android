package com.orange.place.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.orange.place.R;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.constants.Constants;
import com.orange.utils.JsonUtil;

public class SqlLiteHelper extends SQLiteOpenHelper {

	private static final String LOG_STORING_DATA_ERROR = "Storing data error!";

	private static final String LOG_ERROR_NO_DATA = "No data found for DB store, will just ignore!";

	private static SQLiteDatabase writableDb = null;

	public SqlLiteHelper(Context context) {
		super(context, Constants.SQLITE_DB_NAME, null, Constants.SQLITE_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(Constants.LOG_TAG, "Creating database");
		try {
			db.execSQL(SqlLiteHelper.SQL_CREATE_NEARBY_PLACE);
			db.execSQL(SqlLiteHelper.SQL_CREATE_PLACE_POST);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, "Get SQL exception!", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Constants.LOG_TAG, "Upgrading db from version " + oldVersion + " to " + newVersion
				+ " all data will be clobbered");
		db.execSQL("DROP TABLE IF EXISTS table-name-abc");
		this.onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.v(Constants.LOG_TAG, "Opening database");
	}

	public int storePlacePosts(JSONArray jsonArr, String placeId) {
		int len = jsonArr.length();
		if (len <= 0) {
			Log.w(Constants.LOG_TAG, LOG_ERROR_NO_DATA);
			return Constants.ERROR_RESP_DATA_EMPTY;
		}

		ContentValues cv = null;
		cleanupPlacePosts(placeId); // only keep the latest data
		for (int i = 0; i < len; i++) {
			try {
				cv = createCVForPost((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_PLACE_POST, null, cv);
			} catch (JSONException e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}
		return Constants.ERROR_UNKOWN;
	}

	public int storeNearbyPlaces(JSONArray jsonArr) {
		int len = jsonArr.length();
		if (len <= 0) {
			Log.w(Constants.LOG_TAG, LOG_ERROR_NO_DATA);
			return Constants.ERROR_RESP_DATA_EMPTY;
		}

		ContentValues cv = null;
		cleanupNearbyPlaces(); // only keep the latest data
		for (int i = 0; i < len; i++) {
			try {
				cv = createCVForPlace((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_NEARBY_PLACE, null, cv);
			} catch (JSONException e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}
		return Constants.ERROR_UNKOWN;
	}

	public void cleanupPlacePosts(String placeId) {
		Log.d(Constants.LOG_TAG, "Cleanup place posts in DB, where placeId=" + placeId);

		String whereClause = DBConstants.F_PLACEID + "=?";
		String[] whereArgs = new String[1];
		whereArgs[0] = placeId;
		getDatabase().delete(Constants.TABLE_PLACE_POST, whereClause, whereArgs);
	}

	public void cleanupNearbyPlaces() {
		Log.d(Constants.LOG_TAG, "Cleanup nearby places in DB.");
		getDatabase().delete(Constants.TABLE_NEARBY_PLACE, null, null);
	}

	public void updatePlacePostList(List<Map<String, Object>> list, String placeId) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryPlacePosts(placeId);
		while (cur.moveToNext()) {
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
			tmpList.add(post);
		}
		cur.close();

		if (tmpList.size() != 0) {
			list.clear();
			list.addAll(tmpList);
		} else {
			Log.w(Constants.LOG_TAG, "Get an empty place post list, will not clear and update old place post list");
		}
	}

	public void updatePlaceList(List<Map<String, Object>> list) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();

		Cursor cur = queryNearbyPlaces();
		while (cur.moveToNext()) {
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
			place.put("PlaceImage", R.drawable.z_tmp_icon1); // change it !
			tmpList.add(place);
		}
		cur.close();

		if (tmpList.size() != 0) {
			list.clear();
			list.addAll(tmpList);
		} else {
			Log.w(Constants.LOG_TAG, "Get an empty place list, will not clear and update old place list");
		}
	}

	private Cursor queryPlacePosts(String placeId) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Constants.TABLE_PLACE_POST);
		String selection = DBConstants.F_PLACEID + "=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = placeId;

		Cursor cursor = queryBuilder.query(getDatabase(), null, selection, selectionArgs, null, null, null);
		Log.d(Constants.LOG_TAG, "Get place posts from DB. PlaceId:" + placeId);
		return cursor;
	}

	private Cursor queryNearbyPlaces() {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Constants.TABLE_NEARBY_PLACE);

		Cursor cursor = queryBuilder.query(getDatabase(), null, null, null, null, null, null);
		Log.d(Constants.LOG_TAG, "Get place list from DB. Amount: " + cursor.getCount());
		return cursor;
	}

	private SQLiteDatabase getDatabase() {
		if (writableDb == null) {
			writableDb = getWritableDatabase();
		}
		return writableDb;
	}

	private ContentValues createCVForPost(JSONObject json) {
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

	private ContentValues createCVForPlace(JSONObject json) {
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

	// private static final String SQL_CLEANUP_NEARBY_PLACE = "DELETE FROM " + Constants.TABLE_NEARBY_PLACE + ";";
	private static final String SQL_CREATE_PLACE_POST = "CREATE TABLE " + Constants.TABLE_PLACE_POST + " (" //
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

	private static final String SQL_CREATE_NEARBY_PLACE = "CREATE TABLE " + Constants.TABLE_NEARBY_PLACE + " (" //
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
}
