package com.cg.jsbridge.core.jsbridge.core;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.cg.jsbridge.core.jsbridge.util.GlobalParams;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class CallBack {

	private static Handler mHandler = new Handler(Looper.getMainLooper());
	private WeakReference<WebView> mWebViewRef;
	private String mPort;

	public CallBack(WebView webView, String port) {
		// TODO Auto-generated constructor stub
		 mWebViewRef = new WeakReference<WebView>(webView);
	     mPort = port;
	}
	
	public void apply(JSONObject jsonObject) {
		final String execJs = String.format(GlobalParams.CallJS_FORMAT, mPort, String.valueOf(jsonObject));
		if (mWebViewRef != null && mWebViewRef.get() != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mWebViewRef.get().loadUrl(execJs);
				}
			});

		}

	}


}
