package com.wjika.cardstore.commoditymanage.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wjika.cardstore.R;
import com.wjika.cardstore.commoditymanage.ui.CommodityManageActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.CommodityEntity;

import java.util.List;

/**
 * 商品管理列表的adapter
 * Created by admin on 2015/11/30.
 */
public class CommodityCategoryAdapter extends BaseAdapterNew<CommodityEntity> {

	private View.OnClickListener onClickListener;
	private Resources res;

	public CommodityCategoryAdapter(Context context, List<CommodityEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		res = context.getResources();
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.commodity_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CommodityEntity commodityEntity = getItem(position);
		View textViewdivide = ViewHolder.get(convertView, R.id.commodity_text_divide);
		ImageView cardImgBg = ViewHolder.get(convertView, R.id.card_img_bg);
		SimpleDraweeView cardImgCover = ViewHolder.get(convertView, R.id.card_img_cover);
		TextView cardTxtName = ViewHolder.get(convertView, R.id.card_txt_name);
		TextView cardTxtStoreName = ViewHolder.get(convertView, R.id.card_txt_store_name);

		TextView commodityListItemCardDetail = ViewHolder.get(convertView, R.id.commodity_list_item_card_detail);
		TextView commodityListItemCardPrice = ViewHolder.get(convertView, R.id.commodity_list_item_card_price);
		TextView commodityItemCardWorkOff = ViewHolder.get(convertView, R.id.commodity_item_card_work_off);
		TextView commodityListItemPutaway = ViewHolder.get(convertView, R.id.commodity_list_item_putaway);
		if (position == 0) {
			textViewdivide.setVisibility(View.GONE);
		} else {
			textViewdivide.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(commodityEntity.getMerchantCardPicurl())) {
			cardImgCover.setImageURI(Uri.parse(commodityEntity.getMerchantCardPicurl()));
		}
		switch (commodityEntity.getMerchantCardColor()) {
			case 1:
				cardImgBg.setImageResource(R.drawable.card_details_bg_red);
				break;
			case 2:
				cardImgBg.setImageResource(R.drawable.card_details_bg_orange);
				break;
			case 4:
				cardImgBg.setImageResource(R.drawable.card_details_bg_green);
				break;
			default:
				cardImgBg.setImageResource(R.drawable.card_details_bg_blue);
		}
		cardTxtName.setText(commodityEntity.getMerchantCardName());
		cardTxtStoreName.setText(commodityEntity.getMerchantName());

		commodityListItemCardDetail.setText(String.format(res.getString(R.string.commodity_nominal_value), commodityEntity.getMerchantCardName(), commodityEntity.getCardFacePrice()));
		commodityListItemCardPrice.setText(String.format(res.getString(R.string.commodity_rmb_sign), commodityEntity.getCardSalePrice()));
		commodityItemCardWorkOff.setText(String.format(res.getString(R.string.commodity_solded), commodityEntity.getMerchantCardSalesvolume()));
		int status = commodityEntity.getMerchantCardStatus();
		if (UserCenter.isMain(getContext())) {
			commodityListItemPutaway.setVisibility(View.VISIBLE);
			commodityListItemPutaway.setTag(commodityEntity);
			commodityListItemPutaway.setOnClickListener(onClickListener);
			switch (status) {
				case CommodityManageActivity.SELLING:
					commodityListItemPutaway.setText(res.getString(R.string.commodity_sold_out));
					break;
				case CommodityManageActivity.SOLD_OUT:
					commodityListItemPutaway.setText(res.getString(R.string.commodity_putaway));
					break;
			}
		} else {
			commodityListItemPutaway.setVisibility(View.INVISIBLE);
		}
	}
}
