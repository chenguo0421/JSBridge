package com.cg.jsbridge.core.util;

import android.app.Application;



public class Constant extends Application {
	public static final boolean isDebug=true;
	public static final String ProgressHint = "正在加载...";
	private static Constant instance;
	public static final int PermissionRequestCode=75;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
}
