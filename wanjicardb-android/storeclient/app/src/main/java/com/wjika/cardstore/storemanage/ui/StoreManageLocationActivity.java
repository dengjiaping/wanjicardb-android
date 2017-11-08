package com.wjika.cardstore.storemanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by zhangzhaohui on 2016/1/13.
 * 输入所在地
 */
public class StoreManageLocationActivity extends ToolBarActivity implements View.OnClickListener {

	private static final int REQUEST_ACT_LOCATION = 100;
	private static final int REQUEST_NET_UPDATE = 200;

	@ViewInject(R.id.store_manage_location)
	private EditText storeManageLocation;
	@ViewInject(R.id.store_manage_map)
	private LinearLayout storeManageMap;
	@ViewInject(R.id.store_manage_save)
	private TextView storeManageSave;

	private String address;
	private double longitude;//经度
	private double latitude;//纬度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_location_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_manager_input_location));
		storeManageMap.setOnClickListener(this);
		storeManageSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_manage_map:
				address = storeManageLocation.getText().toString().trim();
				startActivityForResult(new Intent(this, LocationMapActivity.class).putExtra("address", address), REQUEST_ACT_LOCATION);
				break;
			case R.id.store_manage_save:
				address = storeManageLocation.getText().toString().trim();
				if (TextUtils.isEmpty(address) || 0 == longitude || 0 == latitude) {
					ToastUtil.shortShow(this, getString(R.string.store_mananger_tomap_select));
					return;
				} else {
					address = storeManageLocation.getText().toString().trim();
				}
				showProgressDialog();
				IdentityHashMap<String, String> param = new IdentityHashMap<>();
				param.put("merchantLinkman", "");
				param.put("merchantPhone", "");
				param.put("merchantAddress", address);
				param.put("merchantIntroduce", "");
				param.put("merchantBusinesshours", "");
				param.put("merchantLongitude", String.valueOf(longitude));
				param.put("merchantLatitude", String.valueOf(latitude));
				param.put("token", UserCenter.getToken(this));
				requestHttpData(Constants.Urls.URL_POST_UPDATE_INFO, REQUEST_NET_UPDATE, FProtocol.HttpMethod.POST, param);
				break;
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_UPDATE == requestCode) {
			setResult(RESULT_OK, new Intent().putExtra("address", address));
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && REQUEST_ACT_LOCATION == requestCode) {
			address = data.getStringExtra("address");
			longitude = data.getDoubleExtra("longitude", 0);
			latitude = data.getDoubleExtra("latitude", 0);
			if (!TextUtils.isEmpty(address)) {
				storeManageLocation.setText(address);
				storeManageLocation.setSelection(storeManageLocation.getText().length());
			}
		}
	}
}
