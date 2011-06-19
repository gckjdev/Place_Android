package com.orange.place.services;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.orange.place.constant.ErrorCode;
import com.orange.place.helper.JsonHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.place.helper.UriHelper;
import com.orange.utils.HttpUtils;

public class PlaceService {
	public static int getPlaceList(Context context, double longtitude, double latitude) {
		Uri uri = UriHelper.createGetNearbyPlaceUri(PrefHelper.getUserId(context), longtitude, latitude);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			// TODO, refactor to not create every time
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
			sqlLiteHelper.storePlaceList(context, JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}
}
