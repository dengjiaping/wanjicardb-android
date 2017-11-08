package com.common.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * @author songxudong
 */
public class DeviceUtil {
	private static int height = -1;
	private static int width = -1;
	private static float densityDpi = -1;
	private static float density = -1;
	//<uses-permission android:name="android.permission.READ_PHONE_STATE" />
	private static String deviceId = "";
	private static String model = android.os.Build.MODEL;
	private static String manufacturer = android.os.Build.MANUFACTURER;
	/**
	 * 当前设备的 IMEI
	 */
	public static String IMEI;

	/**
	 * 当前设备的 IMEI
	 */
	public static String MD5_KEY = "D93569608DFED17BA63EF17CCC60E93D";

	public static String getDeviceId(Context context) {
		if (TextUtils.isEmpty(deviceId)) {
			getValues(context);
		}
		return deviceId;
	}

	public static int getHeight(Context context) {
		if (height < 0) {
			getValues(context);
		}
		return height;
	}

	public static int getWidth(Context context) {
		if (width < 0) {
			getValues(context);
		}
		return width;
	}

	public static String getModel() {
		return model;
	}

	public static String getManufacturer() {
		return manufacturer;
	}

	public static float getDensityDpi(Context context) {
		if (densityDpi < 0) {
			getValues(context);
		}
		return densityDpi;
	}

	public static float getDensity(Context context) {
		if (density < 0) {
			getValues(context);
		}
		return density;
	}

	private static void getValues(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);//
		deviceId = tm.getDeviceId();
		DisplayMetrics display = new DisplayMetrics();
		display = context.getResources().getDisplayMetrics();
		width = display.widthPixels;
		height = display.heightPixels;
		densityDpi = display.densityDpi;
		density = display.density;
		/*
		 * System.out.println("width"+width);
		 * System.out.println("height"+height);
		 * System.out.println("densityDpi"+densityDpi);
		 * System.out.println("density"+density);
		 */
	}

	public static int px_to_dp(Context context, int pxValue) {
		return (int) (pxValue / getDensity(context) + 0.5f);
	}

	public static int dp_to_px(Context context, int dpValue) {
		return (int) (getDensity(context) * dpValue + 0.5f);
	}

	public static boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static String getAppUniqueToken(Context context) {
		try {
			IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			LogUtil.e("Envi", e.toString());
		}
		String result = IMEI;
		String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		if (IMEI == null || "".equals(IMEI)) {
			result = androidId;
		} else {
			result = result + androidId;
		}
		result = result + MD5_KEY;
		return result;
	}
}
