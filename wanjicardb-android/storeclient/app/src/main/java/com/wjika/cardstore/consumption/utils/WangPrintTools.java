package com.wjika.cardstore.consumption.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.utils.TimeUtil;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.LatticePrinter;
import cn.weipass.pos.sdk.LatticePrinter.FontFamily;
import cn.weipass.pos.sdk.LatticePrinter.FontSize;
import cn.weipass.pos.sdk.LatticePrinter.FontStyle;
import cn.weipass.pos.sdk.Printer;

/**
 * 旺pos打印小票工具类
 */
public class WangPrintTools {

	/**
	 * font_size:字体大小枚举值 SMALL:16x16大小; MEDIUM:24x24大小; LARGE:32x32大小; EXTRALARGE:48x48 一行的宽度为384
	 * (当宽度大小为16时可打印384/16=24个字符;为24时可打印384/24=16个字符;为32时可
	 * 打印384/32=12个字符;为48时可打印384/48=8个字符（一个汉字占1个字符，一个字母 、空格或者数字占半字符）
	 * 标准打印示例
	 * @param context
	 * @param printer
	 */
	public static final int rowSize = 384;
	// public static final int smallSize = (int) (384/16d);
	// public static final int mediumSize = (int) (384/24d);
	// public static final int largeSize = (int) (384/32d);
	// public static final int extralargeSize = (int) (384/48d);
	public static final int smallSize = 24 * 2;
	public static final int mediumSize = 16 * 2;
	public static final int largeSize = 12 * 2;
	public static final int extralargeSize = 8 * 2;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static String getPrintErrorInfo(int what, String info) {
		String message = "";
		switch (what) {
		case IPrint.EVENT_CONNECT_FAILD:
			message = "连接打印机失败";
			break;
		case IPrint.EVENT_CONNECTED:
			// Log.e("subscribe_msg", "连接打印机成功");
			break;
		case IPrint.EVENT_PAPER_JAM:
			message = "打印机卡纸";
			break;
		case IPrint.EVENT_UNKNOW:
			message = "打印机未知错误";
			break;
		case IPrint.EVENT_OK:
			// 回调函数中不能做UI操作，所以可以使用runOnUiThread函数来包装一下代码块
			// Log.e("subscribe_msg", "打印机正常");
			break;
		case IPrint.EVENT_NO_PAPER:
			message = "打印机缺纸";
			break;
		case IPrint.EVENT_HIGH_TEMP:
			message = "打印机高温";
			break;
		}
		return message;
	}

	/**
	 * 获取md5加密信息
	 * @param s
	 * @return
	 */
	public static String getStringMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 打印时间段消费统计
	 * @param printer
	 * @param statistics
	 */
	public static void printStatistics(Printer printer, StatisticsEntity statistics, String curShopName){
		String mediumSpline = "";//分隔线
		for (int i = 0; i < mediumSize - 5; i++) {
			mediumSpline += "-";
		}
		String sdate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getStime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);
		String edate = TimeUtil.formatTime(TimeUtil.parseTime(statistics.getEtime(), TimeUtil.SIMPLE_DATE_PATTERN), TimeUtil.TIME_FORMAT_ONE);

