package com.cg.jsbridge.core.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraResultUtil {
	
	
	public static File getTempImage() {
		if (Environment.getExternalStorageState().equals("mounted")) {
			File tempFile = new File(Environment.getExternalStorageDirectory(),
					"temp.jpg");
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return tempFile;
		}
		return null;
	}
	

	
	@SuppressLint("NewApi")
	public static File createImageFile(Context context){
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		//.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
		File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		//创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
		File image=null;
		try {
			image = File.createTempFile(
					imageFileName,  /* 前缀 */
					".jpg",         /* 后缀 */
					storageDir      /* 文件夹 */
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	public static String base64(Bitmap bitmap) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		zoomImage(bitmap).compress(Bitmap.CompressFormat.JPEG, 80, bStream);
		byte[] bytes = bStream.toByteArray();
		String result = Base64.encodeToString(bytes, 2);
		return result;
	}
	
	public static Bitmap zoomImage(Bitmap bitmap) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		float scaleWidth = 0.2F;
		float scaleHeight = 0.2F;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
				(int) height, matrix, true);
		return newBitmap;
	}
	
	
	/** 
	  * fuction: 设置固定的宽度，高度随之变化，使图片不会变形 
	  * 
	  * @param target 
	  * 需要转化bitmap参数 
	  * @param newWidth 
	  * 设置新的宽度 
	  * @return 
	  */  
	 public static Bitmap fitBitmap(Bitmap target, int newWidth)
	 {  
	  int width = target.getWidth();  
	  int height = target.getHeight();  
	  Matrix matrix = new Matrix();
	  float scaleWidth = ((float) newWidth) / width;  
	  // float scaleHeight = ((float)newHeight) / height;  
	  @SuppressWarnings("unused")
	int newHeight = (int) (scaleWidth * height);  
	  matrix.postScale(scaleWidth, scaleWidth);  
	  // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,  
	  // matrix,true);  
	  Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
	    true);  
	  if (target != null && !target.equals(bmp) && !target.isRecycled())  
	  {  
	   target.recycle();  
	   target = null;  
	  }  
	  return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,  
	     // true);  
	 }  
	

}
