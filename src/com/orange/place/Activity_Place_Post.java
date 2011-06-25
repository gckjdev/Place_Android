package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
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
public class Activity_Place_Post extends BetterListActivity {

	private List<Map<String, Object>> placePosts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter placePostsAdapter;
	private Button bGoBack;
	private TextView tMore;
	private String placeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		placeId = getIntent().getStringExtra(DBConstants.F_PLACEID);
		Log.d(Constants.LOG_TAG, "Start to show place post with ID: " + placeId);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_place_post);
		lookupViewElements();
		setRefreshListener();

		// firstly show what we have in DB
		PlaceTask.getPlacePostsFromDB(this, placePosts, placeId);
		String[] placePostsFrom = new String[] { "UserImage", DBConstants.F_TEXT_CONTENT, DBConstants.F_USERID };
		int[] placePostsTo = new int[] { R.id.UserImage, R.id.PostContent, R.id.UserId };
		placePostsAdapter = new SimpleAdapter(this, placePosts, R.layout.list_place_post_item, placePostsFrom, placePostsTo);
		setListAdapter(placePostsAdapter);

		// then get newest posts async
		asyncGetPlacePosts();
		
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPlacePostDetail(position);
			}
		});

		bGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_Place_Post.this);
			}
		});
	}

	private void setRefreshListener() {
		tMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetPlacePosts();
			}
		});
	}

	private void asyncGetPlacePosts() {
		AsyncGetPlacePostsTask getPlacePostsTask = new AsyncGetPlacePostsTask(this);
		getPlacePostsTask.setCallable(new AsyncGetPlaceListCallable());
		getPlacePostsTask.disableDialog();
		getPlacePostsTask.setPlaceId(placeId);
		getPlacePostsTask.execute();
	}

	private void updatePlacePostsView() {
		PlaceTask.getPlacePostsFromDB(this, placePosts, placeId);
		Log.w(Constants.LOG_TAG, "Updating place list view with place list: " + placePosts);
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
				((Activity_Place_Post) context).updatePlacePostsView();
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

	private class AsyncGetPlaceListCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			String placeId = ((AsyncGetPlacePostsTask) task).getPlaceId();
			resultCode = PlaceTask.getPlacePostsFromServer(Activity_Place_Post.this, placeId);
			return resultCode;
		}
	}

	public void showPlacePostDetail(int position) {
		Map<String, Object> place = placePosts.get(position);
		new AlertDialog.Builder(this).setTitle("Self defined ListVeiw")
				.setMessage("you clicked the place post:" + place.get(DBConstants.F_POSTID))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whButton) {
					}
				}).show();
	}

	private void lookupViewElements() {
		bGoBack = (Button) findViewById(R.id.go_back);
		tMore = (TextView) findViewById(R.id.refresh);
	}
}