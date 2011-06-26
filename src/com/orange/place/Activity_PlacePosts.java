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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.droidfu.activities.BetterListActivity;
import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.ToastUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_PlacePosts extends BetterListActivity {

	private List<Map<String, Object>> placePosts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter placePostsAdapter;
	private Button bGoBack;
	private TextView tRefresh;
	private TextView tPlaceName;
	private TextView tPlaceDesc;
	private String placeId;
	private String placeName;
	private String placeDesc;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		placeId = getIntent().getStringExtra(DBConstants.F_PLACEID);
		placeName = getIntent().getStringExtra(DBConstants.F_NAME);
		placeDesc = getIntent().getStringExtra(DBConstants.F_DESC);
		Log.d(Constants.LOG_TAG, "Start to show place posts with placeId: " + placeId);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_place_posts);
		lookupAndSetViewElements();

		// firstly show what we have in DB
		PlaceTask.getPlacePostsFromDB(this, placePosts, placeId);
		String[] placePostsFrom = new String[] { "UserImage", DBConstants.F_TEXT_CONTENT, DBConstants.F_USERID,
				DBConstants.F_CREATE_DATE, DBConstants.C_TOTAL_RELATED };
		int[] placePostsTo = new int[] { R.id.user_image, R.id.post_content, R.id.user_id, R.id.post_time,
				R.id.post_related };
		placePostsAdapter = new SimpleAdapter(this, placePosts, R.layout.item_post, placePostsFrom,
				placePostsTo);
		setListAdapter(placePostsAdapter);

		// then get newest posts async
		asyncGetPlacePosts();

		setRefreshListener();
		setListItemListener();
		setGoBackListener();
	}

	private void setGoBackListener() {
		bGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_PlacePosts.this);
			}
		});
	}

	private void setListItemListener() {
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPlacePostDetail(position);
			}
		});
	}

	private void setRefreshListener() {
		tRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetPlacePosts();
			}
		});
	}

	private void asyncGetPlacePosts() {
		AsyncGetPlacePostsTask getPlacePostsTask = new AsyncGetPlacePostsTask(this);
		getPlacePostsTask.setCallable(new AsyncGetPlacePostsCallable());
		getPlacePostsTask.disableDialog();
		getPlacePostsTask.setPlaceId(placeId);
		getPlacePostsTask.execute();
	}

	private void updatePlacePostsView() {
		PlaceTask.getPlacePostsFromDB(this, placePosts, placeId);
		Log.d(Constants.LOG_TAG, "Updating place post list view with: " + placePosts);
		placePostsAdapter.notifyDataSetChanged();
	}
	
	private class AsyncGetPlacePostsTask extends BetterAsyncTask<Void, Void, Integer> {
		private String placeId;

		public AsyncGetPlacePostsTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_PlacePosts) context).updatePlacePostsView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}

		public String getPlaceId() {
			return placeId;
		}

		public void setPlaceId(String placeId) {
			this.placeId = placeId;
		}
	}

	private class AsyncGetPlacePostsCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			String placeId = ((AsyncGetPlacePostsTask) task).getPlaceId();
			resultCode = PlaceTask.getPlacePostsFromServer(Activity_PlacePosts.this, placeId);
			return resultCode;
		}
	}

	public void showPlacePostDetail(int position) {
		Map<String, Object> post = placePosts.get(position);
		Intent intent = new Intent(Activity_PlacePosts.this, Activity_PostDetail.class);
		intent.putExtra(DBConstants.F_POSTID, (String) post.get(DBConstants.F_POSTID));
		intent.putExtra(DBConstants.F_TEXT_CONTENT, (String) post.get(DBConstants.F_TEXT_CONTENT));
		intent.putExtra(DBConstants.C_TOTAL_RELATED, (String) post.get(DBConstants.C_TOTAL_RELATED));
		intent.putExtra(DBConstants.F_CREATE_DATE, (String) post.get(DBConstants.F_CREATE_DATE));
		intent.putExtra("UserImage", (Integer) post.get("UserImage"));
		startActivity(intent);
	}

	private void lookupAndSetViewElements() {
		bGoBack = (Button) findViewById(R.id.go_back);
		tRefresh = (TextView) findViewById(R.id.refresh);
		tPlaceName = (TextView) findViewById(R.id.place_name);
		tPlaceName.setText(placeName);
		tPlaceDesc = (TextView) findViewById(R.id.place_desc);
		tPlaceDesc.setText(placeDesc);
	}
}