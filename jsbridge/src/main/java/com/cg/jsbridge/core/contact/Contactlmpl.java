package com.cg.jsbridge.core.contact;

import android.content.Context;
import android.content.Intent;

/**
 * 获取联系人号码
 * @author chenguo
 *
 */
public class Contactlmpl {
	
	public static TakePhoneNumSuccess success;
	
	public static void toContactBridgeActivity(Context contxt, TakePhoneNumSuccess callBack) {
		Contactlmpl.success=callBack;
		Intent intent = new Intent(contxt, ContactBridgeActivity.class);
		contxt.startActivity(intent);
	}
	
}
