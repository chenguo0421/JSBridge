package com.cg.jsbridge.core.jsbridge.web;

import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;


import com.cg.jsbridge.core.jsbridge.core.CallBack;
import com.cg.jsbridge.core.jsbridge.core.JSNativeCallBack;
import com.cg.jsbridge.core.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 
 * @author chenguo
 *
 */
public class CGWebViewInterface implements JSNativeCallBack {
	
	private WebActivity context;

	public CGWebViewInterface(WebActivity context) {
		this.context=context;
	}

	/**
	 * 扩展类，可根据该类扩展其它功能
	 * 由于该方法缺少webview所在acitivty载体参数，弃用
	 */
	@Override
	public void NativeCall(String method, String params, CallBack callBack) {
		// TODO Auto-generated method stub
//		Log.e("TAG","method="+Method);
		
		JSONObject obj=new JSONObject();
		try {
			obj.put("data","已使用扩展类调用方法:"+method);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			try {
				obj.put("error",e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		callBack.apply(obj);
	}

	/**
	 * 扩展类方法，Post请求自己扩展，可通过回调将服务器返回参数返还给JS
	 */
	@Override
	public void RequestPost(WebView view, String url, String params,
                            CallBack callBack) {
		// TODO Auto-generated method stub
		context.showMaskDialog();
		Toast.makeText(context,"post  params="+params+",url="+url, Toast.LENGTH_LONG).show();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				context.hideMaskDialog();
			}
		}, 3000);
	}

	
	/**
	 * 扩展类方法，Get请求自己扩展，可通过回调将服务器返回参数返还给JS
	 */
	@Override
	public void RequestGet(WebView view, String url, String params,
                           CallBack callBack) {
		// TODO Auto-generated method stub
		context.showMaskDialog();
		Toast.makeText(context,"get  params="+params+",url="+url, Toast.LENGTH_LONG).show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				context.hideMaskDialog();
			}
		}, 3000);
	}

	
	/**
	 * 扩展类，可根据该类扩展其它功能
	 */
	@Override
	public void NativeCall(WebView webView, final String method, String params,
                           final CallBack callBack) {
		// TODO Auto-generated method stub
		if ("showPickerView".equals(method)) {
			openPickerView(webView,callBack);
		}
		if ("showToast".equals(method)) {
			ToastUtils.show(context, "params", Toast.LENGTH_LONG);
			JSONObject obj=new JSONObject();
			try {
				obj.put("data","已使用扩展类调用方法:"+method);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				try {
					obj.put("error",e.getMessage());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			callBack.apply(obj);
		}
		if ("showDialog".equals(method)) {
			context.showMaskDialog();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					context.hideMaskDialog();
					JSONObject obj=new JSONObject();
					try {
						obj.put("data","已使用扩展类调用方法:"+method);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						try {
							obj.put("error",e.getMessage());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					callBack.apply(obj);
				}
			}, 3000);
		}
	}
	
	private void openPickerView(final WebView webView, final CallBack callback) {
		// TODO Auto-generated method stub
		ToastUtils.show(context,"show pickerview on native");
//		final ArrayList<TypeBean> arr = getTypeBean();
//		Util.alertBottomWheelOption(webView.getContext(),arr,new OnWheelViewClick() {
//
//			@Override
//			public void onClick(View view, int postion) {
////				Toast.makeText(activity,"option:"+postion,Toast.LENGTH_LONG).show();
////				callback.onResult("Y");
////				ToastUtils.show(activity,"option:"+postion);
//				JSONObject jsonObject = new JSONObject();
//				try {
//					jsonObject.put("value", arr.get(postion).getId()+"");
//					jsonObject.put("key", arr.get(postion).getName()+"");
//					ToastUtils.show(webView.getContext(), "选择内容："+arr.get(postion).getName(), Toast.LENGTH_LONG);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////				callback.onResult(jsonObject.toJSONString());
//				callback.apply(jsonObject);
//			}
//
//			@Override
//			public void onClick(View view, int postion, int position) {
////				Toast.makeText(activity,"option:"+beans.get(postion).getName()+",position1:"+beans.get(position).getName(),Toast.LENGTH_LONG).show();
////				ToastUtils.show(activity,"option:"+postion);
//			}
//		});
	}
	
//	private ArrayList<TypeBean> getTypeBean(){
//		ArrayList<TypeBean> arr =new ArrayList<TypeBean>();
//		arr.add(new TypeBean(0,"工商银行"));
//		arr.add(new TypeBean(1,"招商银行"));
//		arr.add(new TypeBean(2,"浦发银行"));
//		arr.add(new TypeBean(3,"华夏银行"));
//		arr.add(new TypeBean(4,"中国银行"));
//		return arr;
//	}


}
