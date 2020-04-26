package com.cg.jsbridge.core.jsbridge.util;

public class GlobalParams {

	public static String PROTOCAL_HEAD="CGJSBridge";//协议头
	public static String CallJS_FORMAT="javascript:JSBridge.onFinish('%s', %s);";//nativa给JS的统一回掉
	
	/**
	 * 提供修改协议头的方法
	 * @return
	 */
	public static String getPROTOCAL_HEAD() {
		return PROTOCAL_HEAD;
	}

	public static void setPROTOCAL_HEAD(String pROTOCAL_HEAD) {
		PROTOCAL_HEAD = pROTOCAL_HEAD;
	}

	
	/**
	 * 允许修改native给JS的同意回
	 * @return
	 */
	public static String getCallJS_FORMAT() {
		return CallJS_FORMAT;
	}

	public static void setCallJS_FORMAT(String callJS_FORMAT) {
		CallJS_FORMAT = callJS_FORMAT;
	}
	
	
	

}
