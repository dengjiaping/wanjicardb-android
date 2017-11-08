package com.wjika.cardstore.base.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjika.cardstore.R;
import com.wjika.cardstore.utils.InputMethodUtil;

/**
 * Created by zhaoweiwei on 2016/1/26.
 * 头部搜索activity
 */
public class SearchBarActivity extends BaseActivity {

	protected ImageView mImageBack;
	protected EditText mEditSearch;
	protected TextView mTextSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.base_search_frame);
		ViewGroup root = (ViewGroup) findViewById(R.id.search_frame_container);
		View.inflate(this, layoutResID, root);
		initSearchBar();
	}

	private void initSearchBar() {
		mImageBack= (ImageView) findViewById(R.id.search_left_button);
		mEditSearch= (EditText) findViewById(R.id.edit_search);
		mTextSearch= (TextView) findViewById(R.id.search_rigth_text);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodUtil.showInput(SearchBarActivity.this, mEditSearch);
			}
		}, 100);
		mImageBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
