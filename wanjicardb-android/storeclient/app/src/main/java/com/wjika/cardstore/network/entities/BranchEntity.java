package com.wjika.cardstore.network.entities;

/**
 * Created by Liu_Zhichao on 2016/1/12 17:24.
 * 分店信息
 */
public class BranchEntity {

	private String id;
	private String merchantName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
