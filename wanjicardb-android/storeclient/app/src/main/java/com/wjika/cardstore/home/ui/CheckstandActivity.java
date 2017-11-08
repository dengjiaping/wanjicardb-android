package com.wjika.cardstore.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.Entity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.InputMethodUtil;
import com.wjika.cardstore.utils.MoneyFormatUtil;
import com.wjika.cardstore.utils.NumberFormatUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;
import com.zbar.lib.CaptureActivity;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/24 20:06.
 * 收银台
 */
public class CheckstandActivity extends ToolBarActivity {

	private static final int REQUEST_NET_CONSUMPTION = 100;

	@ViewInject(R.id.checkstand_account_txt)
	private TextView checkstandAccountTxt;
	@ViewInject(R.id.checkstand_amount_edt)
	private EditText checkstandAmountEdt;
	@ViewInject(R.id.checkstand_confirm_btn)
	private TextView checkstandConfirmBtn;
	private String account;
	private double amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_checkstand_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.home_checkstand));
		MoneyFormatUtil.addTextWatcher(checkstandAmountEdt);
		final String code = getIntent().getStringExtra(CaptureActivity.SCAN_RESULT);
		if (!TextUtils.isEmpty(code)) {
			account = "1" + code.substring(2, 12);
			checkstandAccountTxt.setText(getString(R.string.home_checktand_consume_account) + account);
			checkstandConfirmBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					amount = NumberFormatUtil.string2Double(checkstandAmountEdt.getText().toString());
					if (amount > 0) {
						showProgressDialog();
						IdentityHashMap<String, String> param = new IdentityHashMap<>();
						param.put("consumerRecordValue", String.valueOf(amount));
						param.put("payCode", code);
						requestHttpData(Constants.Urls.URL_POST_SCAN_CONSUMPTION, REQUEST_NET_CONSUMPTION, FProtocol.HttpMethod.POST, param);
					} else {
						ToastUtil.shortShow(CheckstandActivity.this, getString(R.string.home_checktand_income_consume_money));
					}
				}
			});
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					InputMethodUtil.showInput(CheckstandActivity.this, checkstandAmountEdt);
				}
			}, 100);
		}
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity entity = Parsers.parseResult(data);
		if (REQUEST_SUCCESS_CODE.equals(entity.getRspCode())) {
			//支付成功
			gotoResultPage(requestCode, true, "");
		} else {
			//支付失败
			gotoResultPage(requestCode, false, entity.getRspMsg());
		}
	}

	private void gotoResultPage(int requestCode, boolean isSuccess, String failedReason) {
		if (REQUEST_NET_CONSUMPTION == requestCode) {
			Intent intent = new Intent(CheckstandActivity.this, DealActivity.class);
			intent.putExtra(DealActivity.IS_SUCCESS, isSuccess);
			if (isSuccess) {
				intent.putExtra(DealActivity.SUCCESS_ACCOUNT, account);
				intent.putExtra(DealActivity.SUCCESS_AMOUNT, NumberFormatUtil.formatMoney(amount));
			} else {
				intent.putExtra(DealActivity.FAILED_REASON, failedReason);
			}
			startActivity(intent);
			finish();
		}
	}
}
