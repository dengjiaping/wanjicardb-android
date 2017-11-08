package com.wjika.cardstore.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liu_Zhichao on 2016/1/13 11:20.
 * 消息通知
 */
public class MessageEntity extends Entity implements Parcelable{

	private String id;
	private String title;
	private String message;
	private String merchantId;
	private String createDate;
	private int type;//1：系统消息2：消费消息3：订单信息
	private int ifOpen;//是否发送0:未发送1：以发送2：以关闭
	private int ifRead;//是否已读0：未读1：已读
	private int receiveType;//接收消息类型1：商户端2：用户端3：所有
	private int basicType;//具体类型

	protected MessageEntity(Parcel in) {
		id = in.readString();
		title = in.readString();
		message = in.readString();
		merchantId = in.readString();
		createDate = in.readString();
		type = in.readInt();
		ifOpen = in.readInt();
		ifRead = in.readInt();
		receiveType = in.readInt();
		basicType = in.readInt();
	}

	public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
		@Override
		public MessageEntity createFromParcel(Parcel in) {
			return new MessageEntity(in);
		}

		@Override
		public MessageEntity[] newArray(int size) {
			return new MessageEntity[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIfOpen() {
		return ifOpen;
	}

	public void setIfOpen(int ifOpen) {
		this.ifOpen = ifOpen;
	}

	public int getIfRead() {
		return ifRead;
	}

	public void setIfRead(int ifRead) {
		this.ifRead = ifRead;
	}

	public int getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
	}

	public int getBasicType() {
		return basicType;
	}

	public void setBasicType(int basicType) {
		this.basicType = basicType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(message);
		dest.writeString(merchantId);
		dest.writeString(createDate);
		dest.writeInt(type);
		dest.writeInt(ifOpen);
		dest.writeInt(ifRead);
		dest.writeInt(receiveType);
		dest.writeInt(basicType);
	}
}
