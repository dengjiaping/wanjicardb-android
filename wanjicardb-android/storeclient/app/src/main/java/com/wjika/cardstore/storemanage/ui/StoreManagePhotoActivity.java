package com.wjika.cardstore.storemanage.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.view.GridViewForInner;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.storemanage.adapter.StorePhotoAdapter;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/15.
 * 相册管理
 *
 * 封面暂时没有上传和删除，需要注意
 */
public class StoreManagePhotoActivity extends ToolBarActivity implements View.OnClickListener {

	private static final int MAX_PHOTO_NUM = 5;
	private static final int REQUEST_NET_SHOP_PHOTO = 100;
	private static final int REQUEST_NET_DELETE_PHOTO = 200;
	private static final int REQUEST_ACT_ALBUM = 500;

	@ViewInject(R.id.store_photo_scroll)
	private ScrollView storePhotoScroll;
	@ViewInject(R.id.store_manage_shade)
	private RelativeLayout storeManageShade;
//	@ViewInject(R.id.store_manage_delete)
//	private ImageButton storeManageDelete;//暂时没有删除封面功能
	@ViewInject(R.id.store_manage_cover)
	private SimpleDraweeView storeManageCover;
	@ViewInject(R.id.store_manage_cover_add)
	private RelativeLayout storeManageCoverAdd;
	@ViewInject(R.id.store_manage_add_photo)
	private TextView storeManageAddPhoto;
	@ViewInject(R.id.store_manage_add_num)
	private TextView storeManageAddNum;
	@ViewInject(R.id.store_manage_photo_album)
	private GridViewForInner storeManagePhotoAlbum;

