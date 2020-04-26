package com.cg.jsbridge.core.camera;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Cameralmpl{

	public static TakePhotoSuccess success;
	
	/**
	 * 0表示打开相机
	 * 1表示打开相册
	 * @param context
	 * @param success
	 */
	public static void openCamera(Context context, TakePhotoSuccess success){
		Cameralmpl.success=success;
		toCameraBridgeActivity(context,"0",success);
		
	}
	
	
	public static void openPhotos(Context context, TakePhotoSuccess success){
		Cameralmpl.success=success;
		toCameraBridgeActivity(context,"1",success);
		
	}

	
	/**
	 * 0表示打开相机
	 * 1表示打开相册
	 * @param context
	 * @param success
	 * @param SyzModle
	 */
	public static void openCamera(Context context, TakePhotoSuccess success, boolean SyzModle){
		Cameralmpl.success=success;
		toCameraBridgeActivity(context,"0",success,SyzModle);
		
	}
	

	
	private static void toCameraBridgeActivity(Context contxt, String type, TakePhotoSuccess success) {
		Intent intent = new Intent(contxt, CameraBridgeActivity.class);
		intent.putExtra("Bundle", getBundle(type,success));
		contxt.startActivity(intent);
	}
	
	private static void toCameraBridgeActivity(Context contxt, String type, TakePhotoSuccess success, boolean SyzModle) {
		Intent intent = new Intent(contxt, CameraBridgeActivity.class);
		intent.putExtra("Bundle", getBundle(type,SyzModle));
		contxt.startActivity(intent);
	}
	
	
	private static Bundle getBundle(String type, TakePhotoSuccess success){
		Bundle bun=new Bundle();
		bun.putString("type",type);
		return bun;
	}
	
	private static Bundle getBundle(String type, boolean SyzModle){
		Bundle bun=new Bundle();
		bun.putString("type",type);
		bun.putBoolean("SyzModle",SyzModle);
		return bun;
	}
	
	


}
