package com.wjika.cardstore.storemanage.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wjika.cardstore.R;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/2/2 06:30.
 * 相册图片显示adapter
 */
public class StorePhotoAdapter extends BaseAdapterNew<SelectPhotoEntity> {

	private View.OnClickListener onClickListener;
	private boolean flag;

	public StorePhotoAdapter(Context context, List<SelectPhotoEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.onClickListener = onClickListener;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.store_photo_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		SelectPhotoEntity selectPhotoEntity = getItem(position);
		SimpleDraweeView simpleDraweeView = ViewHolder.get(convertView, R.id.store_manage_img);
		RelativeLayout storeItemShade = ViewHolder.get(convertView, R.id.store_item_shade);
		ImageButton storeItemDelete = ViewHolder.get(convertView, R.id.store_item_delete);

		GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
		if (getCount() < 5 && position == getCount() - 1) {
			showResPic(selectPhotoEntity, simpleDraweeView, storeItemShade, hierarchy);
		} else {
			if (getCount() == 5) {
				if (0 != selectPhotoEntity.getCameraId() && TextUtils.isEmpty(selectPhotoEntity.getMerchantPhotoAppsrc())) {
					showResPic(selectPhotoEntity, simpleDraweeView, storeItemShade, hierarchy);
				} else {
					showNetPic(selectPhotoEntity, simpleDraweeView, storeItemShade, storeItemDelete, hierarchy);
				}
			} else {
				showNetPic(selectPhotoEntity, simpleDraweeView, storeItemShade, storeItemDelete, hierarchy);
			}
		}
	}

	private void showResPic(SelectPhotoEntity selectPhotoEntity, SimpleDraweeView simpleDraweeView, RelativeLayout storeItemShade, GenericDraweeHierarchy hierarchy) {
		hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER);
		simpleDraweeView.setHierarchy(hierarchy);
		simpleDraweeView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
		simpleDraweeView.setImageURI(Uri.parse("res://com.wjika.cardstore/" + selectPhotoEntity.getCameraId()));
		simpleDraweeView.setOnClickListener(onClickListener);

		storeItemShade.setVisibility(View.GONE);
	}

	private void showNetPic(SelectPhotoEntity selectPhotoEntity, SimpleDraweeView simpleDraweeView, RelativeLayout storeItemShade, ImageButton storeItemDelete, GenericDraweeHierarchy hierarchy) {
		hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
		simpleDraweeView.setHierarchy(hierarchy);
		simpleDraweeView.setBackgroundResource(R.drawable.default_image_bg);
		simpleDraweeView.setImageURI(Uri.parse(selectPhotoEntity.getMerchantPhotoAppsrc()));
		simpleDraweeView.setOnClickListener(null);

		if (flag) {
			storeItemShade.setVisibility(View.VISIBLE);
		} else {
			storeItemShade.setVisibility(View.GONE);
		}
		storeItemDelete.setTag(selectPhotoEntity);
		storeItemDelete.setOnClickListener(onClickListener);
	}
}