		printer.printText("商家名称：" + curShopName + "\n", Printer.FontFamily.SONG, Printer.FontSize.LARGE, Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
		printer.printText("起始日期：" + sdate, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText("结束日期：" + edate, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText("总单数：" + statistics.getCardOrderNumber(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText("总交易额：" + statistics.getCardSalePrice(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText("总实收：" + statistics.getCardSalePrice(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText("*以上数据中不含已退款单", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
		printer.printText(mediumSpline, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
		printer.printText("万集融合信息技术（北京）有限公司\nhttp://www.wjika.com/\n\n\n\n\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
	}

	/**
	 * 打印单张小票
	 * \n 代码换行
	 * @param printer
	 * @param payment
	 */
	public static void printNormal(Printer printer, ConsumptionEntity payment, Context context) {
		// 标准打印，每个字符打印所占位置可能有一点出入（尤其是英文字符）
		String mediumSpline = "";//分隔线
		for (int i = 0; i < mediumSize - 5; i++) {
			mediumSpline += "-";
		}
		String status;
		if (1 == payment.getTranStatus()){
			status = "交易成功";
		}else {
			status = "交易失败";
		}
		printer.printText("商家名称：" + UserCenter.getMerName(context), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
				Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
		printer.printText("订单号：" + payment.getTransNo() + "\n付款人：" + payment.getUserPhone() + "\n消费金额：" + payment.getRealOrderAmount() +
				"\n当前状态：" + status + "\n付款时间：" + payment.getTransDate(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
				Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

		// 绘制表格，设定好每列所用宽度
		// 每列转换为可用英文字符长度(加起来要等于对应行长度，如medium字体=mediumSize：32)
		int row1 = 16;
		int row2 = 6;
		int row3 = 10;
		String headerRow1 = "商品名称";
		String headerRow2 = "数量";
		String headerRow3 = "金额";
		ArrayList<ConsumptionEntity> itemList = new ArrayList<>();
		/*Payment item = new Payment();
		item.Paymentno = "04342385738";//订单号
		item.Username = "15600720501";//支付用户
		item.Status = 30;//支付状态
		item.Date = "2015-07-31";//支付日期
		item.Amount = 198;//支付金额
		itemList.add(item);*/

		StringBuilder sbTable = new StringBuilder();
		sbTable.append(mediumSpline + "\n");
		/*String str1 = headerRow1 + WangPrintTools.getBlankBySize(row1 - WangPrintTools.length(headerRow1));//文字宽度加上(第一列宽度减去文字宽度，剩下宽度显示空格)
		String str2 = headerRow2 + WangPrintTools.getBlankBySize(row2 - WangPrintTools.length(headerRow2));
		String str3 = " " + headerRow3;//最后列就直接显示

		String headerStr = str1 + str2 + str3;
		sbTable.append(headerStr + "\n");
		sbTable.append(mediumSpline + "\n");*/
		/*for (int i = 0; i < itemList.size(); i++) {
			ItemInfo info = itemList.get(i);
			double nameSize = WangPrintTools.length(info.name);
			if (nameSize > row1) {
				// 列内容长度大于最大列长度,当成一行内容（换行）
				sbTable.append(info.name + "\n");
				// 数量和价格不会超过最大列宽，就不判断内容是否超出了
				String newLineSecond = info.count + WangPrintTools.getBlankBySize(row2 - WangPrintTools.length(info.count));
				String newLineEnd = info.price + "\n";
				String newLineAll = newLineSecond + newLineEnd;
				// 左边补足row1长度空格
				sbTable.append(WangPrintTools.getBlankBySize(row1 - 1) + newLineAll);
			} else {
				// 正常
				String rowFirst = info.name + WangPrintTools.getBlankBySize(row1 - WangPrintTools.length(info.name));
				String rowSecond = info.count + WangPrintTools.getBlankBySize(row2 - WangPrintTools.length(info.count));
				// 最后直接换行就可以了
				String rowEnd = info.price + "\n";
				sbTable.append(rowFirst + rowSecond + rowEnd);
			}
		}*/
//		sbTable.append(mediumSpline + "\n");
//		sbTable.append("商品总数：2件\n");
//		sbTable.append("消费总金额：124.5元\n");
//		sbTable.append(mediumSpline);
//		printer.printText(sbTable.toString(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
		//打印最后内容，并且换行5次，注意：标准打印不支持进纸命令feed(int arg)
//		printer.printQrCode(item.Paymentno + "\n",16,Printer.Gravity.CENTER);//打印二维码

//		printer.printImage(getOneCode(item.Paymentno, 70, 14),Printer.Gravity.CENTER);//打印二维码
		byte[] oneCode = getOneCode(payment.getTransNo(), 380, 100);
		if (oneCode != null){
			printer.printImage(oneCode,Printer.Gravity.CENTER);//打印一维码
		}
		printer.printText("万集融合信息技术（北京）有限公司\nhttp://www.wjika.com/\n\n\n\n\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
	}

	/**
	 * \n 代表换行 点阵打印示例
	 * @param context
	 * @param latticePrinter
	 */
	public static void printLattice(Context context, LatticePrinter latticePrinter) {
		String mediumSpline = "";
		for (int i = 0; i < mediumSize; i++) {
			mediumSpline += "-";
		}

		// 打印logo图片,图片大小需要自己控制byte[]大小（总宽度一样是384），而且打印只能识别黑色图片
//		Drawable logo = context.getResources().getDrawable(R.drawable.print_logo);
//		latticePrinter.printImage(bitmap2Bytes(drawableToBitmap(logo)), Gravity.CENTER);
		//备注，点阵打印FontSize.EXTRALARGE字体不支持
		String title = "大众点评";
		int sizeTitle = largeSize - WangPrintTools.length(title);
		// 文字居中需要在前面补足相应空格，后面可以用换行符换行
		String titleStr = getBlankBySize((int) (sizeTitle / 2d)) + title;
		latticePrinter.printText(titleStr + "\n", FontFamily.SONG, FontSize.LARGE, FontStyle.BOLD);
		
		String webUrl = "www.dianping.com";
		int sizeWebUrl = mediumSize - WangPrintTools.length(webUrl);
		String webUrlStr = getBlankBySize((int) (sizeWebUrl / 2d)) + webUrl;
		latticePrinter.printText(webUrlStr + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		
		int feedCount = 1;
		//进纸数，1代表进一行的高度
		latticePrinter.feed(feedCount);
		// 打印分割线
		latticePrinter.printText(mediumSpline + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		latticePrinter.feed(feedCount);

		String result = "验证消费成功";
		int sizeResult = largeSize - WangPrintTools.length(result);
		String resultStr = getBlankBySize((int) (sizeResult / 2d)) + result;
		latticePrinter.printText(resultStr + "\n", FontFamily.SONG, FontSize.LARGE, FontStyle.BOLD);
		
		latticePrinter.feed(feedCount);
		
		String name = "套餐名：肯德基100元代金券";
		latticePrinter.printText(name + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);

		String price = "售价：90元";
		latticePrinter.printText(price + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);

		String marchent = "商户名：KFC（中山公园站）";
		latticePrinter.printText(marchent + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);

		String time = "消费时间：2015-06-07 09:27";
		latticePrinter.printText(time + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		
		latticePrinter.feed(feedCount);
		// 打印分割线
		latticePrinter.printText(mediumSpline + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		latticePrinter.feed(feedCount);
		
		String count = "验券张数：5张";
		latticePrinter.printText(count + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		String totalPrice = "总额：450元";
		latticePrinter.printText(totalPrice + "\n", FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		// 绘制序列号
		String header = "序列号：";
		String headerBlank = getBlankBySize(WangPrintTools.length(header));

		ArrayList<String> dataList = new ArrayList<String>();
		dataList.add("1234 4567 4565");
		dataList.add("2345 3454 4546");
		dataList.add("2344 2343 3545");
		dataList.add("3445 2345 4576");
		dataList.add("5353 2343 7552");
		StringBuffer sb = new StringBuffer();
		int size = dataList.size();
		for (int i = 0; i < size; i++) {
			String item = dataList.get(i);
			if (i == 0) {
				sb.append(header + item + "\n");
			} else {
				//分行，预留空白内容
				sb.append(headerBlank + item + "\n");
			}
		}
		latticePrinter.printText(sb.toString(), FontFamily.SONG, FontSize.MEDIUM, FontStyle.BOLD);
		//最后进纸5行,方便撕纸
		latticePrinter.feed(5);
		// 真正提交打印事件
		latticePrinter.submitPrint();
	}

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0;
	}

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * @param s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
	 * @param s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	public static double getLength(String s) {
		if (s == null) {
			return 0;
		}
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++) {
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}
		}
		// 进位取整
		return Math.ceil(valueLength);
	}

	public static String getBlankBySize(int size) {
		String resultStr = "";
		for (int i = 0; i < size; i++) {
			resultStr += " ";
		}
		return resultStr;
	}

	// 将Drawable转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	// Bitmap → byte[]
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 获取打印时一维码的数组
	 * @param code
	 * @return
	 */
	public static byte[] getOneCode(String code, int width, int height){
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(code);
		if (encoding != null) {
			hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}

		BitMatrix result;
		try {
			result = new MultiFormatWriter().encode(code, BarcodeFormat.CODE_128, width, height, hints);
		} catch (IllegalArgumentException | WriterException e) {
			e.printStackTrace();
			return null;
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
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap2Bytes(bitmap);
	}

	public static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
}
