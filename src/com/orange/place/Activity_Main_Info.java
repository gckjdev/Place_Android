package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.droidfu.activities.BetterListActivity;
import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PostTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;
import com.orange.utils.ToastUtil;

public class Activity_Main_Info extends BetterListActivity {
	private List<Map<String, Object>> posts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter postsAdapter;
	private ImageButton bNewPost;
	private Button bNearbyPosts;
	private Button bRepliedPosts;
	private Button bFollowedPosts;
	private TextView tMore;
	private String currentView;

	private static final String VIEW_NEARBY = "NearbyPosts";
	private static final String VIEW_FOLLOWED = "FollowedPosts";
	private static final String VIEW_REPLIED = "RepliedPosts";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_info);
		lookupViewElements();

		postsAdapter = new SimpleAdapter(this, posts, R.layout.item_post, Constants.postsViewFrom,
				Constants.postsViewTo);
		setListAdapter(postsAdapter);

		showNearbyPosts();

		setRefreshListener();
		setNearbyPostsListener();
		setFollowedPostsListener();
		setRepliedPostsListener();
		setListItemListener();
		setNewPostListener();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		refreshView();
	}

	private void showNearbyPosts() {
		Log.d(Constants.LOG_TAG, "Start to show Nearby posts");
		bNearbyPosts.requestFocus();
		currentView = VIEW_NEARBY;
		posts.clear();
		updateNearbyPostsView();
		asyncGetNearbyPosts();
	}

	private void showFollowedPosts() {
		Log.d(Constants.LOG_TAG, "Start to show Followed posts");
		bFollowedPosts.requestFocus();
		currentView = VIEW_FOLLOWED;
		posts.clear();
		updateFollowedPostsView();
		asyncGetFollowedPosts();
	}

	private void showRepliedPosts() {
		Log.d(Constants.LOG_TAG, "Start to show Replied posts");
		bRepliedPosts.requestFocus();
		currentView = VIEW_REPLIED;
		posts.clear();
		updateRepliedPostsView();
		asyncGetRepliedPosts();
	}

	private void setRefreshListener() {
		tMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshView();
			}
		});
	}
	
	private void refreshView() {
		if (VIEW_NEARBY.equals(currentView)) {
			asyncGetNearbyPosts();
		} else if (VIEW_FOLLOWED.equals(currentView)) {
			asyncGetFollowedPosts();
		} else if (VIEW_REPLIED.equals(currentView)) {
			asyncGetRepliedPosts();
		}
	}

	private void setNewPostListener() {
		bNewPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_Main_Info.this);
			}
		});
	}

	private void setListItemListener() {
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPostDetail(position);
			}
		});
	}

	public void showPostDetail(int position) {
		Map<String, Object> post = posts.get(position);
		Intent intent = new Intent(Activity_Main_Info.this, Activity_PostDetail.class);
		intent.putExtra(DBConstants.F_POSTID, (String) post.get(DBConstants.F_POSTID));
		intent.putExtra(DBConstants.F_TEXT_CONTENT, (String) post.get(DBConstants.F_TEXT_CONTENT));
		intent.putExtra(DBConstants.C_TOTAL_RELATED, (String) post.get(DBConstants.C_TOTAL_RELATED));
		intent.putExtra(DBConstants.F_CREATE_DATE, (String) post.get(DBConstants.F_CREATE_DATE));
		intent.putExtra("UserImage", (Integer) post.get("UserImage"));
		startActivity(intent);
	}

	private void setNearbyPostsListener() {
		bNearbyPosts.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showNearbyPosts();
					return true;
				}
				return false;
			}
		});
	}

	private void setFollowedPostsListener() {
		bFollowedPosts.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showFollowedPosts();
					return true;
				}
				return false;
			}
		});
	}

	private void setRepliedPostsListener() {
		bRepliedPosts.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showRepliedPosts();
					return true;
				}
				return false;
			}
		});
	}

	private void asyncGetNearbyPosts() {
		AsyncGetNearbyPostsTask getNearbyPostsTask = new AsyncGetNearbyPostsTask(this);
		getNearbyPostsTask.setCallable(new AsyncGetNearbyPostsCallable());
		getNearbyPostsTask.disableDialog();
		getNearbyPostsTask.setLocation(LocationUtil.getCurrentLocation(Activity_Main_Info.this));
		getNearbyPostsTask.execute();
	}

	private void asyncGetFollowedPosts() {
		AsyncGetFollowedPostsTask getFollowedPostsTask = new AsyncGetFollowedPostsTask(this);
		getFollowedPostsTask.setCallable(new AsyncGetFollowedPostsCallable());
		getFollowedPostsTask.disableDialog();
		getFollowedPostsTask.execute();
	}

	private void asyncGetRepliedPosts() {
		AsyncGetRepliedPostsTask getRepliedPostsTask = new AsyncGetRepliedPostsTask(this);
		getRepliedPostsTask.setCallable(new AsyncGetRepliedPostsCallable());
		getRepliedPostsTask.disableDialog();
		getRepliedPostsTask.execute();
	}

	private class AsyncGetNearbyPostsTask extends BetterAsyncTask<Void, Void, Integer> {
		private Location location;

		public AsyncGetNearbyPostsTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Main_Info) context).updateNearbyPostsView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
	}

	private class AsyncGetFollowedPostsTask extends BetterAsyncTask<Void, Void, Integer> {
		public AsyncGetFollowedPostsTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Main_Info) context).updateFollowedPostsView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}
	}

	private class AsyncGetRepliedPostsTask extends BetterAsyncTask<Void, Void, Integer> {
		public AsyncGetRepliedPostsTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Main_Info) context).updateRepliedPostsView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}
	}

	private class AsyncGetNearbyPostsCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			Location location = ((AsyncGetNearbyPostsTask) task).getLocation();
			resultCode = PostTask.getNearbyPostsFromServer(Activity_Main_Info.this, location);
			return resultCode;
		}
	}

	private class AsyncGetFollowedPostsCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			resultCode = PostTask.getFollowedPostsFromServer(Activity_Main_Info.this);
			return resultCode;
		}
	}

	private class AsyncGetRepliedPostsCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			resultCode = PostTask.getRepliedPostsFromServer(Activity_Main_Info.this);
			return resultCode;
		}
	}

	private void updateNearbyPostsView() {
		PostTask.getNearbyPostsFromDB(this, posts);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + posts);
		postsAdapter.notifyDataSetChanged();
	}

	private void updateFollowedPostsView() {
		PostTask.getFollowedPostsFromDB(this, posts);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + posts);
		postsAdapter.notifyDataSetChanged();
	}

	private void updateRepliedPostsView() {
		PostTask.getRepliedPostsFromDB(this, posts);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + posts);
		postsAdapter.notifyDataSetChanged();
	}

	private void lookupViewElements() {
		tMore = (TextView) findViewById(R.id.more);
		bNewPost = (ImageButton) findViewById(R.id.post_new);
		bNearbyPosts = (Button) findViewById(R.id.nearby_posts);
		bFollowedPosts = (Button) findViewById(R.id.followed_posts);
		bRepliedPosts = (Button) findViewById(R.id.replied_posts);
	}
}