	private StorePhotoAdapter shopAdapter;
	private boolean canEdit;//是否能进入编辑模式，
	private boolean isEditMode;//是否编辑模式
	private SelectPhotoEntity selectPhoto;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_photo_act);
		ViewInjectUtils.inject(this);
		initView();
		initData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_manager_photo_manager));
		mBtnTitleRight.setImageResource(R.drawable.store_photo_edit);
		mBtnTitleRight.setVisibility(View.VISIBLE);
		//防止每次打开界面滚动到最后
		storePhotoScroll.smoothScrollTo(0, 0);
		mBtnTitleRight.setOnClickListener(this);
		storeManageAddPhoto.setOnClickListener(this);
	}

	private void initData() {
		requestHttpData(Constants.Urls.URL_GET_SHOP_PHOTO, REQUEST_NET_SHOP_PHOTO, FProtocol.HttpMethod.POST, new IdentityHashMap<String, String>());
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_SHOP_PHOTO == requestCode){
			SelectPhotoEntity selectPhoto = Parsers.parsePhotoList(data);
			if (selectPhoto != null){
				if (TextUtils.isEmpty(selectPhoto.getHomeUrl())){
//					storeManageCoverAdd.setVisibility(View.VISIBLE);
//					storeManageCover.setVisibility(View.GONE);
				} else {
//			canEdit = true;
					storeManageCover.setImageURI(Uri.parse(selectPhoto.getHomeUrl()));
				}

				List<SelectPhotoEntity> selectPhotoEntities = selectPhoto.getSelectPhotoEntities();
				if (selectPhotoEntities.size() > 0) {
					canEdit = true;
				}
				storeManageAddNum.setText(String.format(res.getString(R.string.store_photo_can_add),(MAX_PHOTO_NUM - selectPhotoEntities.size())));
				if (selectPhotoEntities.size() < MAX_PHOTO_NUM){
					selectPhotoEntities.add(new SelectPhotoEntity(R.drawable.store_manage_add_photo));
				}
				shopAdapter = new StorePhotoAdapter(this, selectPhotoEntities, this);
				storeManagePhotoAlbum.setAdapter(shopAdapter);
			}
		} else if (REQUEST_NET_DELETE_PHOTO == requestCode) {
			if (selectPhoto != null){
				shopAdapter.remove(selectPhoto);
				SelectPhotoEntity selectPhotoEntity = shopAdapter.getItem(shopAdapter.getCount() - 1);
				if (0 == selectPhotoEntity.getCameraId() && !TextUtils.isEmpty(selectPhotoEntity.getMerchantPhotoAppsrc())){
					shopAdapter.add(new SelectPhotoEntity(R.drawable.store_manage_add_photo));
				}
				storeManageAddNum.setText(String.format(res.getString(R.string.store_photo_can_add),(MAX_PHOTO_NUM - shopAdapter.getCount() + 1)));
				ToastUtil.shortShow(this, getString(R.string.store_photo_delete_sucess));
			}
			if (shopAdapter.getCount() <= 1){
				canEdit = false;
				isEditMode = false;
				shopAdapter.setFlag(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.right_button:
				if (canEdit){
					if (isEditMode){
						isEditMode = false;
						shopAdapter.setFlag(false);
						storeManageShade.setVisibility(View.GONE);
					} else {
						isEditMode = true;
						shopAdapter.setFlag(true);
//						if (!TextUtils.isEmpty(cover)) {
//							storeManageShade.setVisibility(View.VISIBLE);
//						}
					}
					shopAdapter.notifyDataSetChanged();
				}else {
					ToastUtil.shortShow(this, getString(R.string.store_photo_no_edit_photo));
				}
				break;
			case R.id.store_item_delete:
				selectPhoto = (SelectPhotoEntity) v.getTag();
				if (selectPhoto != null) {
					View dialogView = View.inflate(this, R.layout.custom_dialog_view, null);
					alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();
					TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
					title.setText(getString(R.string.store_photo_is_delete));
					TextView cancel = (TextView) dialogView.findViewById(R.id.dialog_cancle);
					cancel.setText(getString(R.string.cancel));
					dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
					TextView confirm = (TextView) dialogView.findViewById(R.id.dialog_confirm);
					confirm.setText(getString(R.string.button_ok));
					confirm.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							showProgressDialog();
							IdentityHashMap<String, String> param = new IdentityHashMap<>();
							param.put("photoIds", selectPhoto.getMerchantPhotoId());
							requestHttpData(Constants.Urls.URL_POST_DELETE_PHOTO, REQUEST_NET_DELETE_PHOTO, FProtocol.HttpMethod.POST, param);
							alertDialog.dismiss();
						}
					});
					alertDialog.show();
				}
				break;
			case R.id.store_manage_add_photo:
				if (isEditMode) {
					isEditMode = false;
					shopAdapter.setFlag(false);
					storeManageShade.setVisibility(View.GONE);
					shopAdapter.notifyDataSetChanged();
					return;
				}
				startActivity(new Intent(this,StoreManageAddPhotoActivity.class));
				break;
			case R.id.store_manage_img:
				if (isEditMode) {
					isEditMode = false;
					shopAdapter.setFlag(false);
					storeManageShade.setVisibility(View.GONE);
					shopAdapter.notifyDataSetChanged();
					return;
				}
				startActivityForResult(new Intent(this,StoreManageAddPhotoActivity.class), REQUEST_ACT_ALBUM);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && REQUEST_ACT_ALBUM == requestCode){
			SelectPhotoEntity selectPhotoEntity = data.getParcelableExtra("selectPhotoEntity");
			if (selectPhotoEntity != null && shopAdapter != null){
				canEdit = true;
				SelectPhotoEntity photoEntity = shopAdapter.getItem(shopAdapter.getCount() - 1);
				shopAdapter.remove(photoEntity);
				shopAdapter.add(selectPhotoEntity);
				storeManageAddNum.setText(String.format(res.getString(R.string.store_photo_can_add),(MAX_PHOTO_NUM - shopAdapter.getCount())));
				if (shopAdapter.getCount() < MAX_PHOTO_NUM){
					shopAdapter.add(photoEntity);
				}
			}
		}
	}
}
