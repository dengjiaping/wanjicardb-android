package com.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {

	public static void shortShow(Context context, String content) {
		if (!TextUtils.isEmpty(content)) {
			Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public static void longShow(Context context, String content) {
		if (!TextUtils.isEmpty(content)) {
			Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	private static Toast toast;

	public static void showToast(Context context, String text) {
		if (toast == null) {
			toast = toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}

}
