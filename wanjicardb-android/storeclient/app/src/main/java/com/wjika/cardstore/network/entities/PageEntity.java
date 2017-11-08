package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/1/13 13:25.
 * 分页数据
 */
public class PageEntity<T> extends Entity {

	private int totalPage;
	@SerializedName("datas")
	private List<T> result;

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
}
