package com.orange.place;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orange.place.constants.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SNSWebViewActivity extends Activity {

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
	
	String pin = null;

	class JavaScriptInterface {
		public void getHTML(String html) {
			pin = getPin(html);
			// 这里就获取到了我们想要的pin码
			// 这个pin码就是oauth_verifier值,用来进一步获取Access Token和Access Secret用
			Log.d("pin", pin);
			
			// return the previous activity
			Bundle bundle = new Bundle();
	        bundle.putString("PIN", pin);
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
				Log.i(Constants.LOG_TAG, url);
				if(url.equals("http://api.t.sina.com.cn/oauth/authorize")){
				
					view
						.loadUrl("javascript:window.Methods.getHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
					super.onPageFinished(view, url);
				}

			}
		};
		webview.setWebViewClient(wvc);

		Intent i = this.getIntent();
		if (!i.equals(null)) {
			Bundle b = i.getExtras();
			if (b != null) {
				if (b.containsKey("url")) {
					String url = b.getString("url");

					// 这行很重要一点要有，不然网页的认证按钮会无效
					webview.getSettings().setJavaScriptEnabled(true);
					webview.getSettings().setSupportZoom(true);
					// wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
					webview.getSettings().setBuiltInZoomControls(true);

					Log.d(Constants.LOG_TAG, "open URL=" + url);

					webview.loadUrl(url);
					webview.requestFocus();
				}
			}
		}

	}
}
