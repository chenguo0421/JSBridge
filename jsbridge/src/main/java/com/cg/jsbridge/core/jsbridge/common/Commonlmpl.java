package com.cg.jsbridge.core.jsbridge.common;

import android.webkit.WebView;
import android.widget.Toast;


import com.cg.jsbridge.core.jsbridge.core.CallBack;
import com.cg.jsbridge.core.jsbridge.core.IBridge;
import com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack;

import org.json.JSONObject;

public class Commonlmpl implements IBridge {

	public static com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack JSNativeCallBack;
	public static void otherAction(WebView webView, JSONObject param, final CallBack callback){
		String method=param.optString("method");
		String params=param.optString("params");
		if (JSNativeCallBack!=null) {
			JSNativeCallBack.NativeCall(webView,method, params, callback);
		}else {
			Toast.makeText(webView.getContext(),"请您先实现JSNativeCallBack抽象类，并使用CGWebView的init方法初始化", Toast.LENGTH_LONG).show();
		}
		
	}

	public static void addJSNativeCallBack(JSNativeCallBack callback){
		JSNativeCallBack=callback;
	}

}
