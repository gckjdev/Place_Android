package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.LocationUtil;
import com.orange.utils.ToastUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Places extends BetterListActivity {

	private List<Map<String, Object>> places = new ArrayList<Map<String, Object>>();
	private SimpleAdapter placesAdapter;
	private ImageButton bNewPlace;
	private Button bNearbyPlace;
	private Button bFollowedPlace;
	private TextView tMore;
	private String[] placeListFrom = new String[] { DBConstants.F_NAME, DBConstants.F_DESC, DBConstants.F_USERID };
	private int[] placeListTo = new int[] { R.id.place_name, R.id.place_desc, R.id.user_id };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);

		setContentView(R.layout.activity_places);
		lookupViewElements();

		placesAdapter = new SimpleAdapter(this, places, R.layout.item_place, placeListFrom, placeListTo);
		setListAdapter(placesAdapter);

		showNearbyPlaces();

		setRefreshListener();
		setFollowedPlaceListener();
		setNearbyPlaceListener();
		setListItemListener();
		setNewPlaceListener();
	}

	private void showNearbyPlaces() {
		Log.d(Constants.LOG_TAG, "Start to show Nearby place");

		// firstly show what we have in DB
		places.clear();
		updateNearbyPlacesView();

		// then, try get newest place list async
		asyncGetNearbyPlaces();
	}

	private void showFollowedPlaces() {
		Log.d(Constants.LOG_TAG, "Start to show Followed place");

		// firstly show what we have in DB
		places.clear();
		updateFollowedPlacesView();

		// then, try get followed place list async
		asyncGetFollowedPlaces();
	}

	private void setNewPlaceListener() {
		bNewPlace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.makeNotImplToast(Activity_Places.this);
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

	private void setFollowedPlaceListener() {
		bFollowedPlace.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
                	showFollowedPlaces();
                    return true;
                }
                return false;
			}
		});
	}

	private void setNearbyPlaceListener() {
		bNearbyPlace.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
                	showNearbyPlaces();
                    return true;
                }
                return false;
			}
		});
	}

	// private void setNearbyPlaceListener() {
	// bNearbyPlace.setOnClickListener(new Button.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Log.d(Constants.LOG_TAG, "User select to show nearby place");
	// showNearbyPlaces();
	// }
	// });
	// }

	private void setRefreshListener() {
		tMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asyncGetNearbyPlaces();
			}
		});
	}

	private void asyncGetFollowedPlaces() {
		AsyncGetFollowedPlacesTask getFollowedPlacesTask = new AsyncGetFollowedPlacesTask(this);
		getFollowedPlacesTask.setCallable(new AsyncGetFollowedPlacesCallable());
		getFollowedPlacesTask.disableDialog();
		getFollowedPlacesTask.execute();
	}

	private void asyncGetNearbyPlaces() {
		AsyncGetNearbyPlacesTask getNearbyPlacesTask = new AsyncGetNearbyPlacesTask(this);
		getNearbyPlacesTask.setCallable(new AsyncGetNearbyPlacesCallable());
		getNearbyPlacesTask.disableDialog();
		getNearbyPlacesTask.setLocation(LocationUtil.getCurrentLocation(Activity_Places.this));
		getNearbyPlacesTask.execute();
	}

	private void updateNearbyPlacesView() {
		PlaceTask.getNearbyPlacesFromDB(this, places);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + places);
		placesAdapter.notifyDataSetChanged();
	}

	private void updateFollowedPlacesView() {
		PlaceTask.getFollowedPlacesFromDB(this, places);
		Log.d(Constants.LOG_TAG, "Updating place list view with place list: " + places);
		placesAdapter.notifyDataSetChanged();
	}

	private class AsyncGetFollowedPlacesTask extends BetterAsyncTask<Void, Void, Integer> {
		public AsyncGetFollowedPlacesTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Places) context).updateFollowedPlacesView();
			} else {
				Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
			}
		}
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
				((Activity_Places) context).updateNearbyPlacesView();
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

	private class AsyncGetFollowedPlacesCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			resultCode = PlaceTask.getFollowedPlacesFromServer(Activity_Places.this);
			return resultCode;
		}
	}

	private class AsyncGetNearbyPlacesCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			Location location = ((AsyncGetNearbyPlacesTask) task).getLocation();
			resultCode = PlaceTask.getNearbyPlacesFromServer(Activity_Places.this, location);
			return resultCode;
		}
	}

	public void showPlacePosts(int position) {
		Map<String, Object> place = places.get(position);
		Intent intent = new Intent(Activity_Places.this, Activity_PlacePosts.class);
		intent.putExtra(DBConstants.F_PLACEID, (String) place.get(DBConstants.F_PLACEID));
		intent.putExtra(DBConstants.F_NAME, (String) place.get(DBConstants.F_NAME));
		intent.putExtra(DBConstants.F_DESC, (String) place.get(DBConstants.F_DESC));
		startActivity(intent);
	}

	private void lookupViewElements() {
		bNewPlace = (ImageButton) findViewById(R.id.place_new);
		tMore = (TextView) findViewById(R.id.more);
		bNearbyPlace = (Button) findViewById(R.id.place_around);
		bFollowedPlace = (Button) findViewById(R.id.place_followed);
	}
}