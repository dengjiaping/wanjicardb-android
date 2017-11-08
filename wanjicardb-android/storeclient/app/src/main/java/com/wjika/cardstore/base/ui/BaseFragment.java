package com.wjika.cardstore.base.ui;

import android.content.Intent;
import android.os.Bundle;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseFragment;
import com.common.utils.ToastUtil;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.network.entities.Entity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.ExitManager;

import java.util.IdentityHashMap;

public class BaseFragment extends FBaseFragment implements IActivityHelper {

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private String baseTitle = "";

	public String getTitle() {
		return baseTitle;
	}

	public void setTitle(int titleId) {
		baseTitle = getString(titleId);
	}

	public void setTitle(String title) {
		baseTitle = title;
	}

	@Override
	public void requestHttpData(String path, int requestCode) {
		path += ("&token=" + UserCenter.getToken(getActivity()));
		super.requestHttpData(path, requestCode);
	}

	@Override
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
		postParameters.put("token", UserCenter.getToken(getActivity()));
		super.requestHttpData(path, requestCode, method, postParameters);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity entity = Parsers.parseResult(data);
		if (BaseActivity.REQUEST_FAILED_CODE.equals(entity.getRspCode())) {
			UserCenter.clearLoginInfo(getActivity());
			startActivity(new Intent(getActivity(), LoginActivity.class));
			ToastUtil.shortShow(getActivity(), entity.getRspMsg());
			ExitManager.instance.exit();
		} else if (BaseActivity.REQUEST_SUCCESS_CODE.equals(entity.getRspCode())) {
			parseData(requestCode, data);
		} else {
			ToastUtil.shortShow(getActivity(), entity.getRspMsg());
		}
	}

	protected void parseData(int requestCode, String data) {
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(getActivity(), errorMessage);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("baseTitle", baseTitle);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			baseTitle = savedInstanceState.getString("baseTitle", getTitle());
		}
	}
}
