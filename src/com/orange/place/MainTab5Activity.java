package com.orange.place;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import org.json.JSONObject;

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
import android.widget.Toast;

import com.orange.place.constant.DBConstants;
import com.orange.place.constants.Constants;
import com.orange.place.helper.SqlLiteHelper;
import com.orange.sns.qqweibo.QQWeiboSNSRequest;
import com.orange.sns.service.SNSService;
import com.orange.sns.sina.SinaSNSRequest;
import com.orange.utils.ActivityUtil;
import com.orange.utils.UtilConstants;

public class MainTab5Activity extends Activity {

	SNSService snsService = new SNSService();
	SinaSNSRequest sinaRequest = new SinaSNSRequest();
	QQWeiboSNSRequest qqRequest = new QQWeiboSNSRequest();
	
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
//				sinaRequest.setCallbackURL("dipan://MainTab5Activity");
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

			}
		});
		
		Button bSendWeibo = (Button)findViewById(R.id.button_send_weibo);
		bSendWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snsService.sendWeibo(sinaRequest, "≥©œÎ»À…˙£¨æ¢±¨Œ¢≤©");
			}
		});
		
		Button bGetSinaUserInfo = (Button)findViewById(R.id.button_get_sina_weibo_info);
		bGetSinaUserInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject userJSON = snsService.getUserInfo(sinaRequest);
				
//				Toast.makeText(MainTab5Activity.this, userJSON.get("screen_name"), 5);
			}
		});
		
		Button buttonQQLogin = (Button) findViewById(R.id.button_qq_login);
		buttonQQLogin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				qqRequest.setAppKey("a91a42c67bc940b68f75fe885c4a03bc");
				qqRequest.setAppSecret("dd56c4565e5f22affb4aa839fadfc9c2");
				qqRequest.setCallbackURL("null");
				if (snsService.startAuthorization(qqRequest) == false)
					return;

				String url = snsService.getAuthorizeURL(qqRequest);

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

			}
		});
		
		Button bSendQQWeibo = (Button)findViewById(R.id.button_send_qq_weibo);
		bSendQQWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snsService.sendWeibo(qqRequest, "≥©œÎ»À…˙£¨æ¢±¨Ã⁄—∂Œ¢≤©");
			}
		});
		
		Button bGetQQUserInfo = (Button)findViewById(R.id.button_get_qq_weibo_info);
		bGetQQUserInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject userJSON = snsService.getUserInfo(qqRequest);
				
//				Toast.makeText(MainTab5Activity.this, userJSON.get("screen_name"), 5);
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
				if (resultCode == 0)
					snsService.getAccessToken(sinaRequest, pin);
				else
					snsService.getAccessToken(qqRequest, pin);
			}
		}
		
	}
}