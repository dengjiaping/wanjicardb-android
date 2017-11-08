package com.wjika.cardstore.storemanage.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.UserDetailEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by admin on 2015/12/1.
 * 店铺管理
 */
public class StoreManageActivity extends ToolBarActivity implements View.OnClickListener {

	public static final int REQUEST_CODE_STORE_DETAIL = 0x1;
	public static final int REQUEST_CODE_STORE_OFF = 0x2;
	public static final int REQUEST_CODE_STORE_ON = 0x3;
	private static final int REQUEST_ACT_STORE_DETAIL = 0x4;

	@ViewInject(R.id.store_manage_store_message)
	private LinearLayout storeManageStoreMessage;
	@ViewInject(R.id.store_manage_photo_control)
	private LinearLayout storeManagePhotoControl;
	@ViewInject(R.id.store_manage_store_on_off)
	private TextView storeManageStoreOnOff;
	@ViewInject(R.id.store_manage_on_off)
	private LinearLayout storeManageOnOff;

	private AlertDialog alertDialog;
	private UserDetailEntity userDetailEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_act);
		ViewInjectUtils.inject(this);
		initView();
		initData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.home_frag_store_manager));
		//如果是总店隐藏店铺开关
		if (UserCenter.isMain(this)) {
			storeManageOnOff.setVisibility(View.INVISIBLE);
		}
		storeManageStoreMessage.setOnClickListener(this);
		storeManagePhotoControl.setOnClickListener(this);
		storeManageStoreOnOff.setOnClickListener(this);
	}

	private void initData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_STORE_INFO, REQUEST_CODE_STORE_DETAIL, FProtocol.HttpMethod.POST, new IdentityHashMap<String, String>());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_manage_store_message:
				Intent intent = new Intent(this, StoreManageDetailActivity.class);
				intent.putExtra("userEntity", userDetailEntity);
				startActivityForResult(intent, REQUEST_ACT_STORE_DETAIL);
				break;
			case R.id.store_manage_photo_control:
				startActivity(new Intent(this, StoreManagePhotoActivity.class));
				break;
			case R.id.store_manage_store_on_off:
				if (userDetailEntity != null) {
					View dialogView = View.inflate(this, R.layout.custom_dialog_view, null);
					TextView cancel = (TextView) dialogView.findViewById(R.id.dialog_cancle);
					TextView ensure = (TextView) dialogView.findViewById(R.id.dialog_confirm);
					TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
					cancel.setText(getString(R.string.cancel));
					ensure.setText(R.string.button_ok);
					if (userDetailEntity.getMerchantStatus() == 0) {
						alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();
						title.setText(getString(R.string.store_manager_is_open));
						dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
							}
						});
						dialogView.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								setStoreSwitch(1);
								alertDialog.dismiss();
							}
						});
						alertDialog.show();
					} else if (userDetailEntity.getMerchantStatus() == 1) {
						alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();
						title.setText(getString(R.string.store_manager_is_close));
						dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
							}
						});
						dialogView.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								setStoreSwitch(0);
								alertDialog.dismiss();
							}
						});
						alertDialog.show();
					}
				}
				break;
		}
	}

	private void setStoreSwitch(int switchValue) {
		if (switchValue == 0) {
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("merchantStatus", String.valueOf(switchValue));
			requestHttpData(Constants.Urls.URL_POST_MERCHANT_UPDATESHOP, REQUEST_CODE_STORE_OFF, FProtocol.HttpMethod.POST, param);
		} else if (switchValue == 1) {
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("merchantStatus", String.valueOf(switchValue));
			requestHttpData(Constants.Urls.URL_POST_MERCHANT_UPDATESHOP, REQUEST_CODE_STORE_ON, FProtocol.HttpMethod.POST, param);
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		switch (requestCode) {
			case REQUEST_CODE_STORE_DETAIL:
				userDetailEntity = Parsers.parseUserDetail(data);
				if (userDetailEntity != null) {
					if (userDetailEntity.getMerchantStatus() == 0) {
						storeManageStoreOnOff.setSelected(true);
					} else if (userDetailEntity.getMerchantStatus() == 1) {
						storeManageStoreOnOff.setSelected(false);
					}
				}
				break;
			case REQUEST_CODE_STORE_OFF:
				storeManageStoreOnOff.setSelected(true);
				ToastUtil.showToast(StoreManageActivity.this, getString(R.string.store_manager_close));
				userDetailEntity.setMerchantStatus(0);
				break;
			case REQUEST_CODE_STORE_ON:
				storeManageStoreOnOff.setSelected(false);
				ToastUtil.showToast(StoreManageActivity.this, getString(R.string.store_manager_open));
				userDetailEntity.setMerchantStatus(1);
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		storeManageStoreMessage.setClickable(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && REQUEST_ACT_STORE_DETAIL == requestCode) {
			UserDetailEntity user = data.getParcelableExtra("userEntity");
			if (user != null) {
				userDetailEntity = user;
			}
		}
	}
}
