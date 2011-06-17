package com.orange.place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//public class Activity_Place extends BetterListActivity { // we might need this for async 
public class Activity_Place extends ListActivity {
	List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		list = getListForSimpleAdapter();
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_place_item, new String[] { "BigText",
				"LittleText", "img" }, new int[] { R.id.BigText, R.id.LittleText, R.id.img });

		setContentView(R.layout.activity_place);
		setListAdapter(adapter);

		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				alertSelection(position);
			}
		});
	}

	public void alertSelection(int position) {
		new AlertDialog.Builder(this).setTitle("Self defined ListVeiw")
				.setMessage("you clicked the button:" + position)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whButton) {
					}
				}).show();
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