package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.droidfu.activities.BetterListActivity;
import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.helper.GlobalVarHelper;
import com.orange.place.helper.PrefHelper;
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_PostDetail extends BetterListActivity {

	private List<Map<String, Object>> relatedPosts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter relatedPostAdapter;
	private Button bGoBack;
	private Button bReply;
	private TextView tRefresh;
	private TextView tPostContent;
	private TextView tPostTime;
	private TextView tPostRelated;
	private TextView tUserId;
	private ImageView iUserImage;
	private String placeId;
	private String postId;
	private String srcPostId;
	private String postContent;
	private String postTime;
	private String postRelatedAmount;
	private int userImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		postId = getIntent().getStringExtra(DBConstants.F_POSTID);
		placeId = getIntent().getStringExtra(DBConstants.F_PLACEID);
		srcPostId = getIntent().getStringExtra(DBConstants.F_SRC_POSTID);
		postContent = getIntent().getStringExtra(DBConstants.F_TEXT_CONTENT);
		postRelatedAmount = getIntent().getStringExtra(DBConstants.C_TOTAL_RELATED);
		postTime = getIntent().getStringExtra(DBConstants.F_CREATE_DATE);
		userImage = getIntent().getIntExtra("UserImage", R.drawable.main_tab_1_nav);
		Log.d(Constants.LOG_TAG, "Start to show posts detail with postId: " + postId);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_post_detail);
		lookupAndSetViewElements();

		// related post will not store in DB, always get from server
		asyncGetRelatedPosts();

		String[] placePostsFrom = new String[] { "UserImage", DBConstants.F_TEXT_CONTENT, DBConstants.F_USERID,
				DBConstants.F_CREATE_DATE, DBConstants.C_TOTAL_RELATED };
		int[] placePostsTo = new int[] { R.id.user_image, R.id.post_content, R.id.user_id, R.id.post_time,
				R.id.post_related };
		relatedPostAdapter = new SimpleAdapter(this, relatedPosts, R.layout.item_post, placePostsFrom, placePostsTo);
		setListAdapter(relatedPostAdapter);

		setRefreshListener();
		setListItemListener();
		setGoBackListener();
		setReplyListener();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		asyncGetRelatedPosts();
	}

	private void setGoBackListener() {
		bGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity_PostDetail.this.finish();
			}
		});
	}

	private void setReplyListener() {
		bReply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_PostDetail.this, Activity_CreatePost.class);
				intent.putExtra(DBConstants.F_PLACEID, placeId);
				intent.putExtra(DBConstants.F_SRC_POSTID, srcPostId);
				intent.putExtra(DBConstants.F_REPLY_POSTID, postId);
				startActivity(intent);
			}
		});
	}

	private void setListItemListener() {
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// do nothing, as already in the post detail
			}
		});
	}

	private void setRefreshListener() {
		tRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetRelatedPosts();
			}
		});
	}

	private void asyncGetRelatedPosts() {
		AsyncGetRelatedPostsTask getRelatedPostsTask = new AsyncGetRelatedPostsTask(this);
		getRelatedPostsTask.setCallable(new AsyncGetRelatedPostsCallable());
		getRelatedPostsTask.disableDialog();
		getRelatedPostsTask.setPostId(postId);
		getRelatedPostsTask.execute();
	}

	private void updateRelatedPostsView() {
		List<Map<String, Object>> posts = GlobalVarHelper.getRelatedPosts(postId);
		if (posts == null || posts.size() <= 0) {
			Log.w(Constants.LOG_TAG, "related post in global var is empty , will not update view!");
			return;
		}
		relatedPosts.clear();
		relatedPosts.addAll(posts);
		Log.d(Constants.LOG_TAG, "Updating place post list view with: " + relatedPosts);
		relatedPostAdapter.notifyDataSetChanged();
	}

	private class AsyncGetRelatedPostsTask extends BetterAsyncTask<Void, Void, Integer> {
		private String postId;

		public AsyncGetRelatedPostsTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_PostDetail) context).updateRelatedPostsView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}

		public String getPostId() {
			return postId;
		}

		public void setPostId(String placeId) {
			this.postId = placeId;
		}
	}

	private class AsyncGetRelatedPostsCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			String postId = ((AsyncGetRelatedPostsTask) task).getPostId();
			resultCode = PlaceTask.getRelatedPostsFromServer(Activity_PostDetail.this, postId);
			return resultCode;
		}
	}
	
	private void lookupAndSetViewElements() {
		bGoBack = (Button) findViewById(R.id.go_back);
		tRefresh = (TextView) findViewById(R.id.refresh);
		bReply = (Button) findViewById(R.id.reply);
		// R.id.user_image, R.id.post_content, R.id.user_id, R.id.post_time,R.id.post_related
		tPostContent = (TextView) findViewById(R.id.post_content);
		tPostContent.setText(postContent);
		tPostTime = (TextView) findViewById(R.id.post_time);
		tPostTime.setText(postTime);
		tPostRelated = (TextView) findViewById(R.id.post_related);
		tPostRelated.setText(postRelatedAmount);
		tUserId = (TextView) findViewById(R.id.user_id);
		tUserId.setText(PrefHelper.getUserId(this));
		iUserImage = (ImageView) findViewById(R.id.user_image);
		iUserImage.setImageResource(userImage);
	}
}