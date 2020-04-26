package com.cg.jsbridge.core.jsbridge.web;

import android.os.Build;
import android.os.Bundle;

import com.cg.jsbridge.core.R;
import com.cg.jsbridge.core.framwork.core.CGActivity;

import org.json.JSONObject;

import androidx.annotation.RequiresApi;


public class WebActivity extends CGActivity {

	private CGWebView webview;
	private CGWebViewInterface cgInterface;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cgweb);
		initViews();
		
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void initViews() {
		// TODO Auto-generated method stub
		webview=(CGWebView)findViewById(R.id.webview);
		cgInterface=new CGWebViewInterface(this);
		webview.init(this,cgInterface);
		webview.loadUrl("file:///android_asset/index.html");
//		webview.loadUrl(
//				"http://39.105.49.150/wp-admin/admin.php?page=bp-groups");
//		webview.loadUrl("http://m.appchina.com/down/u1934");
	}

	
	
	public interface WebResultInterface{
		public void onresult(JSONObject result);
	}



	
	


}
