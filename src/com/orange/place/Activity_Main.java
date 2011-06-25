package com.orange.place;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TabHost;

import com.orange.utils.ActivityUtil;
import com.orange.utils.TabsUtil;
import com.orange.utils.UiUtil;

public class Activity_Main extends TabActivity {
	public static final String TAG = "MainActivity";

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtil.setNoTitle(this);
		setContentView(R.layout.activity_main);
		initTabHost();
	}

	private void initTabHost() {
		if (tabHost != null) {
			throw new IllegalStateException("Trying to intialize already initializd TabHost");
		}

		tabHost = getTabHost();
		TabsUtil.addTab(tabHost, getString(R.string.main_tab_1), R.drawable.main_tab_1_nav, 1, new Intent(this,
				Activity_Place.class));
		TabsUtil.addTab(tabHost, getString(R.string.main_tab_2), R.drawable.main_tab_2_nav, 2, new Intent(this,
				MainTab2Activity.class));
		TabsUtil.addTab(tabHost, getString(R.string.main_tab_3), R.drawable.main_tab_3_nav, 3, new Intent(this,
				MainTab3Activity.class));
		TabsUtil.addTab(tabHost, getString(R.string.main_tab_4), R.drawable.main_tab_4_nav, 4, new Intent(this,
				MainTab4Activity.class));
		TabsUtil.addTab(tabHost, getString(R.string.main_tab_5), R.drawable.main_tab_5_nav, 5, new Intent(this,
				MainTab5Activity.class));

		// Fix layout for 1.5.
		if (UiUtil.sdkVersion() < 4) {
			FrameLayout flTabContent = (FrameLayout) findViewById(android.R.id.tabcontent);
			flTabContent.setPadding(0, 0, 0, 0);
		}
	}
}
