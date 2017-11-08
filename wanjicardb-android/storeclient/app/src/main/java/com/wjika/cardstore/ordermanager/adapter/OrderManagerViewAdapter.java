package com.wjika.cardstore.ordermanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjika.cardstore.ordermanager.ui.OrderManagerFragment;

import java.util.List;

/**
 * viewpager的adapter
 * Created by kkkkk on 2016/1/25.
 */
public class OrderManagerViewAdapter extends FragmentPagerAdapter {

	private List<OrderManagerFragment> fragments;

	public OrderManagerViewAdapter(FragmentManager fm, List<OrderManagerFragment> fragments) {
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
