package com.cg.jsbridge.core.jsbridge.core;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.cg.jsbridge.core.jsbridge.camera.Cameralmpl;
import com.cg.jsbridge.core.jsbridge.common.Commonlmpl;
import com.cg.jsbridge.core.jsbridge.contact.Contactlmpl;
import com.cg.jsbridge.core.jsbridge.nativerequest.NativeHttpRequest;
import com.cg.jsbridge.core.jsbridge.util.GlobalParams;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class CGJSBridge {

	
	private static HashMap<String, HashMap<String, Method>> exposedMethods=new HashMap<String, HashMap<String, Method>>();
	
	public static void registerHandler(String exposedName, Class<? extends IBridge> cls){
		 if (!exposedMethods.containsKey(exposedName)) {
	            try {
	                exposedMethods.put(exposedName, getAllMethod(cls));
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	}

	/**
	 * 通过对象获取该类的属性
	 * @param injectedCls
	 * @return
	 */
	private static HashMap<String, Method> getAllMethod(
			Class<? extends IBridge> injectedCls) {
		 HashMap<String, Method> mMethodsMap = new HashMap<String, Method>();
	        Method[] methods = injectedCls.getDeclaredMethods();
	        for (Method method : methods) {
	            String name;
	            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || (name = method.getName()) == null) {
	                continue;
	            }
	            @SuppressWarnings("rawtypes")
				Class[] parameters = method.getParameterTypes();
	            if (null != parameters && parameters.length == 3) {
	                if (parameters[0] == WebView.class && parameters[1] == JSONObject.class && parameters[2] == CallBack.class) {
	                    mMethodsMap.put(name, method);
	                }
	            }
	        }
	       return mMethodsMap;
	}

	
	
	public static void registerDefualtHandler(JSNativeCallBack jsNativeCallBack){
//		CGJSBridge.registerHandler("CPCommon", Commonlmpl.class);
		NativeHttpRequest.addJSNativeCallBack(jsNativeCallBack);
		CGJSBridge.registerHandler("CPNativeHttp", NativeHttpRequest.class);
		CGJSBridge.registerHandler("CPCamera", Cameralmpl.class);
		CGJSBridge.registerHandler("CPContact", Contactlmpl.class);
		
		
		Commonlmpl.addJSNativeCallBack(jsNativeCallBack);
		CGJSBridge.registerHandler("CPNativeCall", Commonlmpl.class);
	}



	public static String callJava(WebView webView, String uriString) {
		// TODO Auto-generated method stub
		String methodName = "";
	
        String className = "";
        String param = "{}";
        String port = "";
        //判断协议头合法性
        if (!TextUtils.isEmpty(uriString) && uriString.startsWith(GlobalParams.PROTOCAL_HEAD)) {
            Uri uri = Uri.parse(uriString);
            className = uri.getHost();
            param = uri.getQuery();
            port = uri.getPort() + "";
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
            }
        }
        
        if (exposedMethods.containsKey(className)) {
            HashMap<String, Method> methodHashMap = exposedMethods.get(className);

            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                Method method = methodHashMap.get(methodName);
                if (method != null) {
                    try {
                        method.invoke(null, webView, new JSONObject(param), new CallBack(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
		return null;
	}
	

}
