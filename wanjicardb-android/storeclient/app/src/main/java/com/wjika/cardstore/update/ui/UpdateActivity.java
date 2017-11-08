package com.wjika.cardstore.update.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.NetWorkUtil;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.update.service.UpdateService;
import com.wjika.cardstore.utils.CommonTools;
import com.wjika.cardstore.utils.ConfigUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.Date;

/**
 * Created by Liu_Zhichao on 2016/1/23 13:09.
 * 版本更新
 */
public class UpdateActivity extends Activity {

	public final static String KEY_UPDATE_TYPE = "force";
	public final static String KEY_UPDATE_URL = "url";
	public final static String KEY_UPDATE_VERSION_NAME = "version_name";
	public final static String KEY_UPDATE_VERSION_DESC = "version_desc";

	@ViewInject(R.id.txt_update_message)
	private TextView mTxtUpdateMessage;
	@ViewInject(R.id.update_layout)
	private View mViewTitle;
	@ViewInject(R.id.txt_update_title)
	private TextView mTxtUpdateTitle;
	@ViewInject(R.id.txt_update_version)
	private TextView mTxtUpdateVersion;
	@ViewInject(R.id.img_upate_alert)
	private ImageView mImgUpateAlert;
	@ViewInject(R.id.btn_ignore)
	private TextView mBtnIgnore;
	@ViewInject(R.id.btn_update)
	private TextView mBtnUpdate;

	private int type;
	private String apkUrl;
	private String versionName;
	private String desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_update_act);
		getWindow().addFlags(Window.FEATURE_NO_TITLE);
		ViewInjectUtils.inject(this);

		DisplayMetrics d = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(d);
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
		if (d.densityDpi <= 320) {
			p.height = (int) (d.heightPixels * 0.53);
			getWindow().setAttributes(p);
		}

		Intent startIntent = getIntent();
		if (startIntent != null) {
			type = startIntent.getIntExtra(KEY_UPDATE_TYPE, 0);
			apkUrl = startIntent.getStringExtra(KEY_UPDATE_URL);
			versionName = startIntent.getStringExtra(KEY_UPDATE_VERSION_NAME);
			desc = startIntent.getStringExtra(KEY_UPDATE_VERSION_DESC);
		}

		setFinishOnTouchOutside(type != 2);
		if (type == 2){//2为强制升级
			mTxtUpdateTitle.setVisibility(View.GONE);
			mTxtUpdateVersion.setVisibility(View.GONE);
			mImgUpateAlert.setVisibility(View.VISIBLE);
			mTxtUpdateMessage.setText(String.format(getResources().getString(R.string.can_update), versionName));
			mBtnIgnore.setVisibility(View.GONE);
			ConfigUtil.setIgnoreDate(UpdateActivity.this, 0);
		}else {
			mTxtUpdateTitle.setVisibility(View.VISIBLE);
			mTxtUpdateVersion.setVisibility(View.VISIBLE);
			mImgUpateAlert.setVisibility(View.GONE);
			mTxtUpdateVersion.setText(versionName);
			mTxtUpdateMessage.setText(desc);
			mBtnIgnore.setVisibility(View.VISIBLE);
		}

		mBtnIgnore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfigUtil.setIgnoreDate(UpdateActivity.this, new Date().getTime());
				finish();
			}
		});

		mBtnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtil.isEmpty(apkUrl)) return;
				if (!NetWorkUtil.isConnect(UpdateActivity.this)) {
					Toast.makeText(UpdateActivity.this, R.string.no_available_net, Toast.LENGTH_LONG).show();
					return;
				}

				String packageName = "com.android.providers.downloads";
				int state = getPackageManager().getApplicationEnabledSetting(packageName);
				if(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
						state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER ||
						state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED){
					//不能使用系统下载管理器
					startActivity(CommonTools.getIntent(UpdateActivity.this));
					 /*try {
					  //Open the specific App Info page:
					  Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					  i.setData(Uri.parse("package:" + packageName));
					  startActivity(i);
					 } catch ( ActivityNotFoundException e ) {
					  //Open the generic Apps page:
					  Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
					  startActivity(i);
					 }*/
				} else {
					mBtnUpdate.setEnabled(false);
					startService(new Intent().setClass(getApplicationContext(), UpdateService.class).putExtra(KEY_UPDATE_URL, apkUrl));
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (type == 2 && KeyEvent.KEYCODE_BACK == keyCode) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
