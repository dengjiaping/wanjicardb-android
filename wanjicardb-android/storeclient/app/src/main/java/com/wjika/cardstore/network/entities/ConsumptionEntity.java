package com.wjika.cardstore.network.entities;

/**
 * Created by Liu_Zhichao on 2016/2/14 17:15.
 * 消费
 */
public class ConsumptionEntity {

	private String orderNo;//消费单号
	private String userPhone;//消费用户手机号码
	private String transNo;//退款用
	private String transDate;//交易时间
	private double realOrderAmount;//订单实际支付金额
	private int tranStatus;//当前交易状态 0已失败 1 已成功  2 已撤销 3 已退货 4 已冲正 100全部

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public double getRealOrderAmount() {
		return realOrderAmount;
	}

	public void setRealOrderAmount(double realOrderAmount) {
		this.realOrderAmount = realOrderAmount;
	}

	public int getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(int tranStatus) {
		this.tranStatus = tranStatus;
	}
}
