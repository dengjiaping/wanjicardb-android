package com.wjika.cardstore.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.utils.ViewInjectUtils;

/**
 * Created by Liu_Zhichao on 2016/1/24 20:09.
 * 交易结果页
 */
public class DealActivity extends ToolBarActivity {

	public static final String IS_SUCCESS = "is_success";
	public static final String FAILED_REASON = "failed_reason";
	public static final String SUCCESS_ACCOUNT = "success_account";
	public static final String SUCCESS_AMOUNT = "success_amount";

	@ViewInject(R.id.deal_result)
	private TextView dealResult;
	@ViewInject(R.id.deal_failed_reason)
	private TextView dealFailedReason;
	@ViewInject(R.id.deal_account_all)
	private RelativeLayout dealAccountAll;
	@ViewInject(R.id.deal_account)
	private TextView dealAccount;
	@ViewInject(R.id.deal_amount_all)
	private RelativeLayout dealAmountAll;
	@ViewInject(R.id.deal_amount)
	private TextView dealAmount;
	@ViewInject(R.id.deal_close)
	private TextView dealClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_deal_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		mTxtLeft.setText(getString(R.string.home_trading_result));
		mTxtLeft.setVisibility(View.VISIBLE);
		mBtnTitleLeft.setVisibility(View.GONE);

		Intent intent = getIntent();
		boolean isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);
		if (isSuccess){
			dealAccount.setText(intent.getStringExtra(SUCCESS_ACCOUNT));
			dealAmount.setText(String.format(getString(R.string.commodity_rmb_sign), intent.getStringExtra(SUCCESS_AMOUNT)));
		} else {
			dealResult.setText(getString(R.string.home_payment_failure));
			dealResult.setCompoundDrawablesWithIntrinsicBounds(R.drawable.deal_failed_icon,0,0,0);
			dealFailedReason.setText(String.format(getString(R.string.home_failure_reason), intent.getStringExtra(FAILED_REASON)));
			dealFailedReason.setVisibility(View.VISIBLE);
			dealAccountAll.setVisibility(View.GONE);
			dealAmountAll.setVisibility(View.GONE);
		}
		dealClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
