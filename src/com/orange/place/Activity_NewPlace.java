package com.orange.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orange.place.constant.ErrorCode;
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;

public class Activity_NewPlace extends Activity {

	private Button bCreate;
	private EditText ePlaceName;
	private EditText ePlaceDesc;
	private TextView tErrorInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);

		setContentView(R.layout.activity_new_place);
		lookupViewElements();
		setCreateListener();
	}

	private void setCreateListener() {
		bCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int resultCode = PlaceTask.newPlace(Activity_NewPlace.this, ePlaceName.getText().toString(), ePlaceDesc
						.getText().toString(), LocationUtil.getCurrentLocation(Activity_NewPlace.this), 100, "0");
				if (resultCode == ErrorCode.ERROR_SUCCESS) {
					Toast.makeText(Activity_NewPlace.this, getString(R.string.create_place_success), Toast.LENGTH_LONG)
							.show();
					Activity_NewPlace.this.finish();
				} else {
					tErrorInfo.setText("Create place failed, errorCode=" + resultCode);
				}
			}
		});
	}

	private void lookupViewElements() {
		bCreate = (Button) findViewById(R.id.create);
		ePlaceName = (EditText) findViewById(R.id.place_name);
		ePlaceDesc = (EditText) findViewById(R.id.place_desc);
		tErrorInfo = (TextView) findViewById(R.id.error_info);
	}
}
