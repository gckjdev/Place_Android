package com.orange.place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orange.place.constant.ErrorCode;
import com.orange.place.tasks.UserTask;
import com.orange.utils.ActivityUtil;

public class Activity_Registration extends Activity {

	private Button bRegister;
	private Button bUseWeiboId;
	private Button bUseRenrenId;
	private TextView tRegErrorInfo;
	private EditText eLoginId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (UserTask.isRegistered(this)) {
			startMainActivity();
			this.finish();
			return;
		}

		// init view
		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_registration);
		lookupViewElements();
		setRegisterListener();
	}

	private void startMainActivity() {
		startActivity(new Intent(getApplication(), Activity_Main.class));
	}

	private void setRegisterListener() {
		bRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String loginId = eLoginId.getText().toString();
				// TODO validate the input

				int resultCode = UserTask.registerUser(Activity_Registration.this, loginId);
				if (resultCode == ErrorCode.ERROR_SUCCESS) {
					startMainActivity();
				} else if (resultCode == ErrorCode.ERROR_DEVICEID_BIND) {
					tRegErrorInfo.setText(R.string.reg_error_deviceIdBind);
				} else {
					tRegErrorInfo.setText(R.string.reg_error_unkown);
				}
			}
		});
	}

	private void lookupViewElements() {
		bRegister = (Button) findViewById(R.id.register);
		bUseWeiboId = (Button) findViewById(R.id.reg_use_weibo_id);
		bUseRenrenId = (Button) findViewById(R.id.reg_use_renren_id);
		eLoginId = (EditText) findViewById(R.id.reg_login_id);
		tRegErrorInfo = (TextView) findViewById(R.id.reg_error_info);
	}
}
