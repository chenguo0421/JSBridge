package com.cg.jsbridge.core.jsbridge.core;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CGWebChromeClient extends WebChromeClient {
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
		result.confirm(CGJSBridge.callJava(view, message));
//		Toast.makeText(view.getContext(),"message="+message,Toast.LENGTH_LONG).show();
		return true;
	}


}

