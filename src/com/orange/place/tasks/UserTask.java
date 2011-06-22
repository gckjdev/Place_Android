package com.orange.place.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

import com.orange.place.constant.ErrorCode;
import com.orange.place.helper.JsonHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.helper.UriHelper;
import com.orange.utils.HttpUtils;
import com.orange.utils.SystemUtil;

public class UserTask {

	public static int deviceLogin(Context context) {
		Uri deviceLoginUri = UriHelper.createDeviceLoginUri(SystemUtil.getDeviceId(context));
		return requestAndStoreUserInfo(context, deviceLoginUri);
	}

	public static int registerUser(Context context, String loginId) {
		Uri regUri = UriHelper.createRegUri(loginId, SystemUtil.getDeviceId(context));
		return requestAndStoreUserInfo(context, regUri);
	}

	public static boolean isRegistered(Context context) {

		if (PrefHelper.getUserLoginId(context) != null) {
			return true;
		} else if (deviceLogin(context) == ErrorCode.ERROR_SUCCESS) {
			return true;
		}
		// TODO, iphone client will always do UserService.deviceLogin (async), put it in MainActivity?

		return false;
	}

	private static int requestAndStoreUserInfo(Context context, Uri regUri) {
		JSONObject respJson = HttpUtils.httpGet(regUri);

		int resultCode = JsonHelper.getResultCode(respJson);
		if (resultCode == ErrorCode.ERROR_SUCCESS) {
			PrefHelper.storeUserInfo(context, JsonHelper.getReturnData(respJson));
		}

		return resultCode;
	}
}
