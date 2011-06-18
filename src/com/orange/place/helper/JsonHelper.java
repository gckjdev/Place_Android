package com.orange.place.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.place.constant.ServiceConstant;
import com.orange.place.constants.Constants;

public class JsonHelper {
	public static int getResultCode(JSONObject json) {

		try {
			if (json == null || json.isNull(ServiceConstant.RET_CODE)) {
				return Constants.ERROR_RESP_NULL;
			}

			return json.getInt(ServiceConstant.RET_CODE);
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, "Can not get returnCode from JSON!", e);
			return Constants.ERROR_RESP_PARSE;
		}
	}

	public static JSONObject getReturnData(JSONObject json) {
		try {
			if (json == null || json.isNull(ServiceConstant.RET_DATA)) {
				return null;
			}

			return json.getJSONObject(ServiceConstant.RET_DATA);
		} catch (JSONException e) {
			return null;
		}
	}
}