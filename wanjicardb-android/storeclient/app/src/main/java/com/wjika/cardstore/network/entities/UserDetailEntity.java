package com.wjika.cardstore.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 张家洛 on 2016/3/8.
 * 店铺详情
 */
public class UserDetailEntity extends Entity implements Parcelable {

    private String merchantName;
    private String merchantBusinesshours;
    private String merchantIntroduce;
    private String merchantLinkman;
    private String merchantPhone;
    private String merchantAddress;
    private String merchantLatitude;
    private String merchantLongitude;
    private int merchantStatus;
    private int merchantId;

    protected UserDetailEntity(Parcel in) {
        merchantName = in.readString();
        merchantBusinesshours = in.readString();
        merchantIntroduce = in.readString();
        merchantLinkman = in.readString();
        merchantPhone = in.readString();
        merchantAddress = in.readString();
        merchantLatitude = in.readString();
        merchantLongitude = in.readString();
        merchantStatus = in.readInt();
        merchantId = in.readInt();
    }

    public static final Creator<UserDetailEntity> CREATOR = new Creator<UserDetailEntity>() {
        @Override
        public UserDetailEntity createFromParcel(Parcel in) {
            return new UserDetailEntity(in);
        }

        @Override
        public UserDetailEntity[] newArray(int size) {
            return new UserDetailEntity[size];
        }
    };

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantBusinesshours() {
        return merchantBusinesshours;
    }

    public void setMerchantBusinesshours(String merchantBusinesshours) {
        this.merchantBusinesshours = merchantBusinesshours;
    }

    public String getMerchantIntroduce() {
        return merchantIntroduce;
    }

    public void setMerchantIntroduce(String merchantIntroduce) {
        this.merchantIntroduce = merchantIntroduce;
    }

    public String getMerchantLinkman() {
        return merchantLinkman;
    }

    public void setMerchantLinkman(String merchantLinkman) {
        this.merchantLinkman = merchantLinkman;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantLatitude() {
        return merchantLatitude;
    }

    public void setMerchantLatitude(String merchantLatitude) {
        this.merchantLatitude = merchantLatitude;
    }

    public String getMerchantLongitude() {
        return merchantLongitude;
    }

    public void setMerchantLongitude(String merchantLongitude) {
        this.merchantLongitude = merchantLongitude;
    }

    public int getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(int merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(merchantName);
        dest.writeString(merchantBusinesshours);
        dest.writeString(merchantIntroduce);
        dest.writeString(merchantLinkman);
        dest.writeString(merchantPhone);
        dest.writeString(merchantAddress);
        dest.writeString(merchantLatitude);
        dest.writeString(merchantLongitude);
        dest.writeInt(merchantStatus);
        dest.writeInt(merchantId);
    }
}
