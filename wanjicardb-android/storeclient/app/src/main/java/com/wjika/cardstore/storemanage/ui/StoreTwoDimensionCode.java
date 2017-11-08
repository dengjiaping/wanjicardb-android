package com.wjika.cardstore.storemanage.ui;

import android.os.Bundle;
import android.view.View;

import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseActivity;
import com.wjika.cardstore.storemanage.fragment.QRCodeFragment;

/**
 * Created by zhangzhaohui on 2016/1/13.
 * 店铺二维码
 */
public class StoreTwoDimensionCode extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_two_dimension_code_act);
		int merchantId = getIntent().getIntExtra("merchantId", 0);
		QRCodeFragment qrCodeFragment = new QRCodeFragment();
		qrCodeFragment.setArgs(this, merchantId);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, qrCodeFragment).commit();
		findViewById(R.id.toolbar_left_title).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
