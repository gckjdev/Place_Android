package com.orange.place.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.helper.JsonHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.place.helper.UriHelper;
import com.orange.utils.HttpUtils;

public class PlaceTask {
	public static int getPlaceList(Context context, Location location) {
		double longtitude;
		double latitude;
		if (location == null) {
			Log.e(Constants.LOG_TAG, "The location for getting place list is null!");
			// TODO: what to do if can not get location?
			longtitude = 0;
			latitude = 0;
		} else {
			longtitude = location.getLongitude();
			latitude = location.getLatitude();
		}

		Uri uri = UriHelper.createGetNearbyPlaceUri(PrefHelper.getUserId(context), longtitude, latitude);
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			// TODO, not create the helper every time?
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context);
			sqlLiteHelper.storePlaceList(context, JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}
}
