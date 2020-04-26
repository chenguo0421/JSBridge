package com.cg.jsbridge.core.camera;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import androidx.core.content.FileProvider;


/**
 * 注意：此处的cn.com.cg.base.fileprovider的结构为： 包名+fileprovider
 * @author chenguo
 *
 */
public class CGBaseFileProvider extends FileProvider {
	public static Uri CGGetUriForFile(Context context, File file) {
		// TODO Auto-generated method stub
		return FileProvider.getUriForFile(context.getApplicationContext(),
				context.getPackageName()+".CGCamera.fileprovider", file);
	}
}
