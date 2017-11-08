package com.wjika.cardstore.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.commoditymanage.ui.CommodityManageActivity;
import com.wjika.cardstore.consumption.ui.ConsumptionListActivity;
import com.wjika.cardstore.financemanage.ui.FinanceManageActivity;
import com.wjika.cardstore.home.ui.CheckstandActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.MerDataEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.ordermanager.ui.OrderManagerActivity;
import com.wjika.cardstore.storemanage.ui.StoreManageActivity;
import com.wjika.cardstore.utils.NumberFormatUtil;
import com.zbar.lib.CaptureActivity;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/7 11:22.
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{

	private static final int REQUEST_CODE_STORE_DATA = 100;
	public static final int REQUEST_ACT_SCAN_CODE = 200;

	private TextView homeOrderSum;
	private TextView homeSellAmount;
	private ImageView rightButton;
	private int newOrder;
	private double todayIncome;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return initView(inflater);
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.home_frag, null);
		view.findViewById(R.id.base_titlebar).setBackgroundColor(getResources().getColor(R.color.home_title_bg));
		view.findViewById(R.id.left_button).setVisibility(View.INVISIBLE);

		TextView toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(UserCenter.getMerName(getActivity()));
		toolbarTitle.setVisibility(View.VISIBLE);
		homeOrderSum = (TextView) view.findViewById(R.id.home_order_sum);
		homeSellAmount = (TextView) view.findViewById(R.id.home_sell_amount);
		rightButton = (ImageView) view.findViewById(R.id.right_button);
		if (UserCenter.isHide(getActivity())){
			rightButton.setImageResource(R.drawable.home_data_hide);
			homeOrderSum.setText(getString(R.string.home_frag_not_see));
			homeSellAmount.setText(getString(R.string.home_frag_not_see));
		}else {
			rightButton.setImageResource(R.drawable.home_data_show);
		}
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setOnClickListener(this);


		view.findViewById(R.id.home_scan).setOnClickListener(this);
		view.findViewById(R.id.home_order_manager).setOnClickListener(this);
		view.findViewById(R.id.home_consume_manager).setOnClickListener(this);
		view.findViewById(R.id.home_product_manager).setOnClickListener(this);
		view.findViewById(R.id.home_finance_manager).setOnClickListener(this);
		view.findViewById(R.id.home_store_manager).setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	private void refreshData() {
		requestHttpData(Constants.Urls.URL_GET_STORE_DATA, REQUEST_CODE_STORE_DATA, FProtocol.HttpMethod.POST, new IdentityHashMap<String, String>());
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_CODE_STORE_DATA == requestCode){
			MerDataEntity merDataEntity = Parsers.parseMerData(data);
			if (merDataEntity != null){
				newOrder = merDataEntity.getCardOrderNumber();
				todayIncome = merDataEntity.getCardSalePrice();
				MainActivity.amount = merDataEntity.getFinanceMoney();
				if (UserCenter.isHide(getActivity())){
					homeOrderSum.setText(getString(R.string.home_frag_not_see));
					homeSellAmount.setText(getString(R.string.home_frag_not_see));
				}else {
					homeOrderSum.setText(String.valueOf(newOrder));
					homeSellAmount.setText(NumberFormatUtil.formatMoney(todayIncome));
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.right_button:
				if (UserCenter.isHide(getActivity())){
					UserCenter.saveHide(getActivity(), false);
					rightButton.setImageResource(R.drawable.home_data_show);
					homeOrderSum.setText(String.valueOf(newOrder));
					homeSellAmount.setText(NumberFormatUtil.formatMoney(todayIncome));
				}else {
					UserCenter.saveHide(getActivity(), true);
					rightButton.setImageResource(R.drawable.home_data_hide);
					homeOrderSum.setText(getString(R.string.home_frag_not_see));
					homeSellAmount.setText(getString(R.string.home_frag_not_see));
				}
				break;
			case R.id.home_scan://收款
				startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_ACT_SCAN_CODE);
				break;
			case R.id.home_order_manager://订单管理
				startActivity(new Intent(getActivity(), OrderManagerActivity.class));
				break;
			case R.id.home_consume_manager://消费管理
				startActivity(new Intent(getActivity(), ConsumptionListActivity.class));
				break;
			case R.id.home_product_manager://产品管理
				startActivity(new Intent(getActivity(), CommodityManageActivity.class));
				break;
			case R.id.home_finance_manager://财务管理
				if(UserCenter.isMain(getActivity())){
					startActivity(new Intent(getActivity(), FinanceManageActivity.class));
				}else {
					ToastUtil.shortShow(getActivity(),getString(R.string.head_office_operation));
				}
				break;
			case R.id.home_store_manager://店铺管理
				startActivity(new Intent(getActivity(), StoreManageActivity.class));
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK == resultCode && REQUEST_ACT_SCAN_CODE == requestCode && data != null){
			startActivity(new Intent(getActivity(), CheckstandActivity.class)
					.putExtra(CaptureActivity.SCAN_RESULT,data.getStringExtra(CaptureActivity.SCAN_RESULT)));
		}
	}
}
