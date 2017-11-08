package com.wjika.cardstore.financemanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjika.cardstore.financemanage.ui.FinanceWithdrawalsFragment;

import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/12.
 * 财务管理
 */
public class FinanceCategoryAdapter extends FragmentPagerAdapter {

	private List<FinanceWithdrawalsFragment> fragments;

	public FinanceCategoryAdapter(FragmentManager fm, List<FinanceWithdrawalsFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
