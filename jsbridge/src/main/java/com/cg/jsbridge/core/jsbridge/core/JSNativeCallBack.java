package com.cg.jsbridge.core.jsbridge.core;

import android.webkit.WebView;

public abstract interface JSNativeCallBack {
	public abstract void NativeCall(String Method, String params, CallBack callBack);

	public abstract void RequestPost(WebView view, String url, String params, CallBack callBack);

	public abstract void RequestGet(WebView view, String Method, String params, CallBack callBack);

	public abstract void NativeCall(WebView webView, String method,
                                    String params, CallBack callback);
}
