package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;
import com.orange.utils.ToastUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Place extends BetterListActivity {

	private List<Map<String, Object>> nearbyPlaces = new ArrayList<Map<String, Object>>();
	private SimpleAdapter nearbyPlacesAdapter;
	private ImageButton bNewPlace;
	private TextView tMore;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_place);
		lookupViewElements();
		setRefreshListener();

		// firstly show what we have in DB
		PlaceTask.getNearbyPlacesFromDB(this, nearbyPlaces);
		String[] placeListFrom = new String[] { "PlaceImage", DBConstants.F_NAME, DBConstants.F_DESC,
				DBConstants.F_USERID };
		int[] placeListTo = new int[] { R.id.PlaceImage, R.id.PlaceName, R.id.PlaceDesc, R.id.UserId };
		nearbyPlacesAdapter = new SimpleAdapter(this, nearbyPlaces, R.layout.list_place_item, placeListFrom,
				placeListTo);
		setListAdapter(nearbyPlacesAdapter);

		// then, try get newest place list async
		asyncGetNearbyPlaces();

		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPlacePosts(position);
			}
		});
		
		bNewPlace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_Place.this);
			}
		});
	}

	private void setRefreshListener() {
		tMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetNearbyPlaces();
			}
		});
	}

	private void asyncGetNearbyPlaces() {
		AsyncGetNearbyPlacesTask getNearbyPlacesTask = new AsyncGetNearbyPlacesTask(this);
		getNearbyPlacesTask.setCallable(new AsyncGetNearbyPlacesCallable());
		getNearbyPlacesTask.disableDialog();
		getNearbyPlacesTask.setLocation(LocationUtil.getCurrentLocation(Activity_Place.this));
		getNearbyPlacesTask.execute();
	}

	private void updateNearbyPlacesView() {
		PlaceTask.getNearbyPlacesFromDB(this, nearbyPlaces);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + nearbyPlaces);
		nearbyPlacesAdapter.notifyDataSetChanged();
	}

	private class AsyncGetNearbyPlacesTask extends BetterAsyncTask<Void, Void, Integer> {
		private Location location;

		public AsyncGetNearbyPlacesTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Place) context).updateNearbyPlacesView();
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

	private class AsyncGetNearbyPlacesCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			Location location = ((AsyncGetNearbyPlacesTask) task).getLocation();
			resultCode = PlaceTask.getNearbyPlacesFromServer(Activity_Place.this, location);
			return resultCode;
		}
	}

	public void showPlacePosts(int position) {
		Map<String, Object> place = nearbyPlaces.get(position);
		Intent intent = new Intent(Activity_Place.this, Activity_Place_Post.class);
		intent.putExtra(DBConstants.F_PLACEID, (String) place.get(DBConstants.F_PLACEID));
		startActivity(intent);
	}

	private void lookupViewElements() {
		bNewPlace = (ImageButton) findViewById(R.id.place_new);
		tMore = (TextView) findViewById(R.id.refresh);
	}
}