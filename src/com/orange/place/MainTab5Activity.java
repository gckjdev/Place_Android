package com.orange.place;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.utils.ActivityUtil;

public class MainTab5Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.main_tab_5_activity);

		Button bCleanLoginId = (Button) findViewById(R.id.clean_login_id);
		bCleanLoginId.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString(DBConstants.F_LOGINID, null);
				editor.commit();
			}
		});
		
		Button bQueryPlaceList = (Button) findViewById(R.id.query_place_list);
		bQueryPlaceList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(MainTab5Activity.this);
				Cursor cur = sqlLiteHelper.queryPlaceList();
				Log.d(Constants.LOG_TAG, "Get resoult count: " + cur.getCount());
				while(cur.moveToNext()){
					int nameColumn = cur.getColumnIndex(DBConstants.F_NAME);
					int idColumn = cur.getColumnIndex(DBConstants.F_PLACEID);
					String name = cur.getString(nameColumn);
					String id = cur.getString(idColumn);
					Log.d(Constants.LOG_TAG, "cursor=" + cur);
					Log.d(Constants.LOG_TAG, "Name=" + name + ", id=" + id);
				}
			}
		});
	}
}