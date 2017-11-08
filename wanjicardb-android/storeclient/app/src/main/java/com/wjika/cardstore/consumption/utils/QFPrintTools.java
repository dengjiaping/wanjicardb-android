package com.wjika.cardstore.consumption.utils;

import android.content.Context;
import android.os.RemoteException;

import com.common.utils.ToastUtil;
import com.seuic.android.PrintBarCodeListener;
import com.seuic.android.Printer;
import com.seuic.android.PrinterListener;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.utils.TimeUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by Liu_Zhichao on 2016/2/25 17:56.
 * 钱方pos机打印工具类
 */
public class QFPrintTools {

	private static final String TAIL = "万集融合信息技术（北京）有限公司\nhttp://www.wjika.com/\r\n\n\n\n\n\n\n\n";

	/**
	 * 钱方打印小票
	 * @param printer 打印类对象
	 * @param printerCallback 打印结果回调
	 *  一行16个汉字,32个字符
	 */
	public static void printNormal(Context context, Printer printer, PrinterListener.Stub printerCallback, ConsumptionEntity consumption){
		String status;
		if (1 == consumption.getTranStatus()){
			status = "交易成功";
		}else {
			status = "交易失败";
		}
		StringBuilder bill = new StringBuilder();
		String merName = "商家名称：" + UserCenter.getMerName(context);
		int length = (16 - merName.length()) / 2;
		for (int i = 0; i < length; i++) {
			bill.append("  ");
		}
		bill.append(merName + "\r\n\n");
		bill.append("订单号：" + consumption.getTransNo() + "\n付款人：" + consumption.getUserPhone() + "\n消费金额：" + consumption.getRealOrderAmount() +
				"\n当前状态：" + status + "\n付款时间：" + consumption.getTransDate());
		try {
			byte[] data_in = bill.toString().getBytes("GBK");
			int result;
			result = printer.startPrint(printerCallback, data_in);
			if ( result == -2 ) {
				ToastUtil.shortShow(context, "电量不足，停止打印");
			} else if ( 0 != result ) {
				ToastUtil.shortShow(context, "打印失败");
			}
		} catch (RemoteException e) {
			ToastUtil.shortShow(context, "打印失败");
		} catch (UnsupportedEncodingException e) {
			ToastUtil.shortShow(context, "打印失败");
		}
	}

	public static void printBarCode(final Context context, final Printer printer, String code) {
		try {
			int result = printer.printBarCode(new PrintBarCodeListener.Stub() {
				@Override
				public void OnSuccess() throws RemoteException {
					printTail(context, printer);
				}

				@Override
				public void OnFail(int returnCode) throws RemoteException {
					ToastUtil.shortShow(context, "打印失败");
				}
			}, code, 0, 384, 100);
			if ( result == -2 ) {
				ToastUtil.shortShow(context, "电量不足，停止打印");
			} else if ( 0 != result ) {
				ToastUtil.shortShow(context, "打印失败");
			}
		} catch (RemoteException e) {
			ToastUtil.shortShow(context, "打印失败");
			e.printStackTrace();
		}
	}

	/**
	 * 打印结尾部分
	 */
	public static void printTail(final Context context, Printer printer) {
		try {
			byte[] data_in = TAIL.getBytes("GBK");
			int result = printer.startPrint(null, data_in);
			if ( result == -2 ) {
				ToastUtil.shortShow(context, "电量不足，停止打印");
			} else if ( 0 != result ) {
				ToastUtil.shortShow(context, "打印失败");
			}
		} catch (UnsupportedEncodingException e) {
			ToastUtil.shortShow(context, "打印失败");
		} catch (RemoteException e) {
			ToastUtil.shortShow(context, "打印失败");
		}
	}

	/**
	 * 钱方打印统计数据
	 */
	public static void printStatistics(Context context, Printer printer, PrinterListener.Stub printerCallback, StatisticsEntity statistics) {
		StringBuilder bill = new StringBuilder();
		String name = "商家名称：" + UserCenter.getMerName(context);
		int length = (16 - name.length()) / 2;
		for (int i = 0; i < length; i++) {
			bill.append("  ");
		}
		String sdate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getStime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
		String edate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getEtime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
		String mediumSpline = "";//分隔线
		for (int i = 0; i < 32 - 5; i++) {
			mediumSpline += "-";
		}

		bill.append(name + "\r\n\n");
		bill.append("起始日期：" + sdate + "\n");
		bill.append("结束日期：" + edate + "\n");
		bill.append("总单数：" + statistics.getCardOrderNumber() + "\n");
		bill.append("总交易额：" + statistics.getCardSalePrice() + "\n");
		bill.append("总实收：" + statistics.getCardSalePrice() + "\n");
		bill.append("*以上数据中不含已退款单\n");
		bill.append(mediumSpline + "\n");
		bill.append(TAIL);
		try {
			byte[] data_in = bill.toString().getBytes("GBK");
			int result;
			result = printer.startPrint(printerCallback, data_in);
			if ( result == -2 ) {
				ToastUtil.shortShow(context, "电量不足，停止打印");
			} else if ( 0 != result ) {
				ToastUtil.shortShow(context, "打印失败");
			}
		} catch (RemoteException | UnsupportedEncodingException e) {
			ToastUtil.shortShow(context, "打印失败");
		}
	}
}
