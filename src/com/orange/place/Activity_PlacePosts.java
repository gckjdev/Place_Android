package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidfu.activities.BetterListActivity;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;
import com.orange.place.tasks.PostTask;
import com.orange.place.tasks.async.AsyncGetFollowedPlacesCallable;
import com.orange.place.tasks.async.AsyncGetFollowedPlacesTask;
import com.orange.place.tasks.async.AsyncGetPlacePostsCallable;
import com.orange.place.tasks.async.AsyncGetPlacePostsTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.ToastUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_PlacePosts extends BetterListActivity {

	private List<Map<String, Object>> placePosts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter placePostsAdapter;
	private Button bGoBack;
	private Button bFollow;
	private ImageButton bCreatePost;
	private TextView tRefresh;
	private TextView tPlaceName;
	private TextView tPlaceDesc;
	private String placeId;
	private String placeName;
	private String placeDesc;
	private boolean userFollowedPlace;

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
		PostTask.getPlacePostsFromDB(this, placePosts, placeId);
		placePostsAdapter = new SimpleAdapter(this, placePosts, R.layout.item_post, Constants.postsViewFrom,
				Constants.postsViewTo);
		setListAdapter(placePostsAdapter);

		updateFollowButton(); // first try use the DB
		asyncGetFollowedPlaces(); // then use latest info
		asyncGetPlacePosts();

		setRefreshListener();
		setListItemListener();
		setGoBackListener();
		setFollowListener();
		setCreatePostListener();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		asyncGetPlacePosts();
	}

	private void setCreatePostListener() {
		bCreatePost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_PlacePosts.this, Activity_CreatePost.class);
				intent.putExtra(DBConstants.F_PLACEID, placeId); // no srcPostId and replySrcId, as this is new post
				startActivity(intent);
			}
		});
	}

	private void setFollowListener() {
		bFollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int resultCode = Constants.ERROR_UNKOWN;
				if (userFollowedPlace) {
					resultCode = PlaceTask.unfollowPlace(Activity_PlacePosts.this, placeId);
				} else {
					resultCode = PlaceTask.followPlace(Activity_PlacePosts.this, placeId);
				}

				if (resultCode == ErrorCode.ERROR_SUCCESS) {
					asyncGetFollowedPlaces(); // this will trigger updateFollowButton()
				} else {
					Toast.makeText(Activity_PlacePosts.this, getString(R.string.op_fail_pls_retry), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void setGoBackListener() {
		bGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity_PlacePosts.this.finish();
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

	private void setRefreshListener() {
		tRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetPlacePosts();
			}
		});
	}

	public void updateFollowButton() {
		checkUserFollowedPlace();
		if (userFollowedPlace) {
//			bFollow.setText(getString(R.string.unfollow));
			bFollow.setVisibility(View.VISIBLE);
		} else {
			bFollow.setText(getString(R.string.follow));
			bFollow.setVisibility(View.VISIBLE);
		}
	}

	private void checkUserFollowedPlace() {
		List<Map<String, Object>> places = new ArrayList<Map<String, Object>>();
		PlaceTask.getFollowedPlacesFromDB(this, places);

		for (Map<String, Object> place : places) {
			if (placeId.equals(place.get(DBConstants.F_PLACEID))) {
				userFollowedPlace = true;
				return;
			}
		}
		userFollowedPlace = false;
	}

	private void asyncGetFollowedPlaces() {
		AsyncGetFollowedPlacesTask getFollowedPlacesTask = new AsyncGetFollowedPlacesTask(this);
		getFollowedPlacesTask.setCallable(new AsyncGetFollowedPlacesCallable(this));
		getFollowedPlacesTask.disableDialog();
		getFollowedPlacesTask.execute();
	}

	private void asyncGetPlacePosts() {
		AsyncGetPlacePostsTask getPlacePostsTask = new AsyncGetPlacePostsTask(this);
		getPlacePostsTask.setCallable(new AsyncGetPlacePostsCallable(this));
		getPlacePostsTask.disableDialog();
		getPlacePostsTask.setPlaceId(placeId);
		getPlacePostsTask.execute();
	}

	public void updatePlacePostsView() {
		PostTask.getPlacePostsFromDB(this, placePosts, placeId);
		Log.d(Constants.LOG_TAG, "Updating place post list view with: " + placePosts);
		placePostsAdapter.notifyDataSetChanged();
	}

	public void showPostDetail(int position) {
		Map<String, Object> post = placePosts.get(position);
		Intent intent = new Intent(Activity_PlacePosts.this, Activity_PostDetail.class);
		intent.putExtra(DBConstants.F_POSTID, (String) post.get(DBConstants.F_POSTID));
		intent.putExtra(DBConstants.F_PLACEID, (String) post.get(DBConstants.F_PLACEID));
		intent.putExtra(DBConstants.F_SRC_POSTID, (String) post.get(DBConstants.F_SRC_POSTID));
		intent.putExtra(DBConstants.F_TEXT_CONTENT, (String) post.get(DBConstants.F_TEXT_CONTENT));
		intent.putExtra(DBConstants.C_TOTAL_RELATED, (String) post.get(DBConstants.C_TOTAL_RELATED));
		intent.putExtra(DBConstants.F_CREATE_DATE, (String) post.get(DBConstants.F_CREATE_DATE));
		intent.putExtra("UserImage", (Integer) post.get("UserImage"));
		startActivity(intent);
	}

	private void lookupAndSetViewElements() {
		bGoBack = (Button) findViewById(R.id.go_back);
		bFollow = (Button) findViewById(R.id.follow);
		bCreatePost = (ImageButton) findViewById(R.id.post_new);
		tRefresh = (TextView) findViewById(R.id.refresh);
		tPlaceName = (TextView) findViewById(R.id.place_name);
		tPlaceName.setText(placeName);
		tPlaceDesc = (TextView) findViewById(R.id.place_desc);
		tPlaceDesc.setText(placeDesc);
		bFollow.setVisibility(View.INVISIBLE); // show it until know if user already followed or not
	}
}