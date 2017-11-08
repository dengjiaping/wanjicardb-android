package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kkkkk on 2016/1/25.
 * 订单实体
 */
public class OrderManagerEntity extends Entity {

	private int buyType;//0购卡，1充值
	private int cardOrderAmount;//数量
	private String cardOrderCreatedate;
	private String cardOrderNo;
	private String cardOrderPhone;
	private int cardOrderStatus;//0 支付中 1 支付完成 2 支付取消（已关闭） 3 待支付
	private String cardOrderValue;
	private double cardSalePrice;
	private String merchantCardName;

	@SerializedName("datas")
	private List<OrderManagerEntity> orderManagerEntities;
	private int totalPage;

	public int getBuyType() {
		return buyType;
	}

	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}

	public int getCardOrderAmount() {
		return cardOrderAmount;
	}

	public void setCardOrderAmount(int cardOrderAmount) {
		this.cardOrderAmount = cardOrderAmount;
	}

	public String getCardOrderCreatedate() {
		return cardOrderCreatedate;
	}

	public void setCardOrderCreatedate(String cardOrderCreatedate) {
		this.cardOrderCreatedate = cardOrderCreatedate;
	}

	public String getCardOrderNo() {
		return cardOrderNo;
	}

	public void setCardOrderNo(String cardOrderNo) {
		this.cardOrderNo = cardOrderNo;
	}

	public String getCardOrderPhone() {
		return cardOrderPhone;
	}

	public void setCardOrderPhone(String cardOrderPhone) {
		this.cardOrderPhone = cardOrderPhone;
	}

	public String getCardOrderStatus() {
		switch (cardOrderStatus) {
			case 0:
				return "支付中";
			case 1:
				return "已完成";
			case 2:
				return "已关闭";
			case 3:
				return "待支付";
			default:
				return "";
		}
	}

	public void setCardOrderStatus(int cardOrderStatus) {
		this.cardOrderStatus = cardOrderStatus;
	}

	public String getCardOrderValue() {
		return cardOrderValue;
	}

	public void setCardOrderValue(String cardOrderValue) {
		this.cardOrderValue = cardOrderValue;
	}

	public double getCardSalePrice() {
		return cardSalePrice;
	}

	public void setCardSalePrice(double cardSalePrice) {
		this.cardSalePrice = cardSalePrice;
	}

	public String getMerchantCardName() {
		return merchantCardName;
	}

	public void setMerchantCardName(String merchantCardName) {
		this.merchantCardName = merchantCardName;
	}

	public List<OrderManagerEntity> getOrderManagerEntities() {
		return orderManagerEntities;
	}

	public void setOrderManagerEntities(List<OrderManagerEntity> orderManagerEntities) {
		this.orderManagerEntities = orderManagerEntities;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
