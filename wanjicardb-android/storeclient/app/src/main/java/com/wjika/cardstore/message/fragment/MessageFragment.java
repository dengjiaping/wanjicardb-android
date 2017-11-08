package com.wjika.cardstore.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.message.ui.MessageListActivity;

/**
 * Created by Liu_Zhichao on 2016/1/7 01:06.
 * 动态、消息
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener{

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return initView(inflater);
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.message_frag, null);

		view.findViewById(R.id.left_button).setVisibility(View.INVISIBLE);
		TextView leftText = (TextView) view.findViewById(R.id.left_text);
		leftText.setText(getString(R.string.main_dynamic));
		leftText.setVisibility(View.VISIBLE);

		view.findViewById(R.id.message_order).setOnClickListener(this);
		view.findViewById(R.id.message_finance).setOnClickListener(this);
		view.findViewById(R.id.message_system).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.message_order://订单消息
				break;
			case R.id.message_finance://财务消息
				break;
			case R.id.message_system://系统消息
				startActivity(new Intent(getActivity(), MessageListActivity.class));
				break;
		}
	}
}
