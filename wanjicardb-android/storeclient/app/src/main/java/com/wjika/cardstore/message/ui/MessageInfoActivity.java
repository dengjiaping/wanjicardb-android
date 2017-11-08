package com.wjika.cardstore.message.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.network.entities.MessageEntity;
import com.wjika.cardstore.utils.ViewInjectUtils;

/**
 * Created by Liu_Zhichao on 2016/1/13 18:03.
 * 消息通知详情
 */
public class MessageInfoActivity extends ToolBarActivity {

	@ViewInject(R.id.message_info_content)
	private TextView messageInfoContent;
	@ViewInject(R.id.message_info_date)
	private TextView messageInfoDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_info_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		MessageEntity messageEntity = getIntent().getParcelableExtra("message");
		if (messageEntity != null){
			setLeftTitle(messageEntity.getTitle());
			messageInfoContent.setText(messageEntity.getMessage());
			messageInfoDate.setText(messageEntity.getCreateDate());
		}
	}
}
