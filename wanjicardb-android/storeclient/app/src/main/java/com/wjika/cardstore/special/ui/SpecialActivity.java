package com.wjika.cardstore.special.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.utils.ConfigUtil;

/**
 * Created by Liu_Zhichao on 2016/1/28 14:36.
 * 彩蛋界面
 */
public class SpecialActivity extends ToolBarActivity implements View.OnClickListener{

	private TextView specialCurrent;
	private TextView specialToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_act);
		initView();
	}

	private void initView() {
		setLeftTitle("Kikyou");
		specialCurrent = (TextView) findViewById(R.id.special_current);
		specialToggle = (TextView) findViewById(R.id.special_toggle);

		if (ConfigUtil.isTestEnvironment(this)){
			specialCurrent.setText("当前环境：测试环境");
		} else {
			specialCurrent.setText("当前环境：正式环境");
		}
		specialToggle.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.special_toggle:
				break;
		}
	}
}
