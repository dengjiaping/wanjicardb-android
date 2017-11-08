package com.wjika.cardstore.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liu_ZhiChao on 2015/11/23 15:49.
 * 登录用户
 */
public class UserEntity extends Entity implements Parcelable{

	private String token;
	private String userName;
	private String merchantName;
	private boolean isMain;

	protected UserEntity(Parcel in) {
		token = in.readString();
		userName = in.readString();
		merchantName = in.readString();
		isMain = in.readByte() != 0;
	}

	public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
		@Override
		public UserEntity createFromParcel(Parcel in) {
			return new UserEntity(in);
		}

		@Override
		public UserEntity[] newArray(int size) {
			return new UserEntity[size];
		}
	};

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean main) {
		isMain = main;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(token);
		dest.writeString(userName);
		dest.writeString(merchantName);
		dest.writeByte((byte) (isMain ? 1 : 0));
	}
}
