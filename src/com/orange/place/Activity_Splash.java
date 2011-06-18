package com.orange.place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Activity_Splash extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		// set a event, so splash could finish in some period
		(new Handler()).postDelayed(new Runnable() {
			public void run() {
				startActivity(new Intent(getApplication(), Activity_Registration.class));
				Activity_Splash.this.finish();
			}
		}, 500);
	}
}