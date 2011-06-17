package com.orange.place;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orange.place.common.Constants;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.utils.HttpUtils;
import com.orange.utils.ToastUtil;

public class Activity_Register extends Activity {

	private Button bSetLoginId;
	private Button bUseWeiboId;
	private Button bUseRenrenId;
	private EditText eLoginId;
	private TextView tRegErrorInfo;

	private String deviceId;
	private SharedPreferences preference;
	private TelephonyManager telephonyManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lookupResource();

		// if user before, skip this activity
		String loginId = preference.getString(DBConstants.F_LOGINID, null);
		Log.d(Constants.LOG_TAG, "Get from perference, loginId = " + loginId);
		if (loginId != null) {
			startMainActivity();
			return;
		}

		// init view
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		lookupViewElements();

		// set click listener
		setUserNameListener(bSetLoginId);
		tmpSetNotImpl(bUseWeiboId);
		tmpSetNotImpl(bUseRenrenId);
	}

	private void startMainActivity() {
		startActivity(new Intent(getApplication(), Activity_Main.class));
		Activity_Register.this.finish();
	}

	private void lookupResource() {
		preference = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = telephonyManager.getDeviceId();
	}

	private void setUserNameListener(Button b) {
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO validate the input
				String loginId = eLoginId.getText().toString();
				if (registerUser(loginId)) {
					startMainActivity();
				}
			}
		});
	}

	private boolean registerUser(String loginId) {
		String userId = null;
		boolean result = false;

		Uri regUri = createRegUri();
		Log.d(Constants.LOG_TAG, "Created uri for registration: " + regUri.toString());

		JSONObject respJson = HttpUtils.httpGet(regUri);
		Log.d(Constants.LOG_TAG, "Get Json response: " + respJson);
		if (respJson == null) {
			Log.e(Constants.LOG_TAG, "Get empty Json response, registration failed!");
			tRegErrorInfo.setText(R.string.reg_error_serverError);
			return result;
		}

		try {
			int returnCode = respJson.getInt(ServiceConstant.RET_CODE);
			if (returnCode == ErrorCode.ERROR_SUCCESS) {
				JSONObject returnData = respJson.getJSONObject(ServiceConstant.RET_DATA);
				userId = returnData.getString(ServiceConstant.PARA_USERID);
				result = true;
			} else if (returnCode == ErrorCode.ERROR_DEVICEID_BIND) {
				tRegErrorInfo.setText(R.string.reg_error_deviceIdBind);
			} else {
				tRegErrorInfo.setText(R.string.reg_error_unkown);
			}
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, "Error while handling registration result!", e);
		}

		if (result) {
			Log.d(Constants.LOG_TAG, "Set user info into preference, loginId = " + loginId + " userId:" + userId);
			Editor editor = preference.edit();
			editor.putString(DBConstants.F_LOGINID, loginId);
			editor.putString(DBConstants.F_USERID, userId);
			editor.commit();
		}

		return result;
	}

	private Uri createRegUri() {
		Uri.Builder b = Uri.parse(Constants.SERVER_HTTP).buildUpon();
		String loginId = eLoginId.getText().toString();

		b.path(Constants.CONTEXT_PATH);
		b.appendQueryParameter(ServiceConstant.METHOD, ServiceConstant.METHOD_REGISTRATION);
		b.appendQueryParameter(ServiceConstant.PARA_APPID, Constants.APP_NAME);
		b.appendQueryParameter(ServiceConstant.PARA_DEVICEMODEL, Build.MODEL);
		b.appendQueryParameter(ServiceConstant.PARA_DEVICEOS, Constants.OS + ":" + Build.VERSION.SDK_INT);
		b.appendQueryParameter(ServiceConstant.PARA_LOGINIDTYPE, String.valueOf(DBConstants.LOGINID_OWN));
		b.appendQueryParameter(ServiceConstant.PARA_LOGINID, loginId);
		b.appendQueryParameter(ServiceConstant.PARA_NICKNAME, loginId);
		b.appendQueryParameter(ServiceConstant.PARA_DEVICEID, deviceId);

		return b.build();
	}

	private void tmpSetNotImpl(Button b) {
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_Register.this);
			}
		});
	}

	private void lookupViewElements() {
		bSetLoginId = (Button) findViewById(R.id.reg_set_login_id);
		bUseWeiboId = (Button) findViewById(R.id.reg_use_weibo_id);
		bUseRenrenId = (Button) findViewById(R.id.reg_use_renren_id);
		eLoginId = (EditText) findViewById(R.id.reg_login_id);
		tRegErrorInfo = (TextView) findViewById(R.id.reg_error_info);
	}
}
