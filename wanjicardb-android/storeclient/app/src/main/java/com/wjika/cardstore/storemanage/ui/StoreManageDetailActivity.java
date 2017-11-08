package com.wjika.cardstore.storemanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.UserDetailEntity;
import com.wjika.cardstore.utils.ViewInjectUtils;

/**
 * Created by zhangzhaohui on 2016/1/8.
 * 店铺信息
 */
public class StoreManageDetailActivity extends ToolBarActivity implements View.OnClickListener {

	public static final int LINKMAN = 0x1;
	public static final int TELEPHONE = 0x2;
	public static final int LOCATION = 0x3;
	public static final int TIME = 0x4;

	public static final String SAVE_LINKMAN = "save_linkman";
	public static final String OPEN_TIME = "open_time";
	public static final String CLOSE_TIME = "close_time";

	//    @ViewInject(R.id.store_message_linkman)
//    private LinearLayout storeMessageLinkman;
	@ViewInject(R.id.store_manage_contact)
	private TextView storeManageContact;
	@ViewInject(R.id.store_message_two_dimension_code)
	private LinearLayout storeMessageTwoDimensionCode;
	//    @ViewInject(R.id.store_message_tel)
//    private LinearLayout storeMessageTel;
	@ViewInject(R.id.store_manage_phone)
	private TextView storeManagePhone;
	@ViewInject(R.id.store_message_location)
	private LinearLayout storeMessageLocation;
	//    @ViewInject(R.id.store_message_business_hours)
//    private LinearLayout storeMessageBusinessHours;
	@ViewInject(R.id.store_manage_time)
	private TextView storeManageTime;
	@ViewInject(R.id.store_manage_store_name)
	private TextView storeManageStoreName;
	@ViewInject(R.id.store_manage_store_address)
	private TextView storeManageStoreAddress;
	@ViewInject(R.id.store_manage_introduce)
	private TextView storeManageIntroduce;
	@ViewInject(R.id.image_adress)
	private ImageView imageAdress;
	private int merchantId;
	private UserDetailEntity userDetailEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_message_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_manager_store_info));
		if (UserCenter.isMain(this)) {
			//如果是总店地址暂不能修改。
			storeMessageLocation.setClickable(false);
			imageAdress.setVisibility(View.INVISIBLE);
		} else {
			storeMessageLocation.setOnClickListener(this);
		}
		// TODO: 2016/3/11 注释的地方暂时不删。
//        storeMessageLinkman.setOnClickListener(this);
		storeMessageTwoDimensionCode.setOnClickListener(this);
//        storeMessageTel.setOnClickListener(this);
//        storeMessageBusinessHours.setOnClickListener(this);
		userDetailEntity = getIntent().getParcelableExtra("userEntity");
		if (userDetailEntity != null) {
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantName())) {
				storeManageStoreName.setText(userDetailEntity.getMerchantName());
			}
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantPhone())) {
				storeManagePhone.setText(userDetailEntity.getMerchantPhone());
			}
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantLinkman())) {
				storeManageContact.setText(userDetailEntity.getMerchantLinkman());
			}
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantAddress())) {
				storeManageStoreAddress.setText(userDetailEntity.getMerchantAddress());
			}
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantBusinesshours())) {
				storeManageTime.setText(userDetailEntity.getMerchantBusinesshours());
			}
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantIntroduce())) {
				storeManageIntroduce.setText(userDetailEntity.getMerchantIntroduce());
			}
			merchantId = userDetailEntity.getMerchantId();
			if (!TextUtils.isEmpty(userDetailEntity.getMerchantIntroduce())) {
				CharSequence charSequence = Html.fromHtml(userDetailEntity.getMerchantIntroduce());
				storeManageIntroduce.setText(charSequence);
				storeManageIntroduce.setMovementMethod(LinkMovementMethod.getInstance());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//            case R.id.store_message_linkman:
//                Intent intent = new Intent();
//                intent.setClass(this, StoreManageSetMessageActivity.class);
//                intent.putExtra(StoreManageSetMessageActivity.EXTRA_FROM, StoreManageSetMessageActivity.LINKMAN);
//                startActivityForResult(intent,LINKMAN);
//                break;
			case R.id.store_message_two_dimension_code:
				Intent intent = new Intent();
				intent.setClass(this, StoreTwoDimensionCode.class);
				intent.putExtra("merchantId", merchantId);
				startActivity(intent);
				break;
//            case R.id.store_message_tel:
//                startActivityForResult(new Intent(this,StoreManageSetMessageActivity.class),TELEPHONE);
//                break;
			case R.id.store_message_location:
				startActivityForResult(new Intent(this, StoreManageLocationActivity.class), LOCATION);
				break;
//            case R.id.store_message_business_hours:
//                startActivityForResult(new Intent(this,StoreManageShopHoursActivity.class),TIME);
//                break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && RESULT_OK == resultCode) {
			switch (requestCode) {
				case LINKMAN:
//                    String linkman = data.getStringExtra(SAVE_LINKMAN);
//                    storeManageContact.setText(linkman);
					break;
				case TELEPHONE:
//                    String phone = data.getStringExtra(SAVE_LINKMAN);
//                    storeManagePhone.setText(phone);
					break;
				case LOCATION:
					String address = data.getStringExtra("address");
					if (!TextUtils.isEmpty(address)) {
						storeManageStoreAddress.setText(address);
						userDetailEntity.setMerchantAddress(address);
					}
					break;
				case TIME:
//                    String openTime = data.getStringExtra(OPEN_TIME);
//                    String closeTime = data.getStringExtra(CLOSE_TIME);
//                    storeManageTime.setText(openTime + "-" +closeTime);
					break;
			}
		}
	}
}
