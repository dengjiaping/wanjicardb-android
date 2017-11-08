package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Liu_Zhichao on 2016/1/12 17:41.
 * 店铺商品信息
 */
public class ProductEntity {

	@SerializedName("Id")
	private String id;
	@SerializedName("Name")
	private String name;
	@SerializedName("MerName")
	private String merName;
	@SerializedName("Cover")
	private String cover;
	@SerializedName("Facevalue")
	private String facevalue;
	@SerializedName("Saleprice")
	private String saleprice;
	@SerializedName("Ctype")
	private int ctype;
	@SerializedName("SaledNum")
	private int saledNum;
	@SerializedName("Status")
	private int status;//商品状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getFacevalue() {
		return facevalue;
	}

	public void setFacevalue(String facevalue) {
		this.facevalue = facevalue;
	}

	public String getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}

	public int getCtype() {
		return ctype;
	}

	public void setCtype(int ctype) {
		this.ctype = ctype;
	}

	public int getSaledNum() {
		return saledNum;
	}

	public void setSaledNum(int saledNum) {
		this.saledNum = saledNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
