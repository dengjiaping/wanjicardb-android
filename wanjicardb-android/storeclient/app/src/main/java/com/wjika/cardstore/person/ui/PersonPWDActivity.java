package com.wjika.cardstore.person.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.Entity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/13 20:13.
 * 修改找回密码
 */
public class PersonPWDActivity extends ToolBarActivity implements View.OnClickListener{

	private static final int REQUEST_ACT_PWD_CODE = 100;
	private static final int REQUEST_NET_GET_CODE = 200;
	private static final int REQUEST_NET_VERIFY_CODE = 300;

	@ViewInject(R.id.person_pwd_phone)
	private EditText personPwdPhone;
	@ViewInject(R.id.person_pwd_code)
	private EditText personPwdCode;
	@ViewInject(R.id.person_pwd_get_code)
	private TextView personPwdGetCode;
	@ViewInject(R.id.person_pwd_next)
	private TextView personPwdNext;

	private CountDownTimer countDownTimer;
	private String phone;
	private String code;
	private int intentCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_pwd_act);
		ViewInjectUtils.inject(this);
		getWhichActivity();
	}

	private void getWhichActivity() {
		Intent intent = getIntent();
		intentCode = intent.getIntExtra(LoginActivity.PERSON_PWD_INTENT,0);
		if (intentCode == LoginActivity.PERSON_PWD_INTENT_CODE) {
			setLeftTitle(getString(R.string.person_pwd_find_password));
			personPwdPhone.setFocusable(true);
			personPwdPhone.setClickable(true);
		} else {
			setLeftTitle(getString(R.string.person_fix_password));
			getPhone();
			personPwdPhone.setFocusable(false);
			personPwdPhone.setClickable(false);
		}
		countDownTime();
		personPwdGetCode.setOnClickListener(this);
		personPwdNext.setOnClickListener(this);
	}

	private void getPhone() {
		if (intentCode == LoginActivity.PERSON_PWD_INTENT_CODE) {
			String phoneText = personPwdPhone.getText().toString().trim();
			if (!TextUtils.isEmpty(phoneText) ) {
				if(11 == phoneText.length() && phoneText.startsWith("1")){
					phone = phoneText;
				}else {
					ToastUtil.shortShow(this,getString(R.string.person_enter_correct_num));
				}
			}else {
				ToastUtil.shortShow(this,getString(R.string.person_pwd_phone_not_null));
			}
		} else {
			phone = UserCenter.getMobile(this);//总店一定是用手机号登录的
			if (!TextUtils.isEmpty(phone) && 11 == phone.length() && phone.startsWith("1")){
				personPwdPhone.setText(phone);
			}else {
				ToastUtil.shortShow(this, getString(R.string.person_pwd_not_set));
			}
		}
	}

	private void countDownTime() {
		countDownTimer = new CountDownTimer(60 * 1000, 1000){

			@Override
			public void onTick(long millisUntilFinished) {
				personPwdGetCode.setText(String.format(getString(R.string.person_second),(millisUntilFinished / 1000)));
			}

			@Override
			public void onFinish() {
				personPwdGetCode.setClickable(true);
				personPwdGetCode.setText(getString(R.string.person_pwd_get_code));
				cancel();
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.person_pwd_get_code:
				getPhone();
				if (!TextUtils.isEmpty(phone)) {
					personPwdGetCode.setClickable(false);
					showProgressDialog();
					IdentityHashMap<String, String> param = new IdentityHashMap<>();
					param.put("phone", phone);
					requestHttpData(Constants.Urls.URL_GET_SEND_SMS, REQUEST_NET_GET_CODE, FProtocol.HttpMethod.POST, param);
				}
				break;
			case R.id.person_pwd_next:
				code = personPwdCode.getText().toString().trim();
				if (TextUtils.isEmpty(code)){
					ToastUtil.shortShow(this, getString(R.string.person_pwd_input_code));
					return;
				}

				showProgressDialog();
				IdentityHashMap<String, String> paramVerify = new IdentityHashMap<>();
				paramVerify.put("merchantPhone", phone);
				paramVerify.put("validateCode", code);
				requestHttpData(Constants.Urls.URL_POST_VERIFY_SMS, REQUEST_NET_VERIFY_CODE, FProtocol.HttpMethod.POST, paramVerify);
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity entity = Parsers.parseResult(data);
		if (REQUEST_NET_GET_CODE == requestCode && !REQUEST_SUCCESS_CODE.equals(entity.getRspCode())) {
			ToastUtil.shortShow(this, entity.getRspMsg());
			personPwdGetCode.setClickable(true);
		} else {
			super.success(requestCode, data);
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_GET_CODE == requestCode) {
			countDownTimer.start();
			ToastUtil.shortShow(this, getString(R.string.person_pwd_code_success));
		}else if (REQUEST_NET_VERIFY_CODE == requestCode){
			countDownTimer.onFinish();
			Intent intent = new Intent(this, AlterPWDActivity.class);
			intent.putExtra("phone", phone);
			intent.putExtra("code", code);
			startActivityForResult(intent, REQUEST_ACT_PWD_CODE);
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		if (REQUEST_NET_GET_CODE == requestCode) {
			personPwdGetCode.setClickable(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && REQUEST_ACT_PWD_CODE == requestCode){
			finish();
		}
	}
}
