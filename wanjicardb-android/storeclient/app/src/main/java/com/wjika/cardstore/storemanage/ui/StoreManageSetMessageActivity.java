package com.wjika.cardstore.storemanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.utils.ViewInjectUtils;

/**
 * Created by zhangzhaohui on 2016/1/13.
 * 设置联系人和座机
 */
public class StoreManageSetMessageActivity extends ToolBarActivity implements View.OnClickListener {

	public static final String EXTRA_FROM = "extra_from";
	public static final int LINKMAN = 0x1;
	public static final int PHONE = 0x2;

	@ViewInject(R.id.store_manage_input_linkman)
	private EditText storeManageInputLinkman;
	@ViewInject(R.id.store_manage_save)
	private TextView storeManageSave;

	private int from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_set_message_act);
		ViewInjectUtils.inject(this);
		from = getIntent().getIntExtra(EXTRA_FROM, 0);
		initView();
	}

	private void initView() {
		if (from == LINKMAN) {
			setLeftTitle(getString(R.string.store_manager_linkman));
			storeManageSave.setOnClickListener(this);
		} else {
			setLeftTitle(getString(R.string.store_manager_input_phone));
			storeManageSave.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_manage_save:
				String text = storeManageInputLinkman.getText().toString();
				Intent intent = new Intent();
				intent.putExtra(StoreManageDetailActivity.SAVE_LINKMAN, text);
				setResult(RESULT_OK, intent);
				finish();
				break;
		}
	}
}
