package com.wjika.cardstore.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;

/**
 * Created by Liu_Zhichao on 2016/1/19 18:15.
 * 配置信息工具类
 */
public class ConfigUtil {

	public static final String CONFIG_PRE_KEY_HIDE_GUIDE = "pre_key_hide_guide";
	public static final String CONFIG_PRE_KEY_OLD_VERSION_CODE = "pre_key_old_version_code";
	public static final String CONFIG_PRE_KEY_IGNORE_DATE = "pre_key_ignore_date";
	public static final String CONFIG_PRE_KEY_IS_TEST = "config_pre_key_is_test";

	public static void setHideGuide(Context context, boolean isShow){
		PreferencesUtils.putBoolean(context, CONFIG_PRE_KEY_HIDE_GUIDE, isShow);
	}

	public static boolean isHideGuide(Context context){
		return PreferencesUtils.getBoolean(context, CONFIG_PRE_KEY_HIDE_GUIDE);
	}

	public static void setOldVersionCode(Context context, int versionCode){
		PreferencesUtils.putInt(context, CONFIG_PRE_KEY_OLD_VERSION_CODE, versionCode);
	}

	public static int getOldVersionCode(Context context){
		return PreferencesUtils.getInt(context, CONFIG_PRE_KEY_OLD_VERSION_CODE);
	}

	public static long getIgnoreDate(Context context){
		return PreferencesUtils.getLong(context, CONFIG_PRE_KEY_IGNORE_DATE);
	}

	public static void setIgnoreDate(Context context, long ignoreDate){
		PreferencesUtils.putLong(context, CONFIG_PRE_KEY_IGNORE_DATE, ignoreDate);
	}

	public static boolean isTestEnvironment(Context context){
		return PreferencesUtils.getBoolean(context, CONFIG_PRE_KEY_IS_TEST);
	}

	public static void setIsTestEnvironment(Context context, boolean isTest){
		PreferencesUtils.putBoolean(context, CONFIG_PRE_KEY_IS_TEST, isTest);
	}
}
