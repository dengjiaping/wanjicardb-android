package com.wjika.cardstore.person.ui;

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
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/14 11:23.
 * 修改密码
 */
public class AlterPWDActivity extends ToolBarActivity {

	private static final int REQUEST_NET_FINDPWD_CODE = 100;

	@ViewInject(R.id.alter_pwd_input)
	private EditText alterPwdInput;
	@ViewInject(R.id.alter_pwd_input_again)
	private EditText alterPwdInputAgain;
	@ViewInject(R.id.alter_pwd_confirm)
	private TextView alterPwdConfirm;
	private String phone;
	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_pwd_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_pwd_find_password));
		phone = getIntent().getStringExtra("phone");
		code = getIntent().getStringExtra("code");
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)){
			alterPwdConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String pwd = alterPwdInput.getText().toString().trim();
					String pwdAgain = alterPwdInputAgain.getText().toString().trim();
					if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
						ToastUtil.shortShow(AlterPWDActivity.this, getString(R.string.person_pwd_pass_not_null));
						return;
					}
					if (!pwd.equals(pwdAgain)) {
						ToastUtil.shortShow(AlterPWDActivity.this, getString(R.string.person_pwd_pass_not_equals));
						return;
					}
					IdentityHashMap<String, String> param = new IdentityHashMap<>();
					param.put("merchantPhone", phone);
					param.put("validateCode", code);
					param.put("merchantPassword", pwd);
					requestHttpData(Constants.Urls.URL_POST_FIND_PWD, REQUEST_NET_FINDPWD_CODE, FProtocol.HttpMethod.POST, param);
				}
			});
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_FINDPWD_CODE == requestCode){
			ToastUtil.shortShow(this, getString(R.string.person_pwd_fix_sucess));
			setResult(RESULT_OK);
			finish();
		}
	}
}
