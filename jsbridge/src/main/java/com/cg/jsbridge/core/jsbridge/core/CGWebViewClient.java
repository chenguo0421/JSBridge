package com.cg.jsbridge.core.jsbridge.core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class CGWebViewClient extends WebViewClient {
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		super.onPageFinished(view, url);
	}
	
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
		// TODO Auto-generated method stub
		return super.shouldInterceptRequest(view, request);
	}


	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		// TODO Auto-generated method stub
		handler.cancel();
	}


}
