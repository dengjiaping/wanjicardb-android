package com.wjika.cardstore.launcher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseActivity;
import com.wjika.cardstore.launcher.adapter.GuidePagerAdapter;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.utils.ConfigUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu_ZhiChao on 2015/11/27 11:24.
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener{

	public final int[] guidePics = new int[]{R.drawable.launcher_img_guide_1, R.drawable.launcher_img_guide_2, R.drawable.launcher_img_guide_3,R.drawable.launcher_img_guide_4};

	@ViewInject(R.id.guide_viewpager)
	private ViewPager guideViewpager;
	@ViewInject(R.id.guide_dot_first)
	private RadioButton guideDotFirst;
	@ViewInject(R.id.guide_dot_second)
	private RadioButton guideDotSecond;
	@ViewInject(R.id.guide_dot_third)
	private RadioButton guideDotThird;
	@ViewInject(R.id.guide_dot_fourth)
	private RadioButton guideDotFourth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		List<View> views = new ArrayList<>();
		for (int guidePic : guidePics) {
			View view = View.inflate(this, R.layout.guide_item, null);
			ImageView guideItemPic = (ImageView) view.findViewById(R.id.guide_item_pic);
			guideItemPic.setBackgroundResource(guidePic);
			views.add(view);
		}
		GuidePagerAdapter pagerAdapter = new GuidePagerAdapter(views, this);
		guideViewpager.setAdapter(pagerAdapter);

		guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position){
					case 0:
						guideDotFirst.setChecked(true);
						break;
					case 1:
						guideDotSecond.setChecked(true);
						break;
					case 2:
						guideDotThird.setChecked(true);
						break;
					case 3:
						guideDotFourth.setChecked(true);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.guide_login:
				startActivity(new Intent(this, LoginActivity.class));
				ConfigUtil.setHideGuide(this, true);
				finish();
				break;
		}
	}
}
