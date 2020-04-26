package com.cg.jsbridge.core.jsbridge.camera;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.cg.jsbridge.core.camera.CameraResultUtil;
import com.cg.jsbridge.core.camera.TakePhotoSuccess;
import com.cg.jsbridge.core.jsbridge.core.CallBack;
import com.cg.jsbridge.core.jsbridge.core.IBridge;

import org.json.JSONException;
import org.json.JSONObject;

public class Cameralmpl  implements IBridge {
	
	public static CallBack callBack;
	/**
	 * 0表示打开相机
	 * 1表示打开相册
	 * @param webView
	 * @param param
	 * @param callback
	 */
	public static void openCamera(final WebView webView, JSONObject param, CallBack callback){
		Cameralmpl.callBack=callback;
		com.cg.jsbridge.core.camera.Cameralmpl.openCamera(webView.getContext(), new TakePhotoSuccess() {
			@Override
			public void takePhotoSuccess(Bitmap bitmap, String base64_str) {
				// TODO Auto-generated method stub
				if (Cameralmpl.callBack!=null) {
					JSONObject js = new JSONObject();
					try {
						
						//压缩图片，加快传输速率
//						if (bitmap.getByteCount()>30*1024*1024) {
//							bitmap=ImageUtil.compressImageToBitmap(CameraBridgeActivity.ImgFile.getPath(),4);
//						}else if (bitmap.getByteCount()>20*1024*1024) {
//							bitmap=ImageUtil.compressImageToBitmap(CameraBridgeActivity.ImgFile.getPath(),3);
//						}else if (bitmap.getByteCount()>10*1024*1024){
//							bitmap=ImageUtil.compressImageToBitmap(CameraBridgeActivity.ImgFile.getPath(),2);
//						}
//						bitmap=ImageUtil.compressRGB565(CameraBridgeActivity.ImgFile.getPath());
						//旋转图片
						
						String str = CameraResultUtil.base64(bitmap);
						js.put("photo_base64", str);
					} catch (Exception e) {
						e.getMessage();
						try {
							js.put("error", e.getMessage());
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}

					Cameralmpl.callBack.apply(js);
				}
				
			}
		});
		
	}
	
	
	public static void openPhotos(WebView webView, JSONObject param, CallBack callback){
		Cameralmpl.callBack=callback;
		com.cg.jsbridge.core.camera.Cameralmpl.openPhotos(webView.getContext(), new TakePhotoSuccess() {
			
			@Override
			public void takePhotoSuccess(Bitmap bitmap, String base64_str) {
				// TODO Auto-generated method stub
				JSONObject js = new JSONObject();
				try {
					String str = CameraResultUtil.base64(bitmap);
					js.put("photo_base64", base64_str);
				}catch (Exception e) {
					e.getMessage();
					try {
						js.put("error", e.getMessage());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
				Cameralmpl.callBack.apply(js);
			}
		});
	}
	
	


}
