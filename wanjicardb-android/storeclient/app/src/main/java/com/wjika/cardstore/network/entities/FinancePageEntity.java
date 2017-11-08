package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2015/12/2.
 * 财务管理的pager页面
 */
public class FinancePageEntity extends Entity{
    private String dealTime;
    private String amount;
    private int category;

    @SerializedName("datas")
    private List<FinancePageEntity> financePageEntities;
    private int totalPage;

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<FinancePageEntity> getFinancePageEntities() {
        return financePageEntities;
    }

    public void setFinancePageEntities(List<FinancePageEntity> financePageEntities) {
        this.financePageEntities = financePageEntities;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
