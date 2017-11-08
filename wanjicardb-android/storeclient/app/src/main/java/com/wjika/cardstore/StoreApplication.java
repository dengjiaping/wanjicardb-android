package com.wjika.cardstore;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.common.utils.DeviceUtil;
import com.common.utils.LogUtil;
import com.common.utils.NetWorkUtil;
import com.common.utils.StringUtil;
import com.common.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.wjika.cardstore.utils.PackerNg;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Liu_ZhiChao on 2015/11/17 14:47.
 * application
 */
public class StoreApplication extends Application {

	public static final String CHANNEL_KEY = "UMENG_CHANNEL";

	@Override
	public void onCreate() {
		super.onCreate();
		MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "55a367c167e58ea01c000688", getChannel(this)));
		initBugly();
		SDKInitializer.initialize(this);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		JPushInterface.setAlias(this, DeviceUtil.getDeviceId(this), null);
		NetWorkUtil.syncConnectState(this);
		LogUtil.isDebug = !Utils.isReleaseVersion(this);
		if (LogUtil.isDebug){
			Stetho.initialize(Stetho
					.newInitializerBuilder(this)
					.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
					.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
		}
		//图片加载初始化
		Fresco.initialize(this);
		//需要在应用主进程中初始化的操作
		if (isMainProcess()){

		}
	}

	private void initBugly() {
		CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
		userStrategy.setAppChannel(getChannel(this));
		userStrategy.setAppVersion(getAppVersion(this));
		CrashReport.initCrashReport(this, "900007968", false, userStrategy);
		CrashReport.setUserId(DeviceUtil.getDeviceId(this));
	}

	public boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = android.os.Process.myPid();
		for (ActivityManager.RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取渠道信息，兼容两种打包方式
	 */
	public static String getChannel(Context context) {
		String channel = PackerNg.getMarket(context);
		if (TextUtils.isEmpty(channel)) {
			channel = getAppMetaData(context, CHANNEL_KEY);
		}
		return channel;
	}

	/**
	 * 获取application中指定的meta-data
	 * @return 如果没有获取成功(没有对应值或者异常)，则返回值为空
	 */
	public static String getAppMetaData(Context ctx, String key) {
		String resultData = "test";
		if (ctx == null || StringUtil.isEmpty(key)) {
			return resultData;
		}
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key) == null ? "test" : applicationInfo.metaData.getString(key);
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return resultData;
	}

	public static String getAppVersion(Context context){
		PackageManager pm = context.getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
