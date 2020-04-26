package com.cg.jsbridge.core.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.cg.jsbridge.core.framwork.callback.CallBackIntent;
import com.cg.jsbridge.core.framwork.core.CGActivity;
import com.cg.jsbridge.core.util.ImageUtil;
import com.cg.jsbridge.core.util.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CameraBridgeActivity extends CGActivity implements PermissionUtils.PermissionCallbacks {
	private String type="0";
	private boolean isSynchronizedImg;
	public static File ImgFile;
	private CallBackIntent CallBackIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type=getIntentType();
		isSynchronizedImg=getIntentModle();
		judgePermissions();
		
	}
	
	
	private boolean getIntentModle() {
		// TODO Auto-generated method stub
		if (getIntent().getBundleExtra("Bundle")!=null&&getIntent().getBundleExtra("Bundle").getBoolean("SyzModle")) {
			return getIntent().getBundleExtra("Bundle").getBoolean("SyzModle");
		}
		return false;
	}


	private String getIntentType(){
		if (getIntent().getBundleExtra("Bundle")!=null&&getIntent().getBundleExtra("Bundle").getString("type")!=null) {
			return getIntent().getBundleExtra("Bundle").getString("type");
		}
		return "0";
	}
	
	private void judgePermissions() {
		String[] perms = {
				"android.permission.CAMERA",
				"android.permission.WRITE_EXTERNAL_STORAGE" };
		if (PermissionUtils.hasPermissions(this, perms)) {
			if ("0".equals(type)) {
				toCamera();
			}
			
			if ("1".equals(type)) {
				toPhotos();
			}
		} else
			PermissionUtils.requestPermissions(this, "这个程序需要访问你的相机及存储权限", 123,
					perms);
	}


	private  void toCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra("output", getUriForFile(this, CameraResultUtil.getTempImage()));
		startActivityForResult(intent, 1);
	}
	
	
	private void toPhotos(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this,CameraResultUtil.createImageFile(this)));
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this,CameraResultUtil.getTempImage()));
		intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivityForResult(intent, 2);
	}
	
	private void synchronizedImg(File file){
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		sendBroadcast(mediaScanIntent);
	}
	
	
	private Uri getUriForFile(Context context, File file) {
		if ((context == null) || (file == null))
			throw new NullPointerException();
		Uri uri;
		if (Build.VERSION.SDK_INT >= 24) {
			uri = CGBaseFileProvider.CGGetUriForFile(context, file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}
	
	public static File getTempImage() {
		if (Environment.getExternalStorageState().equals("mounted")) {
			File tempFile = new File(Environment.getExternalStorageDirectory(),
					"temp.jpg");
			try {
				tempFile.createNewFile();
				ImgFile=tempFile;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return tempFile;
		}
		return null;
	}

	//content://com.example.testcamera.fileprovider/lkl/temp.jpg
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==1&&resultCode == -1) {//相机
			Bitmap bitmap=null;
			String base64=null;
			showMaskDialog();
			try {
				bitmap= ImageUtil.rotateBitmap(CameraBridgeActivity.this, BitmapFactory.decodeFile(getTempImage().getPath()),getTempImage().getPath());
				base64=ImageUtil.base64(bitmap);
			} catch (Exception e) {
				e.getMessage();
			}

			if (Cameralmpl.success!=null) {
				Cameralmpl.success.takePhotoSuccess(bitmap,base64);
			}
			if (isSynchronizedImg&&ImgFile!=null) {
				synchronizedImg(ImgFile);
			}
			hideMaskDialog();
			finish();
			return;
		}


		if (requestCode==2&&resultCode == -1) {//相册
			Bitmap bitmap=null;
			String base64=null;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//				bitmap=ImageUtil.rotateBitmap(CameraBridgeActivity.this,BitmapFactory.decodeFile(getTempImage().getPath()),getTempImage().getPath());
				base64=ImageUtil.base64(ImageUtil.zoomImage(bitmap,(float)0.5));
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Cameralmpl.success!=null) {
				Cameralmpl.success.takePhotoSuccess(bitmap,base64);
			}
			finish();
			return;

		}

		if (resultCode != 0)
			return;
		if (data == null) {
			finish();
			return;
		}
		finish();
	}


	@SuppressLint("NewApi")
	public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
		PermissionUtils.onRequestPermissionsResult(requestCode, permissions,
				grantResults, this);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public void onPermissionsGranted(int requestCode, List<String> perms) {
		
		
		
//		String[] permsArr = { "android.permission.CAMERA",
//		"android.permission.WRITE_EXTERNAL_STORAGE" };
//
//		if (PermissionUtils.hasPermissions(this, permsArr)) {
			if ("0".equals(type)) {
				toCamera();
			}

			if ("1".equals(type)) {
				toPhotos();
			}
//		}else {
//			Toast.makeText(this, "相机或存储权限获取失败，请先在设置中手动打开相应权限！", Toast.LENGTH_LONG).show();
//			finish();
//		}
		
	}

	public void onPermissionsDenied(int requestCode, List<String> perms) {
		Toast.makeText(this, "相机或存储权限获取失败，请先在设置中手动打开相应权限！", Toast.LENGTH_LONG).show();
		finish();
	}

	public void log(String string) {
		Log.e("LKL11", "MainActivity " + string);
	}

}
