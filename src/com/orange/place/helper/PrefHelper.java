package com.orange.place.helper;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.constants.Constants;
import com.orange.utils.JsonUtil;

public class PrefHelper {

	public static String getUserLoginId(Context context) {
		return getPreference(context).getString(DBConstants.F_LOGINID, null);
	}
	
	public static String getUserId(Context context){
		return getPreference(context).getString(DBConstants.F_USERID, null);
	}

	public static SharedPreferences getPreference(Context context) {
		return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
	}

	public static void storeUserInfo(Context context, JSONObject data) {
		Log.d(Constants.LOG_TAG, "Set user info in JSON into preference.");
		if (data == null) {
			Log.e(Constants.LOG_TAG, "Data is null, nothing stored!");
			return;
		}

		Editor editor = getPreference(context).edit();
		editor.putString(DBConstants.F_USERID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_USERID));
		editor.putString(DBConstants.F_NICKNAME, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_NICKNAME));
		editor.putString(DBConstants.F_LOGINID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_LOGINID));
		editor.putString(DBConstants.F_SINA_ACCESS_TOKEN,
				JsonUtil.getStringOrNull(data, ServiceConstant.PARA_SINA_ACCESS_TOKEN));
		editor.putString(DBConstants.F_SINA_ACCESS_TOKEN_SECRET,
				JsonUtil.getStringOrNull(data, ServiceConstant.PARA_SINA_ACCESS_TOKEN_SECRET));
		editor.putString(DBConstants.F_QQ_ACCESS_TOKEN,
				JsonUtil.getStringOrNull(data, ServiceConstant.PARA_QQ_ACCESS_TOKEN));
		editor.putString(DBConstants.F_QQ_ACCESS_TOKEN_SECRET,
				JsonUtil.getStringOrNull(data, ServiceConstant.PARA_QQ_ACCESS_TOKEN_SECRET));
		editor.putString(DBConstants.F_QQID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_QQID));
		editor.putString(DBConstants.F_SINAID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_SINAID));
		editor.putString(DBConstants.F_RENRENID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_RENRENID));
		editor.putString(DBConstants.F_FACEBOOKID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_FACEBOOKID));
		editor.putString(DBConstants.F_TWITTERID, JsonUtil.getStringOrNull(data, ServiceConstant.PARA_TWITTERID));

		editor.commit();
	}
}
