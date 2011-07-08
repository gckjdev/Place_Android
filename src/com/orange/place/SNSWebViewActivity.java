package com.orange.place;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orange.place.constants.Constants;
import com.orange.sns.qqweibo.QQWeiboSNSRequest;
import com.orange.sns.service.SNSService;
import com.orange.sns.sina.SinaSNSRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SNSWebViewActivity extends Activity {

	public static final int REQUEST_FROM_SINA	= 0;
	public static final int REQUEST_FROM_QQ		= 1;
	public static final int REQUEST_FROM_RENREN	= 2;
	
	public static final String REQUEST_FROM = "REQUEST_FORM";
	public static final String PIN = "PIN";
	public static final String AUTHORIZE_URL = "url";

	public static final String LOG_TAG = "SNSWebViewActivity";	
	
	String pin = null;
	int requestFrom;		
	
	public String getPin(String html) {
		String ret = "";
		String regEx = "[0-9]{6}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		boolean result = m.find();
		if (result) {
			ret = m.group(0);
		}
		return ret;
	}
	

	class JavaScriptInterface {
		public void parseHTML(String html) {
			pin = getPin(html);
			if (pin == null){
				Log.d(LOG_TAG, "parse PIN, but no PIN found");
				return;
			}
			
			// 这里就获取到了我们想要的pin码
			// 这个pin码就是oauth_verifier值,用来进一步获取Access Token和Access Secret用
			Log.d(LOG_TAG, "parse PIN, PIN="+pin);
			
			// finish this activity so that we can return the previous activity
			Bundle bundle = new Bundle();
	        bundle.putString(PIN, pin);
	        bundle.putInt(REQUEST_FROM, requestFrom);
	        SNSWebViewActivity.this.setResult(0, SNSWebViewActivity.this.getIntent().putExtras(bundle)); 
			SNSWebViewActivity.this.finish();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sns_webview);

		WebView webview = (WebView) findViewById(R.id.snswebview);
		webview.addJavascriptInterface(new JavaScriptInterface(), "Methods");
		WebViewClient wvc = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(LOG_TAG, "Loading URL="+url);
				if(url.equals(SinaSNSRequest.SINA_AUTHORIZE_URL)){				
					view
						.loadUrl("javascript:window.Methods.parseHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");					
				}
				// TODO improve the contains here for QQ
				else if (url.contains("http://open.t.qq.com/oauth_html/mobile.php?type=3&code=0&v=")){
					view
					.loadUrl("javascript:window.Methods.parseHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");				
				}
				else{
				}

				super.onPageFinished(view, url);
			}
		};
		webview.setWebViewClient(wvc);

		Intent i = this.getIntent();
		if (i != null) {
			Bundle b = i.getExtras();
			if (b != null) {
				if (b.containsKey(AUTHORIZE_URL) && (b.getString(AUTHORIZE_URL) != null)) {
					String url = b.getString(AUTHORIZE_URL);
					requestFrom = b.getInt(REQUEST_FROM);

					// 这行很重要一点要有，不然网页的认证按钮会无效
					webview.getSettings().setJavaScriptEnabled(true);
					webview.getSettings().setSupportZoom(true);
					// wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
					webview.getSettings().setBuiltInZoomControls(true);

					Log.d(LOG_TAG, "Start SNS web view, load URL=" + url);

					webview.loadUrl(url);
					webview.requestFocus();
				}
				else{
					Log.e(LOG_TAG, "Start SNS web view activity but no URL key found");					
				}
			}
			else{
				Log.e(LOG_TAG, "Start SNS web view activity but no extra data (URL) found");
			}
		}

	}
	
	// invoke this method to load web view, see below example
	/*
	 * 				String url = snsService.getAuthorizeURL(sinaRequest);
				SNSWebViewActivity.loadActivity(MainTab5Activity.this, 
						url, SNSWebViewActivity.REQUEST_FROM_SINA);

	 */
	public static boolean loadActivity(Activity fromActivity, String authorizeURL, int requestFrom){
		if (fromActivity == null || authorizeURL == null)
			return false;
		
		Intent intent = new Intent(fromActivity, SNSWebViewActivity.class);
		Bundle b = new Bundle();
		b.putString(AUTHORIZE_URL, authorizeURL);
		b.putInt(REQUEST_FROM, requestFrom);
		intent.putExtras(b);
		fromActivity.startActivityForResult(intent, 0);			
		return true;
	}
	
	// invoke this method in onActivityResult, see below example
	/*
	 * 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) { 
		SNSWebViewActivity.loadActivityFinish(intent, snsService, sinaRequest, qqRequest);		
	}
	 */
	public static void loadActivityFinish(Intent intent, SNSService snsService, SinaSNSRequest sinaRequest, QQWeiboSNSRequest qqRequest){
		Bundle bundle = intent.getExtras();
		if (bundle != null){
			String pin = bundle.getString(SNSWebViewActivity.PIN);
			if (pin != null){
				int requestFrom = bundle.getInt(SNSWebViewActivity.REQUEST_FROM);
				if (requestFrom == SNSWebViewActivity.REQUEST_FROM_SINA)
					snsService.getAccessToken(sinaRequest, pin);
				else
					snsService.getAccessToken(qqRequest, pin);
			}
		}
	}
}
