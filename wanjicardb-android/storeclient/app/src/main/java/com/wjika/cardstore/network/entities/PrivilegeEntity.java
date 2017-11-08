package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Liu_Zhichao on 2016/1/12 17:35.
 * 商家特权
 */
public class PrivilegeEntity {

	@SerializedName("Id")
	private String id;
	@SerializedName("ImgUrl")
	private String imgUrl;
	@SerializedName("Detail")
	private String detail;
	@SerializedName("Desc")
	private String desc;
	@SerializedName("CreateDate")
	private String createDate;
	@SerializedName("IsHave")
	private boolean isHave;
	@SerializedName("Type")
	private int type;
}
