package com.wjika.cardstore.base.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.wjika.cardstore.R;

/**
 * Created by Liu_Zhichao on 2016/1/7 14:57.
 * tab切换基类
 */
public abstract class BaseTabsActivity extends BaseActivity implements TabHost.OnTabChangeListener {

	/**
	 * Tab面板
	 */
	protected FragmentTabHost mTabHost;
	/**
	 * Tab控件
	 */
	protected TabWidget mTabWidget;
	/**
	 * 当前Tab页下标
	 */
	protected int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_tabs_layout);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.fcontainer);
		mTabHost.setOnTabChangedListener(this);
		addTabs();
		mTabWidget = mTabHost.getTabWidget();
		mTabWidget.setDividerDrawable(null);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			currentIndex = mTabHost.getCurrentTab();
		}
	}

	protected abstract void addTabs();

	@Override
	public void onTabChanged(String tabId) {
		currentIndex = mTabHost.getCurrentTab();
	}

	/**
	 * 设置Tab切换
	 * @param tabIndex 切换的Tab下标
	 */
	protected void setCurrentTab(int tabIndex)
	{
		mTabHost.setCurrentTab(tabIndex);
	}

	protected void addTab(View tabView, Class<?> cls, Bundle bundle) {
		mTabHost.addTab(mTabHost.newTabSpec(cls.getSimpleName()).setIndicator(tabView), cls, bundle);
	}

	public int getTabPosition()
	{
		return currentIndex;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	public void showTabHost(boolean show) {
		if (show) {
			mTabWidget.setVisibility(View.VISIBLE);
		} else {
			mTabWidget.setVisibility(View.GONE);
		}
	}
}
