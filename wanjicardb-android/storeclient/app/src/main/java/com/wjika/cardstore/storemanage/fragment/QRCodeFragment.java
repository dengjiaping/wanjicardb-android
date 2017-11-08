package com.wjika.cardstore.storemanage.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Liu_Zhichao on 2016/1/29 09:56.
 * 店铺二维码
 */
public class QRCodeFragment extends BaseFragment {

	private static final int WHITE = 0xFFFFFFFF;
	private static final int GRAY = 0xFFe5e5e5;
	private static final int BLACK = 0xFF000000;

	private ImageView qrCode;
	private Context context;
	private int id;

	public void setArgs(Context context,int id){
		this.context = context;
		this.id = id;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.qr_code_frag, null);
		qrCode = (ImageView) view.findViewById(R.id.qr_code);
		setPayCode(String.valueOf(id));
		return view;
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(context, errorMessage);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void setPayCode(String code) {
		if(code != null) {

			int qrHeight = qrCode.getLayoutParams().height;
			int qrWidth = qrCode.getLayoutParams().width;
			//二维码
			try {
				setQvImageView(qrCode, code, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
			} catch (OutOfMemoryError e) {
				System.gc();
				setQvImageView(qrCode, code, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
			}
		}
	}

	private void setImageView(ImageView imageView, String code, BarcodeFormat format, int width, int height) {
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(code);
		if (encoding != null) {
			hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}

		BitMatrix result = null;
		try {
			result = new MultiFormatWriter().encode(code, format, width, height, hints);
		} catch (IllegalArgumentException iae) {
			return;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		width = result.getWidth();
		height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}

	private void setQvImageView(ImageView imageView, String code, BarcodeFormat format, int width, int height) {
		Map<EncodeHintType, Object> hints = null;
		if(code != null){
			String encoding = guessAppropriateEncoding(code);
			if (encoding != null) {
				hints = new EnumMap<>(EncodeHintType.class);
				hints.put(EncodeHintType.CHARACTER_SET, encoding);
			}
		}

		BitMatrix result = null;
		try {
			result = new MultiFormatWriter().encode(code, format, width, height, hints);
		} catch (IllegalArgumentException iae) {
			return;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		width = result.getWidth();
		height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : GRAY;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}

	/**
	 * 获取合适的编码方式
	 * @param contents
	 * @return
	 */
	private static String guessAppropriateEncoding(CharSequence contents) {
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF)
				return "UTF-8";
		}
		return null;
	}
}
