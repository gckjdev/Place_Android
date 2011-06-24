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

	private static final String SQL_CLEANUP_NEARBY_PLACE = "DELETE FROM " + Constants.TABLE_NEARBY_PLACE + ";";
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

	private static SQLiteDatabase writableDb = null;

	public SqlLiteHelper(Context context) {
		super(context, Constants.SQLITE_DB_NAME, null, Constants.SQLITE_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(Constants.LOG_TAG, "Creating database");
		try {
			db.execSQL(SqlLiteHelper.SQL_CREATE_NEARBY_PLACE);
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

	public int storeNearbyPlaceList(JSONArray jsonArr) {
		int len = jsonArr.length();
		if (len <= 0 ) {
			Log.w(Constants.LOG_TAG, "No data found for DB store, will just ignore!");
			return Constants.ERROR_RESP_DATA_EMPTY;
		}
		
		ContentValues cv = null;
		cleanupNearbyPlace(); // only keep the latest data
		for (int i = 0; i < len; i++) {
			try {
				cv = createCVForPlace((JSONObject) jsonArr.get(i));
				getDatabase().insert(Constants.TABLE_NEARBY_PLACE, null, cv);
			} catch (JSONException e) {
				Log.e(Constants.LOG_TAG, "Storing data error!", e);
				return Constants.ERROR_SQLITE;
			} 
		}
		return Constants.ERROR_UNKOWN;
	}

	public void cleanupNearbyPlace() {
		Log.d(Constants.LOG_TAG, "Cleanup nearby places in DB.");
		getDatabase().execSQL(SQL_CLEANUP_NEARBY_PLACE);
	}

	public void updatePlaceList(List<Map<String, Object>> list) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();

		Cursor cur = queryNearbyPlace();
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

	private Cursor queryNearbyPlace() {
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
}
