package com.orange.place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;
import com.orange.place.services.PlaceService;
import com.orange.place.services.UserService;
import com.orange.utils.ActivityUtil;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Place extends ListActivity {

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
			}
		});
	}

	public void alertSelection(int position) {
		new AlertDialog.Builder(this).setTitle("Self defined ListVeiw")
				.setMessage("you clicked the button:" + position)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whButton) {

						Location location;
						LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location == null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						}
						Log.d(Constants.LOG_TAG, "Get location from Network: " + location);

						if (location != null) {
							PlaceService.getPlaceList(Activity_Place.this, location.getLongitude(),
									location.getLatitude());
						} else {
							// shit, can not get location yet, this just for test the API
							PlaceService.getPlaceList(Activity_Place.this, 0, 0);
						}
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