package com.cg.jsbridge.core.framwork.interf;

import android.content.Intent;

import com.cg.jsbridge.core.framwork.callback.CallBackIntent;

public interface ActivityInterface {
	public abstract void startActivityForResult(Intent paramIntent,
                                                CallBackIntent paramCallBackIntent);

	public abstract void setActivityResultCallback(Intent paramIntent);

	public abstract void showMaskDialog();

	public abstract void hideMaskDialog();
}
