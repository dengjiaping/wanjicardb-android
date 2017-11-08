package com.wjika.cardstore.storemanage.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;
import com.wjika.cardstore.storemanage.adapter.StoreManageAddPhotoAdapter;
import com.wjika.cardstore.utils.FileUtils;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/19.
 * 选择照片
 */
public class StoreManageAddPhotoActivity extends ToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

	public static final String IMAGE_CACHE_DIR = "image";
	public static final int REQUEST_CAMERA_CODE = 0x1;//相机
	public static final int REQUEST_CODE_CROP = 0x2;

	@ViewInject(R.id.store_manage_select_photo)
	private GridView storeManageSelectPhoto;
	@ViewInject(R.id.store_manage_cancel)
	private TextView storeManageCancel;
	@ViewInject(R.id.store_manage_completed)
	private TextView storeManageCompleted;

	private StoreManageAddPhotoAdapter storeManageAddPhotoAdapter;
	private List<SelectPhotoEntity> selectPhotoEntities = new ArrayList<>();
	private String imagePath;
	private SelectPhotoEntity oldPhotoEntity;
	private File file;
	private int oldPosition = -1;
	private View oldView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_add_photo);
		ViewInjectUtils.inject(this);
		initView();

		selectPhotoEntities.addAll(initData());
		storeManageAddPhotoAdapter = new StoreManageAddPhotoAdapter(this,selectPhotoEntities);
		storeManageSelectPhoto.setAdapter(storeManageAddPhotoAdapter);
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_manager_select_photo));
		storeManageSelectPhoto.setOnItemClickListener(this);
		storeManageCancel.setOnClickListener(this);
		storeManageCompleted.setOnClickListener(this);
	}

	private ArrayList<SelectPhotoEntity> initData() {
		Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String keyData = MediaStore.Images.Media.DATA;
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(imageUri, new String[]{keyData}, null, null, MediaStore.Images.Media.DATE_MODIFIED);
		ArrayList<SelectPhotoEntity> list = new ArrayList<>();
		list.add(new SelectPhotoEntity(R.drawable.store_manage_camera));

		List<String> imgList = new ArrayList<>();
		List<String> realImgList = new ArrayList<>();
		if(cursor != null){
			if(cursor.moveToLast()){
				while (true){
					//获取图片的路径
					String imagePath = cursor.getString(0);
					if (!TextUtils.isEmpty(imagePath)){
						imgList.add(imagePath);
					}
					if(!cursor.moveToPrevious()){
						break;
					}
				}
			}
			cursor.close();
		}

		for (String img : imgList){
			if (!realImgList.contains(img)){
				realImgList.add(img);
			}
		}
		for (String img : realImgList){
			list.add(new SelectPhotoEntity(img));
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.store_manage_cancel:
				finish();
				break;
			case R.id.store_manage_completed:
				if(!TextUtils.isEmpty(imagePath)){
					Intent intent = new Intent(this,StoreManageCropActivity.class);
					intent.putExtra("path", imagePath);
					startActivityForResult(intent,REQUEST_CODE_CROP);
				} else {
					ToastUtil.shortShow(this, getString(R.string.store_manager_select_photo_store));
				}

				/*测试用，看系统图片选择是否会显示问题图片
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType("image*//*");
				startActivityForResult(getAlbum, 0);*/
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		View selectView = view.findViewById(R.id.store_manage_selected);
		if(position == 0){
			showCameraAction();
		} else {
			SelectPhotoEntity photoEntity = storeManageAddPhotoAdapter.getItem(position);
			if (oldPosition == position) {
				if (photoEntity.isFlag()){
					photoEntity.setFlag(false);
					imagePath = "";
					selectView.setVisibility(View.INVISIBLE);
				} else {
					photoEntity.setFlag(true);
					imagePath = photoEntity.getImagePath();
					selectView.setVisibility(View.VISIBLE);
				}
			} else {
				if (oldView != null) {
					oldPhotoEntity.setFlag(false);
					oldView.setVisibility(View.INVISIBLE);
				}
				photoEntity.setFlag(true);
				selectView.setVisibility(View.VISIBLE);
				oldPosition = position;
				oldView = selectView;
				oldPhotoEntity = photoEntity;
				imagePath = photoEntity.getImagePath();
			}
		}
	}

	private void showCameraAction() {
		try {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			file = FileUtils.getDiskCacheFile(this, IMAGE_CACHE_DIR);
			file = new File((file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg"));
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(openCameraIntent, REQUEST_CAMERA_CODE);
		}catch (Exception e) {
			ToastUtil.shortShow(this, getString(R.string.store_photo_open_camear));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			switch (requestCode){
				case REQUEST_CAMERA_CODE:
					if (file != null) {
						Intent intent = new Intent(this,StoreManageCropActivity.class);
						intent.putExtra("path",file.getPath());
						startActivityForResult(intent,REQUEST_CODE_CROP);
					}
					break;
				case REQUEST_CODE_CROP:
					setResult(RESULT_OK, data);
					finish();
					break;
			}
		}
	}
}
