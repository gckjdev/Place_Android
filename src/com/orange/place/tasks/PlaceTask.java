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
	public static int getNearbyPlaceList(Context context, Location location) {
		if (location == null) {
			Log.e(Constants.LOG_TAG, "The location for getting place list is null, no request to server!");
			return Constants.ERROR_LOCATION_UNKNOWN;
		}

		Uri uri = UriHelper.createGetNearbyPlaceUri(PrefHelper.getUserId(context), location.getLongitude(),
				location.getLatitude());
		JSONObject json = HttpUtils.httpGet(uri);

		int resultCode = JsonHelper.getResultCode(json);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(context); // improve: not create the helper every time?
			sqlLiteHelper.storeNearbyPlaceList(JsonHelper.getReturnDataArray(json));
		}

		return resultCode;
	}
}
