package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
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
import com.orange.place.helper.SqlLiteHelper;
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;
import com.orange.utils.ToastUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Place extends BetterListActivity {

	private List<Map<String, Object>> nearbyPlaceList = new ArrayList<Map<String, Object>>();
	private SimpleAdapter nearbyPlaceListAdapter;
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

		updatePlaceList(nearbyPlaceList);
		String[] placeListFrom = new String[] { "PlaceImage", DBConstants.F_NAME, DBConstants.F_DESC,
				DBConstants.F_USERID };
		int[] placeListTo = new int[] { R.id.PlaceImage, R.id.PlaceName, R.id.PlaceDesc, R.id.UserId };
		nearbyPlaceListAdapter = new SimpleAdapter(this, nearbyPlaceList, R.layout.list_place_item, placeListFrom, placeListTo);
		setListAdapter(nearbyPlaceListAdapter);

		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showPlacePostList(position);
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
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Log.d(Constants.LOG_TAG, "Location providers: " + locationManager.getAllProviders());

				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				Log.d(Constants.LOG_TAG, "Get location: " + location);
				// can not get location yet!

				asyncGetNearbyPlaceList(location);
			}
		});
	}

	private void asyncGetNearbyPlaceList(Location location) {
		AsyncGetNearbyPlaceListTask getPlaceListTask = new AsyncGetNearbyPlaceListTask(this);
		getPlaceListTask.setCallable(new AsyncGetPlaceListCallable());
		getPlaceListTask.disableDialog();
		getPlaceListTask.setLocation(location);
		getPlaceListTask.execute();
	}

	private void updateNearbyPlaceListView() {
		updatePlaceList(nearbyPlaceList);
		Log.w(Constants.LOG_TAG, "Updating place list view with place list: " + nearbyPlaceList);
		nearbyPlaceListAdapter.notifyDataSetChanged();
	}

	private void updatePlaceList(List<Map<String, Object>> list) {
		SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(Activity_Place.this);
		sqlLiteHelper.updatePlaceList(list);
	}

	private class AsyncGetNearbyPlaceListTask extends BetterAsyncTask<Void, Void, Integer> {
		private Location location;

		public AsyncGetNearbyPlaceListTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in asyncRequestPlace", error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if (taskResult == ErrorCode.ERROR_SUCCESS) {
				((Activity_Place) context).updateNearbyPlaceListView();
			} else {
				Log.w(Constants.LOG_TAG, "AsyncGetPlaceListTask failed, nothing need to update!");
			}
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
	}

	private class AsyncGetPlaceListCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
		@Override
		public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
			int resultCode = Constants.ERROR_UNKOWN;
			Location location = ((AsyncGetNearbyPlaceListTask) task).getLocation();
			resultCode = PlaceTask.getNearbyPlaceList(Activity_Place.this, location);
			return resultCode;
		}
	}

	public void showPlacePostList(int position) {
		Map<String, Object> place = nearbyPlaceList.get(position);
		new AlertDialog.Builder(this).setTitle("Self defined ListVeiw")
				.setMessage("you clicked the place:" + place.get(DBConstants.F_PLACEID))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whButton) {
					}
				}).show();
	}

	private void lookupViewElements() {
		bNewPlace = (ImageButton) findViewById(R.id.place_new);
		tMore = (TextView) findViewById(R.id.refresh);
	}
}