package com.wjika.cardstore.consumption.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.wjika.cardstore.R;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/2/16 11:54.
 * 下拉筛选adapter
 */
public class CheckSpinnerAdapter extends BaseAdapterNew<String> {

	private int mPosition;

	public CheckSpinnerAdapter(Context context, List<String> mDatas, int mPosition) {
		super(context, mDatas);
		this.mPosition = mPosition;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.check_spinner_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		TextView checkSpinnerText = ViewHolder.get(convertView, R.id.check_spinner_text);
		checkSpinnerText.setText(getItem(position));
		if (mPosition == position) {
			checkSpinnerText.setTextColor(getContext().getResources().getColor(R.color.home_title_bg));
		} else {
			checkSpinnerText.setTextColor(getContext().getResources().getColor(R.color.title_text));
		}
	}
}
