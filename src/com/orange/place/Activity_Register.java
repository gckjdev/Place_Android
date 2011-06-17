package com.orange.place;

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

import com.orange.place.common.Constants;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;
import com.orange.utils.ToastUtil;

public class Activity_Register extends Activity {

	private Button bSetLoginId;
	private Button bUseWeiboId;
	private Button bUseRenrenId;
	private EditText eLoginId;
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
				registerUserInfo();
				storeUserInfo(loginId);
				startMainActivity();
			}
		});
	}

	private void registerUserInfo() {
		createRegUrl();
		sendRequest();
	}

	private void sendRequest() {
		// TODO Auto-generated method stub
	}

	private void createRegUrl() {
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

		String url = b.build().toString();
		Log.d(Constants.LOG_TAG, "Created url for registration: " + url);
	}

	private void storeUserInfo(String loginId) {
		Log.d(Constants.LOG_TAG, "Set user input into preference, loginId = " + loginId);
		Editor editor = preference.edit();
		editor.putString(DBConstants.F_LOGINID, loginId);
		editor.commit();
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
		bSetLoginId = (Button) findViewById(R.id.set_login_id);

		bUseWeiboId = (Button) findViewById(R.id.use_weibo_id);
		bUseRenrenId = (Button) findViewById(R.id.use_renren_id);
		eLoginId = (EditText) findViewById(R.id.login_id);
	}
}
