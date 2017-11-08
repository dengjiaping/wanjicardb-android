package com.wjika.cardstore.commoditymanage.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.commoditymanage.adapter.CommoditySellingAdapter;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/11/30.
 * 商品管理
 */
public class CommodityManageActivity extends ToolBarActivity {

	public static final int SELLING = 1;//上架
	public static final int SOLD_OUT = 0;//下架

	@ViewInject(R.id.commodity_manage_category)
	private RadioGroup commodityManageCategory;
	@ViewInject(R.id.commodity_manage_content)
	private ViewPager commodityManageContent;
	@ViewInject(R.id.commodity_manage_selling)
	private RadioButton commodityManageSelling;
	@ViewInject(R.id.commodity_manage_sold_out)
	private RadioButton commodityManageSoldOut;

	private List<CommoditySellingFragment> fragments = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commodity_manage_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(res.getString(R.string.commodity_manage));
		CommoditySellingFragment commoditySellingFragment1 = new CommoditySellingFragment();
		CommoditySellingFragment commoditySellingFragment2 = new CommoditySellingFragment();
		commoditySellingFragment1.setArgs(this, SELLING, commodityManageSelling, commodityManageSoldOut);
		commoditySellingFragment2.setArgs(this, SOLD_OUT, commodityManageSelling, commodityManageSoldOut);
		fragments.add(commoditySellingFragment1);
		fragments.add(commoditySellingFragment2);
		CommoditySellingAdapter commoditySellingAdapter = new CommoditySellingAdapter(getSupportFragmentManager(), fragments);
		commodityManageContent.setAdapter(commoditySellingAdapter);
		commodityManageCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int currentItem = commodityManageContent.getCurrentItem();
				switch (checkedId) {
					case R.id.commodity_manage_selling:
						if (currentItem != 0) {
							commodityManageContent.setCurrentItem(0);
							changeTextColor(0);
						}
						break;
					case R.id.commodity_manage_sold_out:
						if (currentItem != 1) {
							commodityManageContent.setCurrentItem(1);
							changeTextColor(1);
						}
						break;

				}
			}
		});

		commodityManageContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 0:
						commodityManageCategory.check(R.id.commodity_manage_selling);
						break;
					case 1:
						commodityManageCategory.check(R.id.commodity_manage_sold_out);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}


	private void changeTextColor(int position) {
		commodityManageSelling.setTextColor(getResources().getColor(R.color.title_text));
		commodityManageSoldOut.setTextColor(getResources().getColor(R.color.title_text));
		switch (position) {
			case 0:
				commodityManageSelling.setTextColor(getResources().getColor(R.color.home_title_bg));
				break;
			case 1:
				commodityManageSoldOut.setTextColor(getResources().getColor(R.color.home_title_bg));
				break;
		}
	}
}
