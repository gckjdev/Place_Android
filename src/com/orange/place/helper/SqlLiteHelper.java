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
import com.orange.place.constants.Constants;

public class SqlLiteHelper extends SQLiteOpenHelper {

	private static final String LOG_EMPTY_LIST = "Get an empty list, will not clear and update old list";
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
			db.execSQL(SqlLiteMappingHelper.SQL_CREATE_NEARBY_PLACE);
			db.execSQL(SqlLiteMappingHelper.SQL_CREATE_PLACE_POST);
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
				cv = SqlLiteMappingHelper.mapJsonToCV_Post((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_PLACE_POST, null, cv);
			} catch (Exception e) {
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
				cv = SqlLiteMappingHelper.mapJsonToCV_Place((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_NEARBY_PLACE, null, cv);
			} catch (Exception e) {
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

	public void getPlacePosts(List<Map<String, Object>> list, String placeId) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryPlacePosts(placeId);
		while (cur.moveToNext()) {
			tmpList.add(SqlLiteMappingHelper.mapCursorToMap_Post(cur));
		}
		cur.close();

		checkAndUpdateList(list, tmpList);
	}

	public void getNearbyPlaces(List<Map<String, Object>> list) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		Cursor cur = queryNearbyPlaces();
		while (cur.moveToNext()) {
			tmpList.add(SqlLiteMappingHelper.mapCursorToMap_Place(cur));
		}
		cur.close();

		checkAndUpdateList(list, tmpList);
	}

	private void checkAndUpdateList(List<Map<String, Object>> list, List<Map<String, Object>> tmpList) {
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

	private Cursor queryNearbyPlaces() {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Constants.TABLE_NEARBY_PLACE);

		Cursor cursor = queryBuilder.query(getDatabase(), null, null, null, null, null, null);
		Log.d(Constants.LOG_TAG, "Query place list from DB. Amount: " + cursor.getCount());
		return cursor;
	}

	private SQLiteDatabase getDatabase() {
		if (writableDb == null) {
			writableDb = getWritableDatabase();
		}
		return writableDb;
	}
}
