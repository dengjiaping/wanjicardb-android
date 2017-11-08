package com.wjika.cardstore.consumption.utils;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by Liu_Zhichao on 2016/4/19 16:45.
 * 拉卡拉POS打印工具类
 */
public class LklPrintTools {

	public static void printNormal(final Context context, final AidlPrinter printer, final ConsumptionEntity consumption) {
		try {
			printer.printText(new ArrayList<PrintItemObj>(){
				{
					add(new PrintItemObj("商家名称：" + UserCenter.getMerName(context), 12, false, PrintItemObj.ALIGN.CENTER));
					add(new PrintItemObj("订单号：" + consumption.getTransNo()));
					add(new PrintItemObj("付款人：" + consumption.getUserPhone()));
					add(new PrintItemObj("消费金额：" + consumption.getRealOrderAmount()));
					add(new PrintItemObj("当前状态：" + (consumption.getTranStatus() == 1 ? "交易成功" : "交易失败")));
					add(new PrintItemObj("付款时间：" + consumption.getTransDate()));
				}
			}, new AidlPrinterListener.Stub() {

				@Override
				public void onPrintFinish() throws RemoteException {
					printBarCode(printer, consumption.getTransNo());
					printTail(printer);
				}

				@Override
				public void onError(int i) throws RemoteException {
					Log.e("TEST", "打印错误：" + i);
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/*BARCODE_TYPE_UPCA = 65;
		BARCODE_TYPE_UPCE = 66;
		BARCODE_TYPE_JAN13 = 67;
		BARCODE_TYPE_JAN8 = 68;
		BARCODE_TYPE_CODE39 = 69;
		BARCODE_TYPE_ITF = 70;
		BARCODE_TYPE_CODEBAR = 71;
		BARCODE_TYPE_CODE93 = 72;
		BARCODE_TYPE_CODE128 = 73;*/
	public static void printBarCode(final AidlPrinter printer, String code) {
		try {
			//参数顺序：width(-1表示自动宽度),height,leftoffse(左偏移量),barcodetype(一维码类型),barcode(要打印的一维码),listener(打印回调)
			printer.printBarCode(-1, 100, 20, 73, code, new AidlPrinterListener.Stub() {

				@Override
				public void onPrintFinish() throws RemoteException {

				}

				@Override
				public void onError(int i) throws RemoteException {
					Log.e("TEST", "打印错误：" + i);
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void printTail(AidlPrinter printer) {
		try {
			printer.printText(new ArrayList<PrintItemObj>(){
				{
					add(new PrintItemObj("    "));
					add(new PrintItemObj("万集融合信息技术（北京）有限公司"));
					add(new PrintItemObj("http://www.wjika.com/"));
					add(new PrintItemObj("    "));
					add(new PrintItemObj("    "));
					add(new PrintItemObj("    "));
				}
			}, new AidlPrinterListener.Stub() {

				@Override
				public void onPrintFinish() throws RemoteException {
					Log.d("TEST", "打印成功");
				}

				@Override
				public void onError(int i) throws RemoteException {
					Log.e("TEST", "打印错误：" + i);
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void printStatistics(final Context context, final AidlPrinter printer, final StatisticsEntity statistics) {
		try {
			printer.printText(new ArrayList<PrintItemObj>() {
				{
					String sdate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getStime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
					String edate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getEtime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
					String mediumSpline = "";//分隔线
					for (int i = 0; i < 32 - 5; i++) {
						mediumSpline += "-";
					}
					add(new PrintItemObj("商家名称：" + UserCenter.getMerName(context), 12, false, PrintItemObj.ALIGN.CENTER));
					add(new PrintItemObj("起始日期：" + sdate));
					add(new PrintItemObj("结束日期：" + edate));
					add(new PrintItemObj("总单数：" + statistics.getCardOrderNumber()));
					add(new PrintItemObj("总交易额：" + statistics.getCardSalePrice()));
					add(new PrintItemObj("总实收：" + statistics.getCardSalePrice()));
					add(new PrintItemObj("*以上数据中不含已退款单"));
					add(new PrintItemObj(mediumSpline));
				}
			}, new AidlPrinterListener.Stub() {

				@Override
				public void onPrintFinish() throws RemoteException {
					printTail(printer);
				}

				@Override
				public void onError(int i) throws RemoteException {
					Log.e("TEST", "打印错误：" + i);
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
