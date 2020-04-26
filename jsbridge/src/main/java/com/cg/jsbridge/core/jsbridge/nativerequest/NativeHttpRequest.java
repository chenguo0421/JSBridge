package com.cg.jsbridge.core.jsbridge.nativerequest;

import android.webkit.WebView;
import android.widget.Toast;


import com.cg.jsbridge.core.jsbridge.core.CallBack;
import com.cg.jsbridge.core.jsbridge.core.IBridge;
import com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack;

import org.json.JSONObject;

public class NativeHttpRequest implements IBridge {
	
	public static com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack JSNativeCallBack;
	public static void post(WebView webView, JSONObject param, final CallBack callback){
		String params=param.optString("params");
		String url=param.optString("url");
		if (JSNativeCallBack!=null) {
			JSNativeCallBack.RequestPost(webView, url, params, callback);
		}else {
			Toast.makeText(webView.getContext(),"请您先实现JSNativeCallBack抽象类，并使用CGWebView的init方法初始化", Toast.LENGTH_LONG).show();
		}
		
	}

	public static void get(WebView webView, JSONObject param, final CallBack callback){
		String params=param.optString("params");
		String url=param.optString("url");
		if (JSNativeCallBack!=null) {
			JSNativeCallBack.RequestGet(webView, url, params, callback);
		}else {
			Toast.makeText(webView.getContext(),"请您先实现JSNativeCallBack抽象类，并使用CGWebView的init方法初始化", Toast.LENGTH_LONG).show();
		}
	}

	
	public static void addJSNativeCallBack(JSNativeCallBack callback){
		JSNativeCallBack=callback;
	}
}
