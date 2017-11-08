package com.wjika.cardstore.financemanage.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.financemanage.adapter.FinanceCategoryAdapter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.utils.NumberFormatUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/12/1.
 * 财务管理
 */
public class FinanceManageActivity extends ToolBarActivity {

	public static final int REQUEST_CODE_FINANCE = 100;
	public static final int REQUEST_CODE_FINANCE_MORE = 101;

	public static final String ALL = "100";//全部
	public static final String INCOME = "1";//收入
	public static final String WITHDRAW = "0";//提现

	public static boolean scrollFlag = true;
	public static boolean scrollFlagFirst = true;

	@ViewInject(R.id.finance_manage_withdraw_deposit_money)
	private LinearLayout financeManageWithdrawDepositMoney;
	@ViewInject(R.id.finance_amount)
	private TextView financeAmount;
	@ViewInject(R.id.finance_manage_category)
	private RadioGroup financeManageCategory;
	@ViewInject(R.id.finance_manage_all)
	private RadioButton financeManageALl;
	@ViewInject(R.id.finance_manage_income)
	private RadioButton financeManageIncome;
	@ViewInject(R.id.finance_manage_withdraw)
	private RadioButton financeManageWithdraw;
	@ViewInject(R.id.finance_manage_withdrawals)
	private ViewPager financeManageWithdrawals;

	private List<FinanceWithdrawalsFragment> fragments = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finance_manage_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		scrollFlag = true;
		setLeftTitle(getString(R.string.home_frag_finance_manager));
		financeAmount.setText(NumberFormatUtil.formatMoney(MainActivity.amount));
		FinanceWithdrawalsFragment financeWithdrawalsFragment1 = new FinanceWithdrawalsFragment();
		financeWithdrawalsFragment1.setArgs(this, ALL, financeManageWithdrawDepositMoney, financeManageCategory, financeManageWithdrawals);
		FinanceWithdrawalsFragment financeWithdrawalsFragment2 = new FinanceWithdrawalsFragment();
		financeWithdrawalsFragment2.setArgs(this, INCOME, financeManageWithdrawDepositMoney, financeManageCategory, financeManageWithdrawals);
		FinanceWithdrawalsFragment financeWithdrawalsFragment3 = new FinanceWithdrawalsFragment();
		financeWithdrawalsFragment3.setArgs(this, WITHDRAW, financeManageWithdrawDepositMoney, financeManageCategory, financeManageWithdrawals);
		fragments.add(financeWithdrawalsFragment1);
		fragments.add(financeWithdrawalsFragment2);
		fragments.add(financeWithdrawalsFragment3);
		FinanceCategoryAdapter financeCategoryAdapter = new FinanceCategoryAdapter(getSupportFragmentManager(), fragments);
		financeManageWithdrawals.setAdapter(financeCategoryAdapter);
		financeManageCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int currentItem = financeManageWithdrawals.getCurrentItem();
				switch (checkedId) {
					case R.id.finance_manage_all:
						if (currentItem != 0) {
							financeManageWithdrawals.setCurrentItem(0);
							changeTextColor(0);
						}
						break;
					case R.id.finance_manage_income:
						if (currentItem != 1) {
							financeManageWithdrawals.setCurrentItem(1);
							changeTextColor(1);
						}
						break;
					case R.id.finance_manage_withdraw:
						if (currentItem != 2) {
							financeManageWithdrawals.setCurrentItem(2);
							changeTextColor(2);
						}
						break;
				}
			}
		});

		financeManageWithdrawals.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 0:
						financeManageCategory.check(R.id.finance_manage_all);
						break;
					case 1:
						financeManageCategory.check(R.id.finance_manage_income);
						break;
					case 2:
						financeManageCategory.check(R.id.finance_manage_withdraw);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	private void changeTextColor(int position) {
		financeManageALl.setTextColor(getResources().getColor(R.color.title_text));
		financeManageIncome.setTextColor(getResources().getColor(R.color.title_text));
		financeManageWithdraw.setTextColor(getResources().getColor(R.color.title_text));
		switch (position) {
			case 0:
				financeManageALl.setTextColor(getResources().getColor(R.color.home_title_bg));
				break;
			case 1:
				financeManageIncome.setTextColor(getResources().getColor(R.color.home_title_bg));
				break;
			case 2:
				financeManageWithdraw.setTextColor(getResources().getColor(R.color.home_title_bg));
		}
	}

	@Override
	public void setLeftTitle(String title) {
		super.setLeftTitle(title);
		mTitlebar.setBackgroundColor(res.getColor(R.color.home_title_bg));
		mBtnTitleLeft.setBackgroundColor(res.getColor(R.color.home_title_bg));
		mBtnTitleLeft.setImageResource(R.drawable.ic_back_white);
		mTitleLeft.setTextColor(getResources().getColor(R.color.title_bg));
	}

	@Override
	public void onDestroy() {
		scrollFlag = true;
		scrollFlagFirst = true;
		super.onDestroy();
	}
}
