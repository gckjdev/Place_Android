package com.orange.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainTabActivity_bak extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_tab_2_activity);
//		initActionBar();
	}

//	private void initActionBar() {
//		LinearLayout header = (LinearLayout) findViewById(R.id.tab_header);
//
//		LinearLayout leftLayout = new LinearLayout(MainTabActivity_bak.this);
//		LinearLayout centerLayout = new LinearLayout(MainTabActivity_bak.this);
//		LinearLayout rightLayout = new LinearLayout(MainTabActivity_bak.this);
//		TextView textView = new TextView(MainTabActivity_bak.this);
//		Button leftButton = new Button(MainTabActivity_bak.this);
//		ImageView imageView = new ImageView(MainTabActivity_bak.this);
//		LinearLayout.LayoutParams textViewparams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//		LinearLayout.LayoutParams leftButtonParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//		LinearLayout.LayoutParams rightButtonParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//
//		leftButton.setText(R.string.header_left_text);
//		textView.setText(R.string.app_name);
//		textView.setGravity(Gravity.CENTER);
//		imageView.setImageResource(R.drawable.main_tab_1_nav_on);
//
//		leftLayout.setGravity(Gravity.CENTER);
//		leftLayout.addView(leftButton);
//		centerLayout.setGravity(Gravity.CENTER);
//		centerLayout.addView(textView);
//		rightLayout.setGravity(Gravity.CENTER);
//		rightLayout.addView(imageView);
//
//		header.addView(leftLayout, leftButtonParams);
//		header.addView(centerLayout, textViewparams);
//		header.addView(rightLayout, rightButtonParams);
//
//		leftButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MainTabActivity_bak.this, R.string.app_name,
//						Toast.LENGTH_LONG).show();
//			}
//		});
//		imageView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MainTabActivity_bak.this, R.string.main_tab_1,
//						Toast.LENGTH_LONG).show();
//			}
//		});
//	}
}