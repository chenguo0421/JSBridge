package com.cg.jsbridge.core.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.util.Log;


import com.cg.jsbridge.core.camera.AfterPermissionGranted;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtils {

//	public static final int SETTINGS_REQ_CODE = 16061;
//	private static final String TAG = "PermissionUtils";

	public static boolean hasPermissions(Context context, String[] perms) {
		if (Build.VERSION.SDK_INT < 23) {
			Log.w("EasyPermissions",
					"hasPermissions: API version < M, returning true by default");
			return true;
		}

		for (String perm : perms) {
			boolean hasPerm = ContextCompat.checkSelfPermission(context, perm) == 0;

			if (!(hasPerm)) {
				return false;
			}
		}

		return true;
	}

	@SuppressLint("ResourceType")
	public static void requestPermissions(Object object, String rationale,
                                          int requestCode, String[] perms) {
		requestPermissions(object, rationale, 17039370, 17039360, requestCode,
				perms);
	}

	public static void requestPermissions(final Object object, final String rationale,
										  @StringRes final int positiveButton, @StringRes final int negativeButton,
										  final int requestCode, final String[] perms) {
		checkCallingObjectSuitability(object);

		//是否需要弹出权限申请提示框
		boolean shouldShowRationale = false;
		//当用户手动关闭权限  或  拒绝权限并且选择不再提醒时，返回false
		//当用户选择拒绝权限，返回的true
		for (String perm : perms) {
			shouldShowRationale = (shouldShowRationale)
					|| (shouldShowRequestPermissionRationale(object, perm));
		}

		if (shouldShowRationale) {
			final Activity activity = getActivity(object);
			if (null == activity) {
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("确认");
			builder.setMessage(rationale);
			builder.setPositiveButton(positiveButton,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
//							((PermissionCallbacks) object)
//							.onPermissionsGranted(
//									requestCode,
//									Arrays.asList(perms));
//							checkDeniedPermissionsNeverAskAgain(object, rationale, requestCode, positiveButton, negativeButton, Arrays.asList(perms));
							Intent intent = new Intent(
									"android.settings.APPLICATION_DETAILS_SETTINGS");
							Uri uri = Uri.fromParts("package",
									activity.getPackageName(),
									null);
							intent.setData(uri);
							startAppSettingsScreen(object,intent);
						}
					});
			builder.setNegativeButton(negativeButton,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (object instanceof PermissionCallbacks)
								((PermissionCallbacks) object)
										.onPermissionsDenied(
												requestCode,
												Arrays.asList(perms));
						}
					});
			builder.show();
		} else {
			executePermissionsRequest(object, perms, requestCode);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void onRequestPermissionsResult(int requestCode,
                                                  String[] permissions, int[] grantResults, Object object) {
		checkCallingObjectSuitability(object);

		ArrayList granted = new ArrayList();
		ArrayList denied = new ArrayList();
		for (int i = 0; i < permissions.length; ++i) {
			String perm = permissions[i];
			if (grantResults[i] == 0)
				granted.add(perm);//授权成功
			else {
				denied.add(perm);//授权失败
			}

		}



		//有权限申请失败
		if ((!(denied.isEmpty())) && (object instanceof PermissionCallbacks)) {
			((PermissionCallbacks) object).onPermissionsDenied(requestCode,
					denied);
			return;
		}

		//		if ((!(granted.isEmpty())) && (object instanceof PermissionCallbacks)) {
		//			((PermissionCallbacks) object).onPermissionsGranted(requestCode,
		//					granted);
		//		}

		//权限申请成功
		if ((!(granted.isEmpty())) && (denied.isEmpty())) {
			((PermissionCallbacks) object).onPermissionsGranted(requestCode,
										granted);
			runAnnotatedMethods(object, requestCode);
			return;
		}
	}

	public static boolean checkDeniedPermissionsNeverAskAgain(Object object,
                                                              String rationale, @StringRes int requestCode, @StringRes int positiveButton,
                                                              @StringRes int negativeButton, List<String> deniedPerms) {
		return checkDeniedPermissionsNeverAskAgain(object, rationale,requestCode,
				positiveButton, negativeButton, null, deniedPerms);
	}

	public static boolean checkDeniedPermissionsNeverAskAgain(
			final Object object,
			String rationale,
			@StringRes final int requestCode,
			@StringRes int positiveButton,
			@StringRes int negativeButton,
			@Nullable DialogInterface.OnClickListener negativeButtonOnClickListener,
			final List<String> deniedPerms) {
		for (String perm : deniedPerms) {
			boolean shouldShowRationale = shouldShowRequestPermissionRationale(
					object, perm);
			if (!(shouldShowRationale)) {
				final Activity activity = getActivity(object);
				if (null == activity) {
					return true;
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("确认");
				builder.setMessage(rationale);
				builder.setPositiveButton(positiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
                                                int which) {
								Intent intent = new Intent(
										"android.settings.APPLICATION_DETAILS_SETTINGS");
								Uri uri = Uri.fromParts("package",
										activity.getPackageName(),
										null);
								intent.setData(uri);
								startAppSettingsScreen(object,intent);
							}
						});
				builder.setNegativeButton(negativeButton,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (object instanceof PermissionCallbacks)
									((PermissionCallbacks) object)
											.onPermissionsDenied(
													requestCode,
													deniedPerms);
							}
						});
				builder.show();

				return true;
			}
		}

		return false;
	}

	
	
	/**
	 *为了帮助查找用户可能需要解释的情形，Android 提供了一个实用程序方法，即 shouldShowRequestPermissionRationale()。
	 *如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
	 *注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。
	 *如果设备规范禁止应用具有该权限，此方法也会返回 false。
	 * @param object
	 * @param perm
	 * @return
	 */
	@TargetApi(23)
	private static boolean shouldShowRequestPermissionRationale(Object object,
                                                                String perm) {
		if (object instanceof Activity)
			return ActivityCompat.shouldShowRequestPermissionRationale(
					(Activity) object, perm);
		if (object instanceof Fragment)
			return ((Fragment) object)
					.shouldShowRequestPermissionRationale(perm);
		if (object instanceof android.app.Fragment) {
			return ((android.app.Fragment) object)
					.shouldShowRequestPermissionRationale(perm);
		}
		return false;
	}

	
	//执行权限请求
	//当权限在设置中被设置为禁止，即使申请当前权限也是无效的，会被直接拒绝
	//小米5s plus    android 6.0.1 MAB48T   MIUI 8.0  申请权限时只有允许和拒绝，当选择拒绝时，和设置中选择拒绝时一样的
	//
	@TargetApi(23)
	private static void executePermissionsRequest(Object object,
                                                  String[] perms, int requestCode) {
		checkCallingObjectSuitability(object);

		if (object instanceof Activity)
			ActivityCompat.requestPermissions((Activity) object, perms,
					requestCode);
		else if (object instanceof Fragment)
			((Fragment) object).requestPermissions(
					perms, requestCode);
		else if (object instanceof android.app.Fragment)
			((android.app.Fragment) object).requestPermissions(perms,
					requestCode);
	}

	@TargetApi(11)
	private static Activity getActivity(Object object) {
		if (object instanceof Activity)
			return ((Activity) object);
		if (object instanceof Fragment)
			return ((Fragment) object).getActivity();
		if (object instanceof android.app.Fragment) {
			return ((android.app.Fragment) object).getActivity();
		}
		return null;
	}

	@TargetApi(11)
	private static void startAppSettingsScreen(Object object, Intent intent) {
		if (object instanceof Activity)
			((Activity) object).startActivityForResult(intent, 16061);
		else if (object instanceof Fragment)
			((Fragment) object).startActivityForResult(
					intent, 16061);
		else if (object instanceof android.app.Fragment)
			((android.app.Fragment) object).startActivityForResult(intent,
					16061);
	}

	private static void runAnnotatedMethods(Object object, int requestCode) {
		Class<? extends Object> clazz = object.getClass();
		if (isUsingAndroidAnnotations(object)) {
			clazz = clazz.getSuperclass();
		}
		for (Method method : clazz.getDeclaredMethods()) {
			if (!(method.isAnnotationPresent(AfterPermissionGranted.class)))
				continue;
			AfterPermissionGranted ann = (AfterPermissionGranted) method
					.getAnnotation(AfterPermissionGranted.class);
			if (ann.value() != requestCode)
				continue;
			if (method.getParameterTypes().length > 0) {
				throw new RuntimeException("Cannot execute non-void method "
						+ method.getName());
			}

			try {
				if (!(method.isAccessible())) {
					method.setAccessible(true);
				}
				method.invoke(object, new Object[0]);
			} catch (IllegalAccessException e) {
				Log.e("EasyPermissions",
						"runDefaultMethod:IllegalAccessException", e);
			} catch (InvocationTargetException e) {
				Log.e("EasyPermissions",
						"runDefaultMethod:InvocationTargetException", e);
			}
		}
	}

	
	/**
	 * 判断当前页面是Activity或fragment
	 * @param object
	 */
	private static void checkCallingObjectSuitability(Object object) {
		boolean isActivity = object instanceof Activity;
		boolean isSupportFragment = object instanceof Fragment;
		boolean isAppFragment = object instanceof android.app.Fragment;
		boolean isMinSdkM = Build.VERSION.SDK_INT >= 23;
		//1.不是v4的fragment，2.不是activity 3.不是app的fragment或者sdk小于23，则抛出异常
		if ((!(isSupportFragment)) && (!(isActivity))&& (((!(isAppFragment)) || (!(isMinSdkM))))) {
			if (isAppFragment) {
				throw new IllegalArgumentException(
						"Target SDK needs to be greater than 23 if caller is android.app.Fragment");
			}

			throw new IllegalArgumentException(
					"Caller must be an Activity or a Fragment.");
		}
	}

	private static boolean isUsingAndroidAnnotations(Object object) {
		if (!(object.getClass().getSimpleName().endsWith("_"))) {
			return false;
		}
		try {
			Class<?> clazz = Class
					.forName("org.androidannotations.api.view.HasViews");
			return clazz.isInstance(object);
		} catch (ClassNotFoundException e) {
		}
		return false;
	}

	public static abstract interface PermissionCallbacks extends
			ActivityCompat.OnRequestPermissionsResultCallback {
		public abstract void onPermissionsGranted(int paramInt,
                                                  List<String> paramList);

		public abstract void onPermissionsDenied(int paramInt,
                                                 List<String> paramList);
	}



}
