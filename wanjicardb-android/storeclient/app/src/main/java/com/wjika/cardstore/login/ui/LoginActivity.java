package com.wjika.cardstore.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.UserEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.person.ui.PersonPWDActivity;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Liu_ZhiChao on 2015/11/17 18:59.
 * 登录页
 */
public class LoginActivity extends ToolBarActivity implements View.OnClickListener{

	private static final int REQUEST_CODE_USER_LOGIN = 100;
	public static final int PERSON_PWD_INTENT_CODE = 101;
	public static final String PERSON_PWD_INTENT = "startPeronPWD";

	@ViewInject(R.id.login_username)
	private EditText loginUsername;
	@ViewInject(R.id.login_password)
	private EditText loginPassword;
	@ViewInject(R.id.login_forget_pwd)
	private TextView loginForgetPwd;
	@ViewInject(R.id.login_confirm)
	private TextView loginConfirm;

	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		mTxtLeft.setText(getString(R.string.login));
		mTxtLeft.setVisibility(View.VISIBLE);
		mBtnTitleLeft.setVisibility(View.GONE);
		loginForgetPwd.setOnClickListener(this);
		loginConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_confirm:
				username = loginUsername.getText().toString().trim();
				String password = loginPassword.getText().toString().trim();
				if (TextUtils.isEmpty(username)){
					ToastUtil.shortShow(this, getString(R.string.login_empty_username));
					return;
				}
				if (TextUtils.isEmpty(password)) {
					ToastUtil.shortShow(this, getString(R.string.login_empty_pwd));
					return;
				}
				showProgressDialog();
				IdentityHashMap<String, String> param = new IdentityHashMap<>();
				param.put("merchantPhone", username);
				param.put("merchantPassword", password);
                param.put("userPushID", JPushInterface.getRegistrationID(this));
				requestHttpData(Constants.Urls.URL_POST_USER_LOGIN, REQUEST_CODE_USER_LOGIN, FProtocol.HttpMethod.POST, param);
				break;
			case R.id.login_forget_pwd:
				Intent intent = new Intent(this, PersonPWDActivity.class);
				intent.putExtra(PERSON_PWD_INTENT,PERSON_PWD_INTENT_CODE);
				startActivity(intent);
				break;
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_CODE_USER_LOGIN == requestCode){
			UserEntity userEntity = Parsers.parseUser(data);
			if (userEntity != null) {
				UserCenter.saveLoginInfo(this, userEntity);
				if(userEntity.isMain()){
					UserCenter.saveMerMobile(this,username);
				}
				UserCenter.saveAccount(this, username);
				startActivity(new Intent(this, MainActivity.class));
				finish();
			}else {
				ToastUtil.shortShow(this, getString(R.string.login_login_failue));
			}
		}
	}
}
