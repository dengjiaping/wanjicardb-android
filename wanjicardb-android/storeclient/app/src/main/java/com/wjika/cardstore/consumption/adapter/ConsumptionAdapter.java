package com.wjika.cardstore.consumption.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.wjika.cardstore.R;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.utils.NumberFormatUtil;
import com.wjika.cardstore.utils.StringUtil;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/2/14 17:57.
 * 消费管理
 */
public class ConsumptionAdapter extends BaseAdapterNew<ConsumptionEntity> {

	private View.OnClickListener onClickListener;
	private Resources res;

	public ConsumptionAdapter(Context context, List<ConsumptionEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		res = context.getResources();
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.consumption_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ConsumptionEntity consumptionEntity = getItem(position);

		TextView consumptionItemStatus = ViewHolder.get(convertView, R.id.consumption_item_status);
		TextView consumptionItemDate = ViewHolder.get(convertView, R.id.consumption_item_date);
		TextView consumptionItemNo = ViewHolder.get(convertView, R.id.consumption_item_no);
		TextView consumptionItemPhone = ViewHolder.get(convertView, R.id.consumption_item_phone);
		TextView consumptionItemAmount = ViewHolder.get(convertView, R.id.consumption_item_amount);
		TextView consumptionitemRefund = ViewHolder.get(convertView, R.id.consumption_item_refund);
		TextView consumptionItemPrint = ViewHolder.get(convertView, R.id.consumption_item_print);

		consumptionitemRefund.setVisibility(View.GONE);
		consumptionItemPrint.setVisibility(View.GONE);
		switch (consumptionEntity.getTranStatus()){
			case 0:
				consumptionItemStatus.setText(res.getString(R.string.consumption_to_be_confirmed));
				break;
			case 1:
				consumptionitemRefund.setVisibility(View.VISIBLE);
				if (MainActivity.isWangPOS || MainActivity.isQFPOS || MainActivity.isLklPOS || MainActivity.isZBPOS) {
					consumptionItemPrint.setVisibility(View.VISIBLE);
				}
				consumptionItemStatus.setText(res.getString(R.string.consumption_consume_succeed));
				consumptionitemRefund.setText(res.getString(R.string.refund));
				consumptionitemRefund.setTextColor(getContext().getResources().getColor(R.color.home_title_bg));
				consumptionitemRefund.setBackgroundResource(R.drawable.commodity_manage_sold_out);
				consumptionitemRefund.setPadding(5,5,5,5);
				consumptionitemRefund.setVisibility(View.VISIBLE);
				consumptionitemRefund.setEnabled(true);
				break;
			case 2:
				consumptionItemStatus.setText(res.getString(R.string.consumption_cancle));
				break;
			case 3:
				consumptionItemStatus.setText(res.getString(R.string.consumption_refunded));
				consumptionitemRefund.setText(res.getString(R.string.consumption_refunded));
				consumptionitemRefund.setTextColor(getContext().getResources().getColor(R.color.text_hint));
				consumptionitemRefund.setBackgroundResource(R.drawable.consumption_refunded);
				consumptionitemRefund.setPadding(5,5,5,5);
				consumptionitemRefund.setVisibility(View.VISIBLE);
				consumptionitemRefund.setEnabled(false);
				break;
			case 4:
				consumptionItemStatus.setText(res.getString(R.string.consumption_reversal_close));
				consumptionitemRefund.setVisibility(View.GONE);
				break;
		}
		consumptionItemDate.setText(consumptionEntity.getTransDate());
		if (!StringUtil.isEmpty(consumptionEntity.getTransNo())) {
			consumptionItemNo.setText(String.format(res.getString(R.string.consumption_consume_num), consumptionEntity.getTransNo()));
		} else {
			consumptionItemNo.setText(String.format(res.getString(R.string.consumption_consume_num), ""));
		}
		if (!StringUtil.isEmpty(consumptionEntity.getUserPhone())) {
			consumptionItemPhone.setText(String.format(res.getString(R.string.consumption_phone_num), consumptionEntity.getUserPhone()));
		} else {
			consumptionItemPhone.setText(String.format(res.getString(R.string.consumption_phone_num), ""));
		}
		SpannableString text = new SpannableString(String.format(res.getString(R.string.consumption_order_money), NumberFormatUtil.formatMoney(consumptionEntity.getRealOrderAmount())));
		text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.search_money)), 5, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		consumptionItemAmount.setText(text);
		consumptionitemRefund.setTag(consumptionEntity);
		consumptionItemPrint.setTag(consumptionEntity);
		consumptionitemRefund.setOnClickListener(onClickListener);
		consumptionItemPrint.setOnClickListener(onClickListener);
	}
}
