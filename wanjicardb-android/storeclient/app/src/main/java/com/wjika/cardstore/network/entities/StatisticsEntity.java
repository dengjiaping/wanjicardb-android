package com.wjika.cardstore.network.entities;

/**
 * Created by Liu_Zhichao on 2016/2/17 17:28.
 * 消费统计
 */
public class StatisticsEntity extends Entity {

	private String stime;//开始日期
	private String etime;//结束日期
	private int cardOrderNumber;//总单数
	private double cardSalePrice;//总交易金额，

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

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
}
