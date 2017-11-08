package com.wjika.cardstore.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * 数字格式化
 */
public class NumberFormatUtil {

	/**
	 * 判断字符是否是数字类型
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static int string2Int(String value) {
		try {
			if (isNumeric(value)) {
				return Integer.parseInt(value);
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public static double string2Double(String value) {
		try {
			if (!TextUtils.isEmpty(value)) {
				return Double.parseDouble(value);
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	/**
	 * 涉及金额格式化
	 * @param money
	 * @return
	 */
	public static String formatMoney(String money){
		DecimalFormat df = new DecimalFormat("###0.00");
		return df.format(string2Double(money));
	}

	public static String formatMoney(double money){
		DecimalFormat df = new DecimalFormat("###0.00");
		return df.format(money);
	}
}
