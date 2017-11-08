package com.wjika.cardstore.network.entities;

/**
 * Created by Liu_Zhichao on 2016/1/12 21:11.
 * 商家销售数据
 */
public class MerDataEntity extends Entity {

	private int cardOrderNumber;
	private double cardSalePrice;
	private double financeMoney;

	public int getCardOrderNumber() {
		return cardOrderNumber;
	}

	public void setCardOrderNumber(int cardOrderNumber) {
		this.cardOrderNumber = cardOrderNumber;
	}

	public double getCardSalePrice() {
		return cardSalePrice;
	}

	public void setCardSalePrice(double cardSalePrice) {
		this.cardSalePrice = cardSalePrice;
	}

	public double getFinanceMoney() {
		return financeMoney;
	}

	public void setFinanceMoney(double financeMoney) {
		this.financeMoney = financeMoney;
	}
}
