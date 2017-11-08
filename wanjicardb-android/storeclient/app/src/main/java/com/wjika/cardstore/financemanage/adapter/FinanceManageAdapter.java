package com.wjika.cardstore.financemanage.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.wjika.cardstore.R;
import com.wjika.cardstore.network.entities.FinancePageEntity;
import com.wjika.cardstore.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by admin on 2015/12/2.
 * 财务管理的item
 */
public class FinanceManageAdapter extends BaseAdapterNew<FinancePageEntity> {

	private Resources res;

	public FinanceManageAdapter(Context context, List<FinancePageEntity> mDatas) {
		super(context, mDatas);
		res = context.getResources();
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.finance_manage_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		FinancePageEntity item = getItem(position);
		TextView financeManageItemMoney = ViewHolder.get(convertView, R.id.finance_manage_item_money);
		TextView financeManageItemMoneyDetail = ViewHolder.get(convertView, R.id.finance_manage_item_money_detail);
		TextView financeManageItemTime = ViewHolder.get(convertView, R.id.finance_manage_item_time);
		TextView financeManageItemTimeDetail = ViewHolder.get(convertView, R.id.finance_manage_item_time_detail);
		View commodityTextDivide = ViewHolder.get(convertView, R.id.commodity_text_divide);

		if (position == 0) {
			commodityTextDivide.setVisibility(View.VISIBLE);
		} else {
			commodityTextDivide.setVisibility(View.GONE);
		}

		if (item.getCategory() == 0) {
			financeManageItemMoneyDetail.setText(getContext().getString(R.string.finance_manager_withdraw_money));
			financeManageItemMoney.setText(String.format(res.getString(R.string.finance_manager_decrease), NumberFormatUtil.formatMoney(item.getAmount())));
			financeManageItemTimeDetail.setText(getContext().getString(R.string.finance_manager_withdraw_time));
		}
		if (item.getCategory() == 1) {
			financeManageItemMoneyDetail.setText(res.getString(R.string.finance_manager_sell_card));
			financeManageItemMoney.setText(String.format(res.getString(R.string.finance_manager_increase), NumberFormatUtil.formatMoney(item.getAmount())));
			financeManageItemTimeDetail.setText(getContext().getString(R.string.finance_manager_sell_card_time));
		}
		financeManageItemTime.setText(item.getDealTime());
	}
}
