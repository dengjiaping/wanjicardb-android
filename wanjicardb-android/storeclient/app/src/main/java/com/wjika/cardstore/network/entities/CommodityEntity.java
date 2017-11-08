package com.wjika.cardstore.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2015/11/30.
 * 商品卡
 */
public class CommodityEntity extends Entity {

    private String merchantCardId;
    private String merchantCardName;//卡名称
    private double cardSalePrice;//售价
    private double cardFacePrice;//面值
    private int merchantCardColor;//默认蓝色，1红 2黄 3蓝 4绿
    private String merchantCardPicurl;//logo地址
    private String merchantName;//总店名
    private int merchantCardSalesvolume;//已售数量
    private int merchantCardStatus;//1：上架 0：下架

    @SerializedName("datas")
    private List<CommodityEntity> commodityEntities;
    private int onShelf;
    private int offShelf;
    private int totalPage;

    public String getMerchantCardId() {
        return merchantCardId;
    }

    public void setMerchantCardId(String merchantCardId) {
        this.merchantCardId = merchantCardId;
    }

    public String getMerchantCardName() {
        return merchantCardName;
    }

    public void setMerchantCardName(String merchantCardName) {
        this.merchantCardName = merchantCardName;
    }

    public double getCardSalePrice() {
        return cardSalePrice;
    }

    public void setCardSalePrice(double cardSalePrice) {
        this.cardSalePrice = cardSalePrice;
    }

    public double getCardFacePrice() {
        return cardFacePrice;
    }

    public void setCardFacePrice(double cardFacePrice) {
        this.cardFacePrice = cardFacePrice;
    }

    public int getMerchantCardColor() {
        return merchantCardColor;
    }

    public void setMerchantCardColor(int merchantCardColor) {
        this.merchantCardColor = merchantCardColor;
    }

    public String getMerchantCardPicurl() {
        return merchantCardPicurl;
    }

    public void setMerchantCardPicurl(String merchantCardPicurl) {
        this.merchantCardPicurl = merchantCardPicurl;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getMerchantCardSalesvolume() {
        return merchantCardSalesvolume;
    }

    public void setMerchantCardSalesvolume(int merchantCardSalesvolume) {
        this.merchantCardSalesvolume = merchantCardSalesvolume;
    }

    public int getMerchantCardStatus() {
        return merchantCardStatus;
    }

    public void setMerchantCardStatus(int merchantCardStatus) {
        this.merchantCardStatus = merchantCardStatus;
    }

    public List<CommodityEntity> getCommodityEntities() {
        return commodityEntities;
    }

    public void setCommodityEntities(List<CommodityEntity> commodityEntities) {
        this.commodityEntities = commodityEntities;
    }

    public int getOnShelf() {
        return onShelf;
    }

    public void setOnShelf(int onShelf) {
        this.onShelf = onShelf;
    }

    public int getOffShelf() {
        return offShelf;
    }

    public void setOffShelf(int offShelf) {
        this.offShelf = offShelf;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}