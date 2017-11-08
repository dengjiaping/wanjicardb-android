package com.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by songxudong
 * on 2015/6/22.
 */
public class Utils {

	/**
	 * @param ctx
	 * @return 是否为正式包  true 为正式包
	 */
	public static boolean isReleaseVersion(Context ctx) {
		boolean isRelease = false;

		PackageManager pm = ctx.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
			isRelease = (0 == (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (PackageManager.NameNotFoundException e) {
	    /*debuggable variable will remain false*/
		}

		return isRelease;
	}
}
