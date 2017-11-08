package com.wjika.cardstore.storemanage.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wjika.cardstore.R;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;
import com.wjika.cardstore.utils.CommonTools;

import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/21.
 * 相册图片
 */
public class StoreManageAddPhotoAdapter extends BaseAdapterNew<SelectPhotoEntity> {

	public StoreManageAddPhotoAdapter(Context context, List<SelectPhotoEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.store_manage_photo_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		SelectPhotoEntity selectPhotoEntity = getItem(position);
		SimpleDraweeView storeManagePhoto = ViewHolder.get(convertView, R.id.store_item_photo);
		View view = ViewHolder.get(convertView, R.id.store_manage_selected);
		GenericDraweeHierarchy hierarchy = storeManagePhoto.getHierarchy();
		if (position == 0) {
			int cameraId = selectPhotoEntity.getCameraId();
			hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
			int padding = CommonTools.dip2px(getContext(), 30);
			storeManagePhoto.setPadding(padding, padding, padding, padding);
			storeManagePhoto.setHierarchy(hierarchy);
			storeManagePhoto.setImageURI(Uri.parse("res://com.wjika.cardstore/" + cameraId));
		} else {
			hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
			storeManagePhoto.setPadding(0, 0, 0, 0);

			int contentWidth = (CommonTools.getScreenWidth(getContext()) - CommonTools.dip2px(getContext(), 6)) / 4;
			Uri picUri = Uri.parse("file://" + selectPhotoEntity.getImagePath());
			ImageRequest request = ImageRequestBuilder.newBuilderWithSource(picUri)
					.setResizeOptions(new ResizeOptions(contentWidth, contentWidth))
					.build();
			DraweeController controller = Fresco.newDraweeControllerBuilder()
					.setOldController(storeManagePhoto.getController())
					.setImageRequest(request)
					.build();
			storeManagePhoto.setController(controller);
		}

		if (selectPhotoEntity.isFlag()) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.INVISIBLE);
		}
	}
}
