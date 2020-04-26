package com.cg.jsbridge.core.jsbridge.contact;

import android.content.Context;
import android.webkit.WebView;


import com.cg.jsbridge.core.contact.TakePhoneNumSuccess;
import com.cg.jsbridge.core.jsbridge.core.CallBack;
import com.cg.jsbridge.core.jsbridge.core.IBridge;

import org.json.JSONException;
import org.json.JSONObject;

public class Contactlmpl implements IBridge {

	public static CallBack callBack;

	public static void getContact(WebView webView, JSONObject param, final CallBack callback){
		callBack=callback;
		toContactBridgeActivity(webView.getContext());
	}
	
	
	
	public static void toContactBridgeActivity(Context contxt) {
//		Intent intent = new Intent(contxt, ContactBridgeActivity.class);
//		contxt.startActivity(intent);
		com.cg.jsbridge.core.contact.Contactlmpl.toContactBridgeActivity(contxt, new TakePhoneNumSuccess() {
			
			@Override
			public void takeContactSuccess(String name, String phoneNum) {
				// TODO Auto-generated method stub
				if (Contactlmpl.callBack!=null) {
					 JSONObject js=new JSONObject();
		                try {
							js.put("phone", phoneNum);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							try {
								js.put("error", e.getMessage());
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
		                Contactlmpl.callBack.apply(js);
				}
			}
		});
	}
	
	 

}
