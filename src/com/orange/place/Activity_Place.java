package com.orange.place;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.droidfu.activities.BetterListActivity;
import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;
import com.orange.utils.ActivityUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Place extends BetterListActivity {

	private List<Map<String, Object>> list;

	private Button bGoBack;
	private Button bRefresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_place);
		lookupViewElements();
		setRefreshListener();

		list = getListForSimpleAdapter();
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_place_item, new String[] { "BigText",
				"LittleText", "img" }, new int[] { R.id.BigText, R.id.LittleText, R.id.img });
		setListAdapter(adapter);

		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				alertSelection(position);
			}
		});

	}

	private void setRefreshListener() {
		bRefresh.setOnClickListener(new View.OnClickListener() {
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

				asyncGetPlaceList(location);
			}
		});
	}

	private void asyncGetPlaceList(Location location) {
		AsyncGetPlaceListTask getPlaceListTask = new AsyncGetPlaceListTask(this);
		getPlaceListTask.setCallable(new AsyncGetPlaceListCallable());
		getPlaceListTask.disableDialog();
		getPlaceListTask.setLocation(location);
		getPlaceListTask.execute();
	}

	protected void updatePlaceList() {
		Log.w(Constants.LOG_TAG, "Updating place list view");
		// TODO update list view here
	}

	private class AsyncGetPlaceListTask extends BetterAsyncTask<Void, Void, Integer> {
		private Location location;

		public AsyncGetPlaceListTask(Context context) {
			super(context);
		}

		@Override
		protected void handleError(Context context, Exception error) {
			Log.e(Constants.LOG_TAG, "Error happened in asyncRequestPlace", error);
		}

		@Override
		protected void after(Context context, Integer taskResult) {
			if(taskResult == ErrorCode.ERROR_SUCCESS){
				((Activity_Place) context).updatePlaceList();
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
			int resultCode = Constants.ERROR_RESP_UNKOWN;
			Location location = ((AsyncGetPlaceListTask) task).getLocation();
			resultCode = PlaceTask.getPlaceList(Activity_Place.this, location);
			return resultCode;
		}
	}

	public void alertSelection(int position) {
		new AlertDialog.Builder(this).setTitle("Self defined ListVeiw")
				.setMessage("you clicked the button:" + position)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whButton) {
					}
				}).show();
	}

	private void lookupViewElements() {
		bRefresh = (Button) findViewById(R.id.refresh);
		bGoBack = (Button) findViewById(R.id.go_back);
	}

	private List<Map<String, Object>> getListForSimpleAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(3);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BigText", "Android");
		map.put("LittleText", "Google phone.");
		map.put("img", R.drawable.title_button_normal);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("BigText", "Lenovo");
		map.put("LittleText", "Ophone");
		map.put("img", R.drawable.title_button_selected);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("BigText", "Droid");
		map.put("LittleText", "Motorola");
		map.put("img", R.drawable.title_lightgray_bg);
		list.add(map);

		return list;
	}
}