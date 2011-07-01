package com.orange.place.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;

public class SqlLiteHelper extends SQLiteOpenHelper {

	private static final String LOG_EMPTY_LIST = "Get an empty list, will not clear and update old list";
	private static final String LOG_STORING_DATA_ERROR = "Storing data error!";
	private static final String LOG_ERROR_NO_DATA = "No data found for DB to store, will just ignore!";

	private static SQLiteDatabase writableDb = null;

	public SqlLiteHelper(Context context) {
		super(context, Constants.SQLITE_DB_NAME, null, Constants.SQLITE_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(Constants.LOG_TAG, "Creating database");
		try {
			db.execSQL(MappingHelper.SQL_CREATE_FOLLOWED_PLACE);
			db.execSQL(MappingHelper.SQL_CREATE_NEARBY_PLACE);
			db.execSQL(MappingHelper.SQL_CREATE_PLACE_POST);
			db.execSQL(MappingHelper.SQL_TABLE_NEARBY_POST);
			db.execSQL(MappingHelper.SQL_TABLE_FOLLOWED_POST);
			db.execSQL(MappingHelper.SQL_TABLE_REPLIED_POST);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, "Get SQL exception!", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Constants.LOG_TAG, "Upgrading db from version " + oldVersion + " to " + newVersion);
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
				cv = MappingHelper.mapJsonToCV_Post((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_PLACE_POST, null, cv);
			} catch (Exception e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}
		return ErrorCode.ERROR_SUCCESS;
	}

	public int storeFollowedPlaces(JSONArray jsonArr) {
		return cleanAndStorePlaces(jsonArr, Constants.TABLE_FOLLOWED_PLACE);
	}

	public int storeNearbyPlaces(JSONArray jsonArr) {
		return cleanAndStorePlaces(jsonArr, Constants.TABLE_NEARBY_PLACE);
	}

	private int cleanAndStorePlaces(JSONArray jsonArr, String table) {
		int len = jsonArr.length();
		if (len <= 0) {
			Log.w(Constants.LOG_TAG, LOG_ERROR_NO_DATA);
			return Constants.ERROR_RESP_DATA_EMPTY;
		}

		Log.d(Constants.LOG_TAG, "Start to cleanup and store info for table: " + table);
		getDatabase().delete(table, null, null);

		ContentValues cv = null;
		for (int i = 0; i < len; i++) {
			try {
				cv = MappingHelper.mapJsonToCV_Place((JSONObject) jsonArr.get(i));
				getDatabase().insert(table, null, cv);
			} catch (Exception e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}
		return ErrorCode.ERROR_SUCCESS;
	}

	public int storeNearbyPosts(JSONArray jsonArr) {
		return cleanAndStorePosts(jsonArr, Constants.TABLE_NEARBY_POST);
	}

	public int storeFollowedPosts(JSONArray jsonArr) {
		return cleanAndStorePosts(jsonArr, Constants.TABLE_FOLLOWED_POST);
	}

	public int storeRepliedPosts(JSONArray jsonArr) {
		return cleanAndStorePosts(jsonArr, Constants.TABLE_REPLIED_POST);
	}

	private int cleanAndStorePosts(JSONArray jsonArr, String table) {
		int len = jsonArr.length();
		if (len <= 0) {
			Log.w(Constants.LOG_TAG, LOG_ERROR_NO_DATA);
			return Constants.ERROR_RESP_DATA_EMPTY;
		}

		Log.d(Constants.LOG_TAG, "Start to cleanup and store info for table: " + table);
		getDatabase().delete(table, null, null);

		ContentValues cv = null;
		for (int i = 0; i < len; i++) {
			try {
				cv = MappingHelper.mapJsonToCV_Post((JSONObject) jsonArr.get(i));
				getDatabase().insert(table, null, cv);
			} catch (Exception e) {
				Log.e(Constants.LOG_TAG, LOG_STORING_DATA_ERROR, e);
				return Constants.ERROR_SQLITE;
			}
		}
		return ErrorCode.ERROR_SUCCESS;
	}

	public void cleanupPlacePosts(String placeId) {
		Log.d(Constants.LOG_TAG, "Cleanup place posts in DB, where placeId=" + placeId);

		String whereClause = DBConstants.F_PLACEID + "=?";
		String[] whereArgs = new String[1];
		whereArgs[0] = placeId;
		getDatabase().delete(Constants.TABLE_PLACE_POST, whereClause, whereArgs);
	}

	public void getPlacePosts(List<Map<String, Object>> list, String placeId) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryPlacePosts(placeId);
		while (cur.moveToNext()) {
			tmpList.add(MappingHelper.mapCursorToMap_Post(cur));
		}
		cur.close();

		replaceList(list, tmpList);
	}

	public void getNearbyPosts(List<Map<String, Object>> list) {
		getPosts(list, Constants.TABLE_NEARBY_POST);
	}

	public void getFollowedPosts(List<Map<String, Object>> list) {
		getPosts(list, Constants.TABLE_FOLLOWED_POST);
	}

	public void getRepliedPosts(List<Map<String, Object>> list) {
		getPosts(list, Constants.TABLE_REPLIED_POST);
	}

	private void getPosts(List<Map<String, Object>> list, String table) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryTableAll(table);
		while (cur.moveToNext()) {
			tmpList.add(MappingHelper.mapCursorToMap_Post(cur));
		}
		cur.close();

		replaceList(list, tmpList);
	}

	public void getFollowedPlaces(List<Map<String, Object>> list) {
		getPlaces(list, Constants.TABLE_FOLLOWED_PLACE);
	}

	public void getNearbyPlaces(List<Map<String, Object>> list) {
		getPlaces(list, Constants.TABLE_NEARBY_PLACE);
	}

	private void getPlaces(List<Map<String, Object>> list, String table) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryTableAll(table);
		while (cur.moveToNext()) {
			tmpList.add(MappingHelper.mapCursorToMap_Place(cur));
		}
		cur.close();

		replaceList(list, tmpList);
	}

	private void replaceList(List<Map<String, Object>> list, List<Map<String, Object>> tmpList) {
		if (tmpList.size() != 0) {
			list.clear();
			list.addAll(tmpList);
		} else {
			Log.w(Constants.LOG_TAG, LOG_EMPTY_LIST);
		}
	}

	private Cursor queryPlacePosts(String placeId) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Constants.TABLE_PLACE_POST);
		String selection = DBConstants.F_PLACEID + "=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = placeId;

		Cursor cursor = queryBuilder.query(getDatabase(), null, selection, selectionArgs, null, null, null);
		Log.d(Constants.LOG_TAG, "Query place posts from DB. PlaceId:" + placeId);
		return cursor;
	}

	private Cursor queryTableAll(String table) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(table);

		Cursor cursor = queryBuilder.query(getDatabase(), null, null, null, null, null, null);
		Log.d(Constants.LOG_TAG, "Query place list from table: " + table + ", with result: " + cursor);
		return cursor;
	}

	private SQLiteDatabase getDatabase() {
		if (writableDb == null) {
			writableDb = getWritableDatabase();
		}
		return writableDb;
	}
}
