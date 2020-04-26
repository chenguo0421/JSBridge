package com.cg.jsbridge.core.framwork.core;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.cg.jsbridge.core.customview.AlertDialog;
import com.cg.jsbridge.core.customview.AlertDialogWithoutAnything;
import com.cg.jsbridge.core.framwork.callback.CallBackIntent;
import com.cg.jsbridge.core.framwork.interf.ActivityInterface;
import com.cg.jsbridge.core.util.Constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.fragment.app.FragmentActivity;

public abstract class CGActivity extends FragmentActivity implements ActivityInterface {

	private static final String TAG = "CSIIActivity";
	private static final int REQUESTCODE = 100;
	private static final int RESULTCODE = 101;
	private com.cg.jsbridge.core.framwork.callback.CallBackIntent CallBackIntent;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private Dialog dialog = null;
	private boolean isRunning = false;
	private long dialogTime = 60000L;
	private Dialog dialogWithoutAnything;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.isRunning = true;
		this.dialog = AlertDialog.createAlertDialog(this,Constant.ProgressHint, this.dialogTime, new AlertDialog.OnTimeOutListener() {
			
			@Override
			public void onTimeOut(Dialog dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG, "dialog--timeout:"
						+ CGActivity.this.dialogTime);
			}
		});
		
		this.dialogWithoutAnything = AlertDialogWithoutAnything.createAlertDialog(this, Constant.ProgressHint, this.dialogTime, new AlertDialogWithoutAnything.OnTimeOutListener() {
			
			@Override
			public void onTimeOut(Dialog dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG, "dialog--timeout:"
						+ CGActivity.this.dialogTime);
			}
		});
	}

	protected void onDestroy() {
		super.onDestroy();
		this.isRunning = false;
	}

	public void startActivityForResult(Intent paramIntent,
                                       CallBackIntent paramCallBackIntent) {
		this.CallBackIntent = paramCallBackIntent;
		startActivityForResult(paramIntent, 100);
	}

	public ExecutorService getThreadPool() {
		return this.threadPool;
	}

	public void setActivityResultCallback(Intent paramIntent) {
		if (paramIntent == null)
			setResult(RESULTCODE, new Intent());
		else
			setResult(RESULTCODE, paramIntent);
		finish();
	}

	protected void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		Log.d("CGActivity", "onActivityResult---requestCode:" + paramInt1
				+ "---resultCode:" + paramInt2);
		if (RESULTCODE == paramInt2)
			if (this.CallBackIntent != null)
				this.CallBackIntent.onResult(paramIntent);
			else
				Log.d(TAG, "onActivityResult--callback==null");
		else if ((REQUESTCODE == paramInt1) && (paramInt2 != 0))
			if (this.CallBackIntent != null)
				this.CallBackIntent.onResult(paramIntent);
			else
				Log.d(TAG,"onActivityResult--callback==null");
		super.onActivityResult(paramInt1, paramInt2, paramIntent);
	}

	public void showMaskDialog() {
		Log.d(TAG, "showMaskDialog");
		if ((!(this.isRunning)) || (this.dialog == null))
			return;
		this.dialog.show();
	}

	public void hideMaskDialog() {
		Log.d(TAG, "hideMaskDialog");
		if ((!(this.isRunning)) || (this.dialog == null))
			return;
		this.dialog.dismiss();
	}
	
	public void showMaskDialogWithoutAnything() {
		Log.d(TAG, "showMaskDialog");
		if ((!(this.isRunning)) || (this.dialogWithoutAnything == null))
			return;
		this.dialogWithoutAnything.show();
	}

	public void hideMaskDialogWithoutAnything() {
		Log.d(TAG, "hideMaskDialog");
		if ((!(this.isRunning)) || (this.dialogWithoutAnything == null))
			return;
		this.dialogWithoutAnything.dismiss();
	}

	public void setMaskDialog(Dialog paramDialog) {
		this.dialog = paramDialog;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void setMaskShowTime(long paramLong) {
		this.dialogTime = paramLong;
	}
}
