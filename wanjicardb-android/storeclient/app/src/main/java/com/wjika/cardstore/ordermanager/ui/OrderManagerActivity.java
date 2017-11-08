package com.wjika.cardstore.ordermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.ordermanager.adapter.OrderManagerViewAdapter;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkkkk on 2016/1/25.
 * 订单管理主界面
 */
public class OrderManagerActivity extends ToolBarActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

	public static final int ORDER_ALL = 0X01;
	public static final int ORDER_FINISH = 0X02;
	public static final int ORDER_READY = 0X03;
	public static final int ORDER_CLOSE = 0X04;

	@ViewInject(R.id.order_manager_content)
	private ViewPager orderManageContent;
	@ViewInject(R.id.order_manager_radiogroup)
	private RadioGroup orderRadioGroup;
	@ViewInject(R.id.order_button_all)
	private RadioButton orderRadioButtonAll;
	@ViewInject(R.id.order_button_finish)
	private RadioButton orderRadioButtonFinish;
	@ViewInject(R.id.order_button_readypay)
	private RadioButton orderRadioButtonReadyPay;
	@ViewInject(R.id.order_button_close)
	private RadioButton orderRadioButtonClose;
	@ViewInject(R.id.rigth_text)
	private TextView orderTextSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_manager_act);
		initView();
		initFragments();
	}

	private void initView() {
		setLeftTitle(getString(R.string.home_frag_order_manager));
		ViewInjectUtils.inject(this);
		orderTextSearch.setVisibility(View.VISIBLE);
		orderTextSearch.setBackgroundResource(R.drawable.search);
		orderTextSearch.setOnClickListener(this);
		orderRadioGroup.check(R.id.order_button_all);
		orderRadioButtonAll.setTextColor(getResources().getColor(R.color.home_title_bg));
	}

	private void initFragments() {
		List<OrderManagerFragment> fragments = new ArrayList<>();
		OrderManagerFragment allFragment = new OrderManagerFragment();
		allFragment.setOrderArg(ORDER_ALL);
		OrderManagerFragment finishFragment = new OrderManagerFragment();
		finishFragment.setOrderArg(ORDER_FINISH);
		OrderManagerFragment readyPayFragment = new OrderManagerFragment();
		readyPayFragment.setOrderArg(ORDER_READY);
		OrderManagerFragment closedFragment = new OrderManagerFragment();
		closedFragment.setOrderArg(ORDER_CLOSE);
		fragments.add(allFragment);
		fragments.add(finishFragment);
		fragments.add(readyPayFragment);
		fragments.add(closedFragment);
		OrderManagerViewAdapter viewAdapter = new OrderManagerViewAdapter(getSupportFragmentManager(), fragments);
		orderManageContent.setAdapter(viewAdapter);
		orderManageContent.addOnPageChangeListener(this);
		orderRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		initTextColor();
		switch (position) {
			case 0:
				orderRadioGroup.check(R.id.order_button_all);
				setTextCheckedColor(orderRadioButtonAll);
				break;
			case 1:
				orderRadioGroup.check(R.id.order_button_finish);
				setTextCheckedColor(orderRadioButtonFinish);
				break;
			case 2:
				orderRadioGroup.check(R.id.order_button_readypay);
				setTextCheckedColor(orderRadioButtonReadyPay);
				break;
			case 3:
				orderRadioGroup.check(R.id.order_button_close);
				setTextCheckedColor(orderRadioButtonClose);
				break;
		}
	}

	/**
	 * 设定字体的初始颜色
	 */
	private void initTextColor() {
		orderRadioButtonAll.setTextColor(getResources().getColor(R.color.order_text));
		orderRadioButtonFinish.setTextColor(getResources().getColor(R.color.order_text));
		orderRadioButtonReadyPay.setTextColor(getResources().getColor(R.color.order_text));
		orderRadioButtonClose.setTextColor(getResources().getColor(R.color.order_text));
	}

	/**
	 * 设定字体的被选中的颜色
	 */
	private void setTextCheckedColor(RadioButton radioButton) {
		radioButton.setTextColor(getResources().getColor(R.color.home_title_bg));
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		initTextColor();
		switch (checkedId) {
			case R.id.order_button_all:
				setTextCheckedColor(orderRadioButtonAll);
				orderManageContent.setCurrentItem(0);
				break;
			case R.id.order_button_finish:
				setTextCheckedColor(orderRadioButtonFinish);
				orderManageContent.setCurrentItem(1);
				break;
			case R.id.order_button_readypay:
				setTextCheckedColor(orderRadioButtonReadyPay);
				orderManageContent.setCurrentItem(2);
				break;
			case R.id.order_button_close:
				setTextCheckedColor(orderRadioButtonClose);
				orderManageContent.setCurrentItem(3);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rigth_text:
				startActivity(new Intent(this, OrderSearchActivity.class));
				break;
		}
	}
}
