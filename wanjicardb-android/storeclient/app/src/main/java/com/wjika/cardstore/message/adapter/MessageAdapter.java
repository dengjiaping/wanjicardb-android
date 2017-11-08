package com.wjika.cardstore.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.wjika.cardstore.R;
import com.wjika.cardstore.network.entities.MessageEntity;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/1/13 14:33.
 * 消息list适配器
 */
public class MessageAdapter extends BaseAdapterNew<MessageEntity> {

	public MessageAdapter(Context context, List<MessageEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.message_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		MessageEntity messageEntity = getItem(position);
		TextView messageItemTitle = ViewHolder.get(convertView, R.id.message_item_title);
		TextView messageItemDate = ViewHolder.get(convertView, R.id.message_item_date);
		TextView messageItemContent = ViewHolder.get(convertView, R.id.message_item_content);

		if (!TextUtils.isEmpty(messageEntity.getTitle())){
			messageItemTitle.setText(messageEntity.getTitle());
		}
		if (!TextUtils.isEmpty(messageEntity.getCreateDate())){
			messageItemDate.setText(messageEntity.getCreateDate());
		}
		if (!TextUtils.isEmpty(messageEntity.getMessage())){
			messageItemContent.setText(messageEntity.getMessage());
		}
	}
}
