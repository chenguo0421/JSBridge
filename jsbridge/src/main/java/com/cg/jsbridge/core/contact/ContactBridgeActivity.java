 package com.cg.jsbridge.core.contact;

 import android.annotation.SuppressLint;
 import android.content.Intent;
 import android.database.Cursor;
 import android.net.Uri;
 import android.os.Bundle;
 import android.provider.ContactsContract;
 import android.util.Log;
 import android.widget.Toast;


 import com.cg.jsbridge.core.framwork.core.CGActivity;
 import com.cg.jsbridge.core.util.Constant;
 import com.cg.jsbridge.core.util.PermissionUtils;
 import com.cg.jsbridge.core.util.ToastUtils;

 import java.util.HashMap;
 import java.util.List;


 public class ContactBridgeActivity extends CGActivity implements PermissionUtils.PermissionCallbacks {
     public static final String TAG = "ContactBridgeActivity";
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         // TODO Auto-generated method stub
         super.onCreate(savedInstanceState);
         requestPermission();
     }

     /**
      * 请求联系人权限
      */
     @SuppressLint("NewApi")
     private void requestPermission() {
         // TODO Auto-generated method stub
         String[] perms = {"android.permission.READ_CONTACTS" };
         if (PermissionUtils.hasPermissions(this, perms)) {
             toContact();
         } else
             PermissionUtils.requestPermissions(this, "这个程序需要访问你的联系人权限", Constant.PermissionRequestCode,
                     perms);
     }




     /**
      * 权限操作结果处理
      */
     @Override
     public void onRequestPermissionsResult(int requestCode,
                                            String[] permissions, int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         PermissionUtils.onRequestPermissionsResult(requestCode, permissions,
                 grantResults, this);
     }








     private void toContact() {
         // TODO Auto-generated method stub
          Intent intent = new Intent(Intent.ACTION_PICK,
                  ContactsContract.Contacts.CONTENT_URI);
          startActivityForResult(intent, 1);
     }



     @SuppressWarnings("deprecation")
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         // TODO Auto-generated method stub
         super.onActivityResult(requestCode, resultCode, data);
          switch (requestCode) {
             case 1:
                 if (resultCode == RESULT_OK) {
                     Uri contactData = data.getData();
                     Cursor cursor = managedQuery(contactData, null, null, null,
                             null);
                     cursor.moveToFirst();
                     HashMap<String, String> map = getContactPhone(cursor);
                     if (Contactlmpl.success!=null){
                         Contactlmpl.success.takeContactSuccess(map.get("name"),map.get("number"));
                     }
                     finish();
                     return;
                 }
                 break;

             default:
                 break;
             }


             if (resultCode != 0)
                 return;
             if (data == null) {
                 finish();
                 return;
             }
             finish();
     }


      private HashMap<String, String> getContactPhone(Cursor cursor) {
             // TODO Auto-generated method stub
             int phoneColumn = cursor
                     .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
             int phoneNum = cursor.getInt(phoneColumn);
             String number = "";
             String name="";
          HashMap<String, String> map=new HashMap<>();
             if (phoneNum > 0) {

                 // 获得联系人的ID号
                 int nameColumn=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                 int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                 String contactId = cursor.getString(idColumn);
                 name=cursor.getString(nameColumn);
                 // 获得联系人电话的cursor
                 Cursor phone = getContentResolver().query(
                         ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                         null,
                         ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                 + contactId, null, null);
                 if (phone.moveToFirst()) {
                     for (; !phone.isAfterLast(); phone.moveToNext()) {
                         int index = phone
                                 .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                         int typeindex = phone
                                 .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                         @SuppressWarnings("unused")
                         int phone_type = phone.getInt(typeindex);
                         String phoneNumber = phone.getString(index);
                         number = phoneNumber;
 //	                  switch (phone_type) {//此处请看下方注释
 //	                  case 2:
 //	                      result = phoneNumber;
 //	                      break;
     //
 //	                  default:
 //	                      break;
 //	                  }
                     }
                     if (!phone.isClosed()) {
                         phone.close();
                     }
                 }
             }
             map.put("name",name);
             map.put("number",number);
             return map;
         }

     @Override
     public void onPermissionsGranted(int paramInt, List<String> paramList) {
         // TODO Auto-generated method stub
 //		LogUtil.d("TAG","success");
 //		String[] perms = {"android.permission.READ_CONTACTS" };
 //		if (PermissionUtils.hasPermissions(this, perms)) {
             toContact();
 //		}else {
 //			//用户拒绝权限
 //			ToastUtils.show(baseAt, "缺少联系人权限，请在设置中打开联系人权限!", Toast.LENGTH_LONG);
 //			finish();
 //
 //		}
     }

     @Override
     public void onPermissionsDenied(int paramInt, List<String> paramList) {
         // TODO Auto-generated method stub
         Log.d("TAG","faile");
         ToastUtils.show(this, "缺少联系人权限，请在设置中打开联系人权限!", Toast.LENGTH_LONG);
         finish();
     }


 }
