package com.wjika.cardstore.storemanage.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.storemanage.crop.ClipImageLayout;
import com.wjika.cardstore.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IdentityHashMap;

/**
 * Created by zhangzhaohui on 2016/1/29.
 * 图片裁剪
 */
public class StoreManageCropActivity extends ToolBarActivity implements View.OnClickListener {

	public static final String TMP_PATH = "clip_temp.jpg";
	public static final int REQUEST_NET_UPLOAD_PHOTO = 100;

	private ClipImageLayout storeManageCropImage = null;
	private String path;
	private int height;
	private int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_crop_act);
		setLeftTitle(getString(R.string.store_manager_tailor_photo));
		storeManageCropImage= (ClipImageLayout) findViewById(R.id.store_manage_crop_image);
		path = getIntent().getStringExtra("path");
		height = getWindowManager().getDefaultDisplay().getHeight();
		width = getWindowManager().getDefaultDisplay().getWidth();

		findViewById(R.id.store_manage_crop_cancel).setOnClickListener(this);
		findViewById(R.id.store_manage_crop_done).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CreatBitmapAsyncTask creatBitmapAsyncTask = new CreatBitmapAsyncTask();
		creatBitmapAsyncTask.execute(path);
		// 有的系统返回的图片是旋转了，有的没有旋转，所以处理

	}

	private class CreatBitmapAsyncTask extends AsyncTask<String,Bitmap,String>{

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			int degreee = readBitmapDegree(path);
			try {
				Bitmap bitmap = FileUtils.revitionImageSize(path, width, height);
				if (bitmap != null) {
					if (degreee == 0) {
						publishProgress(bitmap);

					} else {
						Bitmap bitmap1 = rotateBitmap(degreee, bitmap);
						publishProgress(bitmap1);
					}
				} else {
//				ToastUtil.shortShow(this,"图片太大");
					finish();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Bitmap... values) {
			storeManageCropImage.setImageBitmap(values[0]);
			closeProgressDialog();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.store_manage_crop_cancel:
				finish();
				break;
			case R.id.store_manage_crop_done:
				showProgressDialog();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap = storeManageCropImage.clip();
						String path = Environment.getExternalStorageDirectory() + "/" + TMP_PATH;
						saveBitmap(bitmap, path);
						File file = new File(path);
						FileUtils.compressBmpToFile(file, 100);
						IdentityHashMap<String, String> param = new IdentityHashMap<>();
						param.put("merchantPhotoCategory", "6");
						param.put("token", UserCenter.getToken(StoreManageCropActivity.this));
						requestHttpData(Constants.Urls.URL_POST_UPLOAD_PHOTO, REQUEST_NET_UPLOAD_PHOTO, FProtocol.HttpMethod.POST, param, "file", file);
					}
				}).start();
				break;
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_UPLOAD_PHOTO == requestCode){
			SelectPhotoEntity selectPhotoEntity = Parsers.parseSelectPhoto(data);
			if (selectPhotoEntity != null){
				ToastUtil.shortShow(this, getString(R.string.store_manager_upload_sucess));
				setResult(RESULT_OK, new Intent().putExtra("selectPhotoEntity", selectPhotoEntity));
				finish();
			}
		}
	}

	private void saveBitmap(Bitmap bitmap, String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}

		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fOut != null)
					fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建图片
	 *
	 * @param path
	 * @return
	 */
	private Bitmap createBitmap(String path) {
		if (path == null) {
			return null;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[64 * 1024];
		FileInputStream is = null;
		Bitmap bitmap = null;

		try {
			is = new FileInputStream(path);
			bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (OutOfMemoryError e){
			System.gc();
			System.gc();
		}finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bitmap;
	}

	// 读取图像的旋转度
	private int readBitmapDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	// 旋转图片
	private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		return resizedBitmap;
	}


	@Override
	protected void finalize() throws Throwable {
		System.gc();
		System.gc();
		super.finalize();
	}

	@Override
	public void onDestroy() {
		System.gc();
		super.onDestroy();
	}
}
