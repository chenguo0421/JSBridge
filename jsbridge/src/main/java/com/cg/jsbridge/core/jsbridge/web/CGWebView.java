package com.cg.jsbridge.core.jsbridge.web;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.cg.jsbridge.core.jsbridge.core.CGJSBridge;
import com.cg.jsbridge.core.jsbridge.core.CGWebChromeClient;
import com.cg.jsbridge.core.jsbridge.core.CGWebViewClient;
import com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack;
import com.cg.jsbridge.core.util.ToastUtils;

import androidx.annotation.RequiresApi;

public class CGWebView extends WebView {


	@SuppressWarnings("unused")
	private JSNativeCallBack jsNativeCallBack;
	private CGWebChromeClient client;
	public CGWebView(Context context) {
		super(context);
	}
	public CGWebView(Context context, AttributeSet attr) {
		super(context,attr);
	}



	/**
	 * 初始化webview
	 */
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@SuppressLint("SetJavaScriptEnabled")
	public void init(Context context, JSNativeCallBack jsNativeCallBack){
		this.jsNativeCallBack=jsNativeCallBack;
		if (jsNativeCallBack!=null) {
			CGJSBridge.registerDefualtHandler(jsNativeCallBack);
			setWebView();
		}else {
			ToastUtils.show(context, "缺少JSNativeCallBack回调，初始化webview失败", Toast.LENGTH_LONG);
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@SuppressLint({"SdCardPath", "SetJavaScriptEnabled", "WrongConstant"})
	@SuppressWarnings("deprecation")
	private void setWebView() {
		getSettings().setJavaScriptEnabled(true);
		getSettings().setAllowFileAccess(false);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		getSettings().setCacheMode(2);
		getSettings().setDatabaseEnabled(true);
		getSettings().setDomStorageEnabled(true);
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setUseWideViewPort(true);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) {
			getSettings().setAllowFileAccessFromFileURLs(false);
			getSettings().setAllowUniversalAccessFromFileURLs(false);
		}
		getSettings().setDatabasePath(
				"/data/data/" + getContext().getPackageName() + "/databases/");
		getSettings().setAppCacheEnabled(false);
		
		client=new CGWebChromeClient();
		setWebViewClient(new CGWebViewClient());
		setWebChromeClient(client);


		
	}



	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		invalidate();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	public void setResult(String resultParams) {
		// TODO Auto-generated method stub
		
	}

}
