package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
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
import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;
import com.orange.place.tasks.async.AsyncGetFollowedPlacesCallable;
import com.orange.place.tasks.async.AsyncGetFollowedPlacesTask;
import com.orange.place.tasks.async.AsyncGetNearbyPlacesCallable;
import com.orange.place.tasks.async.AsyncGetNearbyPlacesTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Main_Places extends BetterListActivity {

	private List<Map<String, Object>> places = new ArrayList<Map<String, Object>>();
	private SimpleAdapter placesAdapter;
	private ImageButton bCreatePlace;
	private Button bNearbyPlaces;
	private Button bFollowedPlaces;
	private TextView tMore;
	private String currentView;

	private static final String VIEW_NEARBY = "NearbyPlaces";
	private static final String VIEW_FOLLOWED = "FollowedPlaces";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_places);
		lookupViewElements();

		placesAdapter = new SimpleAdapter(this, places, R.layout.item_place, Constants.placeViewFrom,
				Constants.placeViewTo);
		setListAdapter(placesAdapter);

		showNearbyPlaces();

		setRefreshListener();
		setFollowedPlaceListener();
		setNearbyPlaceListener();
		setListItemListener();
		setCreatePlaceListener();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		refreshView();
	}
	
	private void showNearbyPlaces() {
		Log.d(Constants.LOG_TAG, "Start to show Nearby place");
		bNearbyPlaces.requestFocus();
		currentView = VIEW_NEARBY;
		places.clear();
		updateNearbyPlacesView();
		asyncGetNearbyPlaces();
	}

	private void showFollowedPlaces() {
		Log.d(Constants.LOG_TAG, "Start to show Followed place");
		bFollowedPlaces.requestFocus();
		currentView = VIEW_FOLLOWED;
		places.clear();
		updateFollowedPlacesView();
		asyncGetFollowedPlaces();
	}

	private void setCreatePlaceListener() {
		bCreatePlace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Activity_Main_Places.this, Activity_CreatePlace.class));
			}
		});
	}

	private void setListItemListener() {
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPlacePosts(position);
			}
		});
	}

	private void setNearbyPlaceListener() {
		bNearbyPlaces.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showNearbyPlaces();
					return true;
				}
				return false;
			}
		});
	}

	private void setFollowedPlaceListener() {
		bFollowedPlaces.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showFollowedPlaces();
					return true;
				}
				return false;
			}
		});
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
			asyncGetNearbyPlaces();
		} else if (VIEW_FOLLOWED.equals(currentView)) {
			asyncGetFollowedPlaces();
		}
	}

	private void asyncGetFollowedPlaces() {
		AsyncGetFollowedPlacesTask getFollowedPlacesTask = new AsyncGetFollowedPlacesTask(this);
		getFollowedPlacesTask.setCallable(new AsyncGetFollowedPlacesCallable(this));
		getFollowedPlacesTask.disableDialog();
		getFollowedPlacesTask.execute();
	}

	private void asyncGetNearbyPlaces() {
		AsyncGetNearbyPlacesTask getNearbyPlacesTask = new AsyncGetNearbyPlacesTask(this);
		getNearbyPlacesTask.setCallable(new AsyncGetNearbyPlacesCallable(this));
		getNearbyPlacesTask.disableDialog();
		getNearbyPlacesTask.setLocation(LocationUtil.getCurrentLocation(Activity_Main_Places.this));
		getNearbyPlacesTask.execute();
	}

	public void updateNearbyPlacesView() {
		PlaceTask.getNearbyPlacesFromDB(this, places);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + places);
		placesAdapter.notifyDataSetChanged();
	}

	public void updateFollowedPlacesView() {
		PlaceTask.getFollowedPlacesFromDB(this, places);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + places);
		placesAdapter.notifyDataSetChanged();
	}

	public void showPlacePosts(int position) {
		Map<String, Object> place = places.get(position);
		Intent intent = new Intent(Activity_Main_Places.this, Activity_PlacePosts.class);
		intent.putExtra(DBConstants.F_PLACEID, (String) place.get(DBConstants.F_PLACEID));
		intent.putExtra(DBConstants.F_NAME, (String) place.get(DBConstants.F_NAME));
		intent.putExtra(DBConstants.F_DESC, (String) place.get(DBConstants.F_DESC));
		startActivity(intent);
	}

	private void lookupViewElements() {
		tMore = (TextView) findViewById(R.id.more);
		bCreatePlace = (ImageButton) findViewById(R.id.place_new);
		bNearbyPlaces = (Button) findViewById(R.id.nearby_places);
		bFollowedPlaces = (Button) findViewById(R.id.followed_places);
	}
}