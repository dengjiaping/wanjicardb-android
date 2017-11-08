package com.wjika.cardstore.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/28.
 * 相册图片
 */
public class SelectPhotoEntity implements Parcelable {

	private String homeId;
	private String homeUrl;//首图
	@SerializedName("datas")
	private List<SelectPhotoEntity> selectPhotoEntities;

	private String merchantPhotoId;
	private String merchantPhotoAppsrc;//店铺图
	private String photoType;//

	private String imagePath;//本地图片的绝对路径
	private int cameraId;//相机图片的id
	private boolean flag;//是否选中

	public SelectPhotoEntity(int cameraId) {
		this.cameraId = cameraId;
	}

	public SelectPhotoEntity(String imagePath) {
		this.imagePath = imagePath;
	}

	protected SelectPhotoEntity(Parcel in) {
		homeId = in.readString();
		homeUrl = in.readString();
		selectPhotoEntities = in.createTypedArrayList(SelectPhotoEntity.CREATOR);
		merchantPhotoId = in.readString();
		merchantPhotoAppsrc = in.readString();
		photoType = in.readString();
		imagePath = in.readString();
		cameraId = in.readInt();
		flag = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(homeId);
		dest.writeString(homeUrl);
		dest.writeTypedList(selectPhotoEntities);
		dest.writeString(merchantPhotoId);
		dest.writeString(merchantPhotoAppsrc);
		dest.writeString(photoType);
		dest.writeString(imagePath);
		dest.writeInt(cameraId);
		dest.writeByte((byte) (flag ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SelectPhotoEntity> CREATOR = new Creator<SelectPhotoEntity>() {
		@Override
		public SelectPhotoEntity createFromParcel(Parcel in) {
			return new SelectPhotoEntity(in);
		}

		@Override
		public SelectPhotoEntity[] newArray(int size) {
			return new SelectPhotoEntity[size];
		}
	};

	public String getHomeId() {
		return homeId;
	}

	public void setHomeId(String homeId) {
		this.homeId = homeId;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public List<SelectPhotoEntity> getSelectPhotoEntities() {
		return selectPhotoEntities;
	}

	public void setSelectPhotoEntities(List<SelectPhotoEntity> selectPhotoEntities) {
		this.selectPhotoEntities = selectPhotoEntities;
	}

	public String getMerchantPhotoId() {
		return merchantPhotoId;
	}

	public void setMerchantPhotoId(String merchantPhotoId) {
		this.merchantPhotoId = merchantPhotoId;
	}

	public String getMerchantPhotoAppsrc() {
		return merchantPhotoAppsrc;
	}

	public void setMerchantPhotoAppsrc(String merchantPhotoAppsrc) {
		this.merchantPhotoAppsrc = merchantPhotoAppsrc;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getCameraId() {
		return cameraId;
	}

	public void setCameraId(int cameraId) {
		this.cameraId = cameraId;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
