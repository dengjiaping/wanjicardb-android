package com.wjika.cardstore.consumption.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.common.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.wjika.cardstore.R;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.utils.TimeUtil;
import com.yunnex.printlib.PrintUtils;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Liu_Zhichao on 2016/6/12 15:39.
 * 掌贝POS机打印工具类
 */
public class ZBPrintTools {

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static void printNormal(Activity activity, ConsumptionEntity consumptionEntity, StatisticsEntity statisticsEntity) {
		String companyName = activity.getString(R.string.company_name);
		String companyUrl = activity.getString(R.string.company_url);
		Intent intent = null;
		if (consumptionEntity != null) {
			intent = PrintUtils.bindIntent(getPrinterQueue(consumptionEntity, UserCenter.getMerName(activity), companyName, companyUrl));
		} else if (statisticsEntity != null) {
			intent = PrintUtils.bindIntent(getPrinterQueue(statisticsEntity, UserCenter.getMerName(activity), companyName, companyUrl));
		}
		if (intent == null) {
			ToastUtil.shortShow(activity, "打印小票没有内容");
		} else {
			try {
				activity.startActivity(intent);
			} catch (Exception e) {
				ToastUtil.shortShow(activity, "掌贝打印SDK未安装");
				e.printStackTrace();
			}
		}
	}

	private static Queue<PrintUtils.PrintData> getPrinterQueue(ConsumptionEntity consumptionEntity, String merName, String companyName, String companyUrl) {
		Queue<PrintUtils.PrintData> printerQueue = new LinkedList<>();
		try {
			String status;
			if (1 == consumptionEntity.getTranStatus()){
				status = "交易成功";
			}else {
				status = "交易失败";
			}

			printerQueue.add(PrintUtils.getByte(PrintUtils.printCenter("商家名称：" + merName, PrintUtils.LINE_2), 3));

			StringBuilder sb = new StringBuilder();
			sb.append(PrintUtils.print("订单号：" + consumptionEntity.getTransNo()));
			sb.append(PrintUtils.print("付款人：" + consumptionEntity.getUserPhone()));
			sb.append(PrintUtils.print("消费金额：" + consumptionEntity.getRealOrderAmount()));
			sb.append(PrintUtils.print("当前状态：" + status));
			sb.append(PrintUtils.print("付款时间：" + consumptionEntity.getTransDate() + "\n"));
			printerQueue.add(PrintUtils.getByte(sb.toString()));

			Bitmap oneCodeImg = getOneCodeImg(consumptionEntity.getTransNo(), BarcodeFormat.CODE_128, 300, 100);
			if (oneCodeImg != null) {
				printerQueue.add(PrintUtils.getByte(oneCodeImg));
			}

			printerQueue.add(PrintUtils.getByte(PrintUtils.print("\n\n" + companyName + "\n" + companyUrl)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printerQueue;
	}

	private static Queue<PrintUtils.PrintData> getPrinterQueue(StatisticsEntity statisticsEntity, String merName, String companyName, String companyUrl) {
		Queue<PrintUtils.PrintData> printerQueue = new LinkedList<>();
		try {
			String sdate = TimeUtil.formatTime(TimeUtil.parseTime(statisticsEntity.getStime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
			String edate = TimeUtil.formatTime(TimeUtil.parseTime(statisticsEntity.getEtime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);

			printerQueue.add(PrintUtils.getByte(PrintUtils.printCenter("商家名称：" + merName, PrintUtils.LINE_2), 3));
			StringBuilder sb = new StringBuilder();
			sb.append(PrintUtils.print("起始日期：" + sdate));
			sb.append(PrintUtils.print("结束日期：" + edate));
			sb.append(PrintUtils.print("总单数：" + statisticsEntity.getCardOrderNumber()));
			sb.append(PrintUtils.print("总交易额：" + statisticsEntity.getCardSalePrice()));
			sb.append(PrintUtils.print("总实收：" + statisticsEntity.getCardSalePrice()));
			sb.append(PrintUtils.print("*以上数据中不含已退款单"));
			printerQueue.add(PrintUtils.getByte(sb.toString()));

			printerQueue.add( PrintUtils.getByte(PrintUtils.printDottedLine(PrintUtils.LINE_2)));
			printerQueue.add(PrintUtils.getByte(PrintUtils.print("\n\n" + companyName + "\n" + companyUrl)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printerQueue;
	}

	private static Bitmap getOneCodeImg(String code, BarcodeFormat format, int width, int height) {
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
			return null;
		} catch (WriterException | NullPointerException e) {
			e.printStackTrace();
		}
		if (result == null)
			return null;
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
		if (bitmap == null){
			return null;
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF)
				return "UTF-8";
		}
		return null;
	}
}
