package com.wjika.cardstore.network;

/**
 * Created by jacktian on 15/8/19.
 * 接口
 */
public class Constants {

	public static class Urls {
		// 当前使用域名，测试环境
//	    private static final String URL_BASE_DOMAIN = "http://123.56.253.122";
		// 当前使用域名，正式环境
		private static String URL_BASE_DOMAIN = "http://mobilecardb.wjika.com";
		//登录
		public static final String URL_POST_USER_LOGIN = URL_BASE_DOMAIN + "/merchant/login";
		//退出
		public static final String URL_POST_USER_LOGOUT = URL_BASE_DOMAIN + "/merchant/exits";
		//获取商家订单量营业额
		public static final String URL_GET_STORE_DATA = URL_BASE_DOMAIN + "/merchant/homePage/relatedInformationer";
		//获取商家信息
		public static final String URL_POST_STORE_INFO = URL_BASE_DOMAIN + "/merchant/merchantMsg";
		//获取商家消息通知
		public static final String URL_GET_MESSAGE_INFO = URL_BASE_DOMAIN + "/dynamic/dynamicLists";
		//意见反馈
		public static final String URL_POST_PERSON_FEEDBACK = URL_BASE_DOMAIN + "/merchant/feedback";
		//应用升级提醒接口
		public static final String URL_GET_UPDATE_VERSION = URL_BASE_DOMAIN + "/merchant/versionCompare";
		//获取商户下卡产品信息
		public static final String URL_GET_MERCHANT_PRODUCTS = URL_BASE_DOMAIN + "/merchantCard/cardLister";
		//修改商品的状态（上下架）
		public static final String URL_POST_PRODUCT_UPDATESTATUS = URL_BASE_DOMAIN + "/merchantCard/cardChange";
		//商户资金提现记录
		public static final String URL_GET_MERCHANT_WITHDRAWALS = URL_BASE_DOMAIN + "merchant/withdrawals";
		//获取验证码
		public static final String URL_GET_SEND_SMS = URL_BASE_DOMAIN + "/common/validatecode/generateCode";
		//校验验证码
		public static final String URL_POST_VERIFY_SMS = URL_BASE_DOMAIN + "/merchant/verificationer";
		//找回密码
		public static final String URL_POST_FIND_PWD = URL_BASE_DOMAIN + "/merchant/changePassword";
		//订单管理所有数据
		public static final String URL_GET_ORDER_MANAGER_ALL = URL_BASE_DOMAIN + "/merchant/order/orderStatuslister";
		//扫码消费
		public static final String URL_POST_SCAN_CONSUMPTION = URL_BASE_DOMAIN + "/consume/pay";
		//获取财务信息
		public static final String URL_GET_MERCHANT_FINANCE = URL_BASE_DOMAIN + "/finance/getWithdrawRecorde";
		//修改店铺状态
		public static final String URL_POST_MERCHANT_UPDATESHOP = URL_BASE_DOMAIN + "/merchant/statused";
		//店铺相册
		public static final String URL_GET_SHOP_PHOTO = URL_BASE_DOMAIN + "/merchant/merchantPhotos";
		//店铺照片删除,批量删除使用逗号分隔
		public static final String URL_POST_DELETE_PHOTO = URL_BASE_DOMAIN + "/merchant/delPhoto";
		//上传照片
		public static final String URL_POST_UPLOAD_PHOTO = URL_BASE_DOMAIN + "/merchant/merchantPic";
		//分店列表
		public static final String URL_POST_SUBBRANCH_LIST = URL_BASE_DOMAIN + "/merchant/merchantSubbranch";
		//消费列表
		public static final String URL_GET_CONSUMPTION_LIST = URL_BASE_DOMAIN + "/userConsumerRecord/merchant/list";
		//获取消费统计数据
		public static final String URL_GET_STATISTICS_DATA = URL_BASE_DOMAIN + "/merchant/merchantTimeBucketer";
		//消费退款
		public static final String URL_POST_CONSUMPTION_REFUND = URL_BASE_DOMAIN + "/consume/return";
		//更新店铺信息
		public static final String URL_POST_UPDATE_INFO = URL_BASE_DOMAIN + "/merchant/merchantUpdate";
	}
}
