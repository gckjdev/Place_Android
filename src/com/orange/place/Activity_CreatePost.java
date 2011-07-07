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

public class Activity_CreatePost extends Activity {

	private Button bPost;
	private EditText ePostContent;
	private TextView tErrorInfo;
	private String placeId;
	private String replyPostId;
	private String srcPostId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);

		placeId = getIntent().getStringExtra(DBConstants.F_PLACEID);
		replyPostId = getIntent().getStringExtra(DBConstants.F_REPLY_POSTID);
		srcPostId = getIntent().getStringExtra(DBConstants.F_SRC_POSTID);

		setContentView(R.layout.activity_new_post);
		lookupViewElements();
		setCreateListener();
	}

	private void setCreateListener() {
		bPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int resultCode = PostTask.createPost(Activity_CreatePost.this, placeId, ePostContent.getText().toString(),
						LocationUtil.getCurrentLocation(Activity_CreatePost.this), DBConstants.CONTENT_TYPE_TEXT, null,
						srcPostId, replyPostId);
				if (resultCode == ErrorCode.ERROR_SUCCESS) {
					Toast.makeText(Activity_CreatePost.this, getString(R.string.create_post_success), Toast.LENGTH_LONG)
							.show();
					Activity_CreatePost.this.finish();
				} else {
					tErrorInfo.setText("Create post failed, errorCode=" + resultCode);
				}
			}
		});
	}

	private void lookupViewElements() {
		bPost = (Button) findViewById(R.id.post);
		ePostContent = (EditText) findViewById(R.id.post_content);
		tErrorInfo = (TextView) findViewById(R.id.error_info);
	}
}
