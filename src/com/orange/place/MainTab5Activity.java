package com.orange.place;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.sns.service.SNSService;
import com.orange.sns.sina.SinaSNSRequest;
import com.orange.utils.ActivityUtil;
import com.orange.utils.UtilConstants;

public class MainTab5Activity extends Activity {

	SNSService snsService = new SNSService();
	SinaSNSRequest sinaRequest = new SinaSNSRequest();
	
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
//				SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(MainTab5Activity.this);
//				Cursor cur = sqlLiteHelper.queryPlaceList();
//				Log.d(Constants.LOG_TAG, "Get resoult count: " + cur.getCount());
//				while(cur.moveToNext()){
//					int nameColumn = cur.getColumnIndex(DBConstants.F_NAME);
//					int idColumn = cur.getColumnIndex(DBConstants.F_PLACEID);
//					String name = cur.getString(nameColumn);
//					String id = cur.getString(idColumn);
//					Log.d(Constants.LOG_TAG, "cursor=" + cur);
//					Log.d(Constants.LOG_TAG, "Name=" + name + ", id=" + id);
//				}
			}
		});
		
//		Button bCleanupPlaceList = (Button) findViewById(R.id.cleanup_place_list);
//		bCleanupPlaceList.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(MainTab5Activity.this);
//				sqlLiteHelper.cleanupNearbyPlaces();
//			}
//		});
		
		Button buttonSinaLogin = (Button) findViewById(R.id.button_sina_login);
		buttonSinaLogin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				sinaRequest.setAppKey("637805385");
				sinaRequest.setAppSecret("9391125674c00f84022a4ab191f5a392");
//				sinaRequest.setAppKey("1528146353");
//				sinaRequest.setAppSecret("4815b7938e960380395e6ac1fe645a5c");
				sinaRequest.setCallbackURL("dipan://MainTab5Activity");
				try {
					snsService.startAuthorization(sinaRequest);
					String url = snsService.getAuthorizeURL(sinaRequest);

					// open URL by browser
					/*
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
					*/					
					Intent intent = new Intent(MainTab5Activity.this, SNSWebViewActivity.class);
					Bundle b = new Bundle();
					b.putString("url", url);
					intent.putExtras(b);
					startActivityForResult(intent, 0);
					
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}		

			}
		});
		
		Button bSendWeibo = (Button)findViewById(R.id.button_send_weibo);
		bSendWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snsService.sendWeibo(sinaRequest, "为什么我是我，为什么我只能感觉到我，我死后是什么样子，我的感觉又会哪里去？");
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Log.i(UtilConstants.LOG_TAG, intent.getData().getQuery());
	} 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) { 
		Bundle bundle = intent.getExtras();
		if (bundle != null){
			String pin = (String) bundle.get("PIN");
			if (pin != null){
//				sinaRequest.setOauthToken("b4f19f9a5aa03c1aa094ac529218a974");
//				sinaRequest.setOauthTokenSecret("a5363f26b7f89f9555573643aaa68f27");
				snsService.getAccessToken(sinaRequest, pin);
			}
		}
		
	}
}