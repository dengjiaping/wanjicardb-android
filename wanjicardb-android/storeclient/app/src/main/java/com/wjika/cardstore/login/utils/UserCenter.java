package com.wjika.cardstore.login.utils;

import android.content.Context;
import android.text.TextUtils;

import com.common.utils.PreferencesUtils;
import com.wjika.cardstore.network.entities.UserEntity;
import com.wjika.cardstore.utils.StringUtil;


/**
 * Created by Liu_Zhichao on 2016/1/11 17:37.
 * 个人信息
 */
public final class UserCenter {

	public static UserCenter instance = new UserCenter();

	public static final String USER_PRE_KEY_TOKEN = "user_pre_token";
	public static final String USER_PRE_KEY_USERNAME = "user_pre_username";
	public static final String USER_PRE_KEY_MERNAME = "user_pre_mername";
	public static final String USER_PRE_KEY_ISMAIN = "user_pre_is_main";
	public static final String USER_PRE_KEY_MOBILE = "user_pre_mobile";
	public static final String USER_PRE_KEY_ACCOUNT = "user_pre_account";

	private UserCenter(){
	}

	public static void saveLoginInfo(Context context, UserEntity userEntity){
		if (userEntity != null){
			PreferencesUtils.putString(context, USER_PRE_KEY_TOKEN, userEntity.getToken());
			PreferencesUtils.putString(context, USER_PRE_KEY_USERNAME, userEntity.getUserName());
			PreferencesUtils.putString(context, USER_PRE_KEY_MERNAME, userEntity.getMerchantName());
			PreferencesUtils.putBoolean(context, USER_PRE_KEY_ISMAIN, userEntity.isMain());
		}
	}

	public static void clearLoginInfo(Context context){
		PreferencesUtils.putString(context, USER_PRE_KEY_TOKEN, "");
		PreferencesUtils.putString(context, USER_PRE_KEY_USERNAME, "");
		PreferencesUtils.putString(context, USER_PRE_KEY_MERNAME, "");
		PreferencesUtils.putBoolean(context, USER_PRE_KEY_ISMAIN, false);
		PreferencesUtils.putString(context, USER_PRE_KEY_MOBILE, "");
	}

	public static boolean isLogin(Context context){
		String token = PreferencesUtils.getString(context, USER_PRE_KEY_TOKEN);
		return !TextUtils.isEmpty(token);
	}

	public static void saveMerMobile(Context context,String mobileNum){
		if(!StringUtil.isEmpty(mobileNum)){
			PreferencesUtils.putString(context, USER_PRE_KEY_MOBILE, mobileNum);
		}
	}

	public static String getMobile(Context context){
		return PreferencesUtils.getString(context, USER_PRE_KEY_MOBILE);
	}

	public static void saveAccount(Context context, String account) {
		PreferencesUtils.putString(context, USER_PRE_KEY_ACCOUNT, account);
	}

	public static String getAccount(Context context) {
		return PreferencesUtils.getString(context, USER_PRE_KEY_ACCOUNT);
	}

	public static String getToken(Context context){
		return PreferencesUtils.getString(context, USER_PRE_KEY_TOKEN);
	}

	public static String getMerName(Context context) {
		return PreferencesUtils.getString(context, USER_PRE_KEY_MERNAME);
	}

	public static boolean isMain(Context context){
		return PreferencesUtils.getBoolean(context, USER_PRE_KEY_ISMAIN);
	}

	public static boolean isHide(Context context) {
		return PreferencesUtils.getBoolean(context, getAccount(context));
	}

	public static void saveHide(Context context, boolean isHide) {
		PreferencesUtils.putBoolean(context, getAccount(context), isHide);
	}
}
