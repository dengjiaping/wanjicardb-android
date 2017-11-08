package com.wjika.cardstore.base.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseActivity;
import com.common.utils.LogUtil;
import com.common.utils.ToastUtil;
import com.common.widget.ProgressImageView;
import com.umeng.analytics.MobclickAgent;
import com.wjika.cardstore.R;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.Entity;
import com.wjika.cardstore.network.entities.UpdateVersionEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.update.ui.UpdateActivity;
import com.wjika.cardstore.utils.CommonTools;
import com.wjika.cardstore.utils.ConfigUtil;
import com.wjika.cardstore.utils.ExitManager;

import java.util.Date;
import java.util.IdentityHashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * @author songxudong
 */
public class BaseActivity extends FBaseActivity implements IActivityHelper {

	private static final String BASETAG = FBaseActivity.class.getSimpleName();
	public static final String REQUEST_SUCCESS_CODE = "000";//全部网络请求，逻辑处理成功时返回的code
	public static final String REQUEST_FAILED_CODE = "50009003";//token过期
	public static final int REQUEST_UPDATE_VERSION_CODE = -3;
	public static final long IGNORE_UPDATE_DAYS = 7;

	protected Resources res;
	protected View mLayoutLoading;
	protected ProgressImageView mImgLoading;
	protected TextView mTxtLoadingEmpty;
	protected TextView mTxtLoadingRetry;
	protected ImageView mImgLoadingRetry;
	protected ImageView mImgLoadingEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitManager.instance.addActivity(this);
		res = getResources();
	}

	@Override
	protected void onResume() {
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onResume() invoked!!");
		super.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}

	@Override
	public void requestHttpData(String path, int requestCode) {
		path += ("&token=" + UserCenter.getToken(this));
		super.requestHttpData(path, requestCode);
	}

	@Override
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
		postParameters.put("token", UserCenter.getToken(this));
		super.requestHttpData(path, requestCode, method, postParameters);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity entity = Parsers.parseResult(data);
		if (REQUEST_FAILED_CODE.equals(entity.getRspCode())) {
			UserCenter.clearLoginInfo(this);
			startActivity(new Intent(this, LoginActivity.class));
			ToastUtil.shortShow(this,entity.getRspMsg());
			ExitManager.instance.exit();
		} else if (REQUEST_SUCCESS_CODE.equals(entity.getRspCode())){
			//数据请求成功
			if (REQUEST_UPDATE_VERSION_CODE == requestCode){
				//版本更新
				UpdateVersionEntity updateVersionEntity = Parsers.parseUpdateVersion(data);
				if (updateVersionEntity != null && updateVersionEntity.getUpdateType() != 0){
					long ignoreDate = ConfigUtil.getIgnoreDate(this);
					if (ignoreDate > 0){
						long cTime = new Date().getTime();
						long dTime = cTime - ignoreDate;
						long days = dTime / 1000 / 60 / 60 / 24;
						if (days >= 0 && days < BaseActivity.IGNORE_UPDATE_DAYS){
							return;
						} else {
							ConfigUtil.setIgnoreDate(this, 0);
						}
					}
					String url = updateVersionEntity.getUrl() == null ? "" : updateVersionEntity.getUrl();
					String version = updateVersionEntity.getVersion() == null ? "" : updateVersionEntity.getVersion();
					startActivity(new Intent().setClass(this, UpdateActivity.class)
							.putExtra(UpdateActivity.KEY_UPDATE_TYPE, updateVersionEntity.getUpdateType())
							.putExtra(UpdateActivity.KEY_UPDATE_URL, url)
							.putExtra(UpdateActivity.KEY_UPDATE_VERSION_NAME, version)
							.putExtra(UpdateActivity.KEY_UPDATE_VERSION_DESC, updateVersionEntity.getDescribe())
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
			} else {
				parseData(requestCode, data);
			}
		}else {
			ToastUtil.shortShow(this, entity.getRspMsg());
		}
	}

	/**
	 * 请求成功后实际处理数据的方法
	 * @param requestCode
	 * @param data
	 */
	protected void parseData(int requestCode, String data) {
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	protected void checkUpdateVersion(){
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("version", String.valueOf(CommonTools.getVersion(this)));
		requestHttpData(Constants.Urls.URL_GET_UPDATE_VERSION, REQUEST_UPDATE_VERSION_CODE, FProtocol.HttpMethod.POST, param);
	}

	protected void initLoadingView(View.OnClickListener listener){
		mLayoutLoading = findViewById(R.id.loading_layout);
		mImgLoading = (ProgressImageView)findViewById(R.id.loading_img_anim);
		mTxtLoadingEmpty = (TextView)findViewById(R.id.loading_txt_empty);
		mTxtLoadingRetry = (TextView)findViewById(R.id.loading_txt_retry);
		mImgLoadingRetry = (ImageView)findViewById(R.id.loading_img_refresh);
		mImgLoadingEmpty = (ImageView)findViewById(R.id.loading_img_empty);
		mLayoutLoading.setOnClickListener(listener);
		mLayoutLoading.setClickable(false);
	}

	protected void setLoadingStatus(LoadingStatus status){
		if (mLayoutLoading == null || mImgLoading == null || mImgLoadingEmpty == null
				|| mImgLoadingRetry == null || mTxtLoadingEmpty == null || mTxtLoadingRetry == null){
			return;
		}
		switch (status){
			case LOADING:
			{
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.VISIBLE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				break;
			}
			case EMPTY:
			{
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoadingEmpty.setVisibility(View.VISIBLE);
				mTxtLoadingEmpty.setVisibility(View.VISIBLE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				break;
			}
			case RETRY:
			{
				mLayoutLoading.setClickable(true);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.VISIBLE);
				mTxtLoadingRetry.setVisibility(View.VISIBLE);
				break;
			}
			case GONE:
			{
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.GONE);
				mImgLoading.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				break;
			}
		}
	}

	public static enum LoadingStatus{
		INIT,
		LOADING,
		EMPTY,
		RETRY,
		GONE
	}
}
