package com.wjika.cardstore.person.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.network.Constants;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/13 20:46.
 * 意见反馈
 */
public class FeedbackActivity extends ToolBarActivity{

	private static final int REQUEST_CODE_UPLOAD_FEEDBACK = 100;

	private EditText feedbackInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_act);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_feedback));
		feedbackInput = (EditText) findViewById(R.id.feedback_input);
		findViewById(R.id.feedback_commit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = feedbackInput.getText().toString().trim();
				if (TextUtils.isEmpty(message)){
					ToastUtil.shortShow(FeedbackActivity.this, getString(R.string.person_feedback_input_suggest));
					return;
				}
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("message", message);
				requestHttpData(Constants.Urls.URL_POST_PERSON_FEEDBACK, REQUEST_CODE_UPLOAD_FEEDBACK, FProtocol.HttpMethod.POST, params);
			}
		});
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_CODE_UPLOAD_FEEDBACK == requestCode){
			ToastUtil.shortShow(this, getString(R.string.person_feedback_commit_success));
			finish();
		}
	}
}
