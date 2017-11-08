package com.wjika.cardstore.launcher.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wjika.cardstore.R;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2015/12/1 15:53.
 * 引导页
 */
public class GuidePagerAdapter extends PagerAdapter {

	private List<View> views;
	private View.OnClickListener onClickListener;

	public GuidePagerAdapter(List<View> views, View.OnClickListener onClickListener) {
		this.views = views;
		this.onClickListener = onClickListener;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		View login = view.findViewById(R.id.guide_login);
		if (position == views.size() - 1){
			login.setOnClickListener(onClickListener);
			login.setVisibility(View.VISIBLE);
		} else {
			login.setOnClickListener(null);
			login.setVisibility(View.GONE);
		}
		container.addView(view);
		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}
}
