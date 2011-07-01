package com.orange.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.tasks.PostTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;

public class Activity_NewPost extends Activity {

	private Button bCreate;
	private EditText ePostContent;
	private TextView tErrorInfo;
	private String placeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);

		placeId = getIntent().getStringExtra(DBConstants.F_PLACEID);

		setContentView(R.layout.activity_new_post);
		lookupViewElements();
		setCreateListener();
	}

	private void setCreateListener() {
		bCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int resultCode = PostTask.newPost(Activity_NewPost.this, placeId, ePostContent.getText().toString(),
						LocationUtil.getCurrentLocation(Activity_NewPost.this), DBConstants.CONTENT_TYPE_TEXT, null,
						null, null);
				if (resultCode == ErrorCode.ERROR_SUCCESS) {
					Toast.makeText(Activity_NewPost.this, getString(R.string.create_post_success), Toast.LENGTH_LONG)
							.show();
					Activity_NewPost.this.finish();
				} else {
					tErrorInfo.setText("Create post failed, errorCode=" + resultCode);
				}
			}
		});
	}

	private void lookupViewElements() {
		bCreate = (Button) findViewById(R.id.create);
		ePostContent = (EditText) findViewById(R.id.post_content);
		tErrorInfo = (TextView) findViewById(R.id.error_info);
	}
}
