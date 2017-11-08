package com.wjika.cardstore.commoditymanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjika.cardstore.commoditymanage.ui.CommoditySellingFragment;

import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/12.
 * 商品管理
 */
public class CommoditySellingAdapter extends FragmentPagerAdapter {

	private List<CommoditySellingFragment> fragments;

	public CommoditySellingAdapter(FragmentManager fm, List<CommoditySellingFragment> fragments) {
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
