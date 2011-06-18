package com.orange.place;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;

public class MainTab5Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_tab_5_activity);

		Button bCleanLoginId;
		bCleanLoginId = (Button) findViewById(R.id.clean_login_id);
		bCleanLoginId.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString(DBConstants.F_LOGINID, null);
				editor.commit();
			}
		});
	}
}