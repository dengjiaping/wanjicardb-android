package com.wjika.cardstore.ordermanager.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.wjika.cardstore.R;
import com.wjika.cardstore.network.entities.OrderManagerEntity;

import java.util.List;

/**
 * Created by kkkkk on 2016/1/25.
 * 订单管理下的数据列表的adapter
 */
public class OrderManagerListAdapter extends BaseAdapterNew<OrderManagerEntity> {

	public OrderManagerListAdapter(Context context, List<OrderManagerEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.order_manager_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		OrderManagerEntity orderManagerEntity = getItem(position);
		TextView orderTypeText = ViewHolder.get(convertView, R.id.order_type_text);
		TextView orderOrderNoText = ViewHolder.get(convertView, R.id.order_orderno_text);
		TextView orderNameText = ViewHolder.get(convertView, R.id.order_name_text);
		TextView orderNumText = ViewHolder.get(convertView, R.id.order_num_text);
		TextView orderMoneyText = ViewHolder.get(convertView, R.id.order_money_text);
		TextView orderPhoneText = ViewHolder.get(convertView, R.id.order_phone_text);
		TextView orderTimeText = ViewHolder.get(convertView, R.id.order_time_text);

		orderTypeText.setText(String.format(getContext().getString(R.string.order_state), orderManagerEntity.getCardOrderStatus()));
		orderOrderNoText.setText(String.format(getContext().getString(R.string.order_num), orderManagerEntity.getCardOrderNo()));
		orderNameText.setText(orderManagerEntity.getMerchantCardName());
		orderNumText.setText(String.format(getContext().getString(R.string.order_amount), orderManagerEntity.getCardOrderAmount()));
		orderMoneyText.setText(String.format(getContext().getString(R.string.commodity_rmb_sign), orderManagerEntity.getCardOrderValue()));
		orderPhoneText.setText(String.format(getContext().getString(R.string.order_account), orderManagerEntity.getCardOrderPhone()));
		orderTimeText.setText(String.format(getContext().getString(R.string.order_time), orderManagerEntity.getCardOrderCreatedate()));
	}
}
