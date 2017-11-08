package com.wjika.cardstore.launcher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseActivity;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.utils.CommonTools;
import com.wjika.cardstore.utils.ConfigUtil;
import com.wjika.cardstore.utils.ExitManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Liu_ZhiChao on 2015/11/27 11:23.
 * 启动页
 */
public class LauncherActivity extends BaseActivity{

	private static final long LAUNCH_MIN_TIME = 2000L;

	private long mLaunchTime;
	private int versionCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLaunchTime = SystemClock.elapsedRealtime();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_act);
		ExitManager.instance.remove(this);//nohistory属性的activity不在ExitManager里记录
		gotoActivity();
	}

	private void gotoActivity() {
		long elapsed = SystemClock.elapsedRealtime() - mLaunchTime;
		if (elapsed >= LAUNCH_MIN_TIME){
			performGotoActivity();
		}else {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					if (LauncherActivity.this.isFinishing()) {
						return;
					}
					cancel();
					performGotoActivity();
				}
			}, LAUNCH_MIN_TIME - elapsed);
		}
	}

	private void performGotoActivity() {
		if (isNewVersion()){
			ConfigUtil.setOldVersionCode(this, versionCode);
			startActivity(new Intent(this, GuideActivity.class));
		}else {
			if (ConfigUtil.isHideGuide(this)){
				if (UserCenter.isLogin(this)){
					startActivity(new Intent(this, MainActivity.class));
				}else {
					startActivity(new Intent(this, LoginActivity.class));
				}
			}else {
				startActivity(new Intent(this, GuideActivity.class));
			}
		}
	}

	private boolean isNewVersion() {
		versionCode = CommonTools.getVersion(this);
		return ConfigUtil.getOldVersionCode(this) < versionCode;
	}
}
