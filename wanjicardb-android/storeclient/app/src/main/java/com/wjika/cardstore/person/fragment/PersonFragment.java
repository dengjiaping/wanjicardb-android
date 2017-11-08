package com.wjika.cardstore.person.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseActivity;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.base.ui.WebViewActivity;
import com.wjika.cardstore.login.ui.LoginActivity;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.UpdateVersionEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.person.ui.AboutUsActivity;
import com.wjika.cardstore.person.ui.FeedbackActivity;
import com.wjika.cardstore.person.ui.PersonPWDActivity;
import com.wjika.cardstore.update.ui.UpdateActivity;
import com.wjika.cardstore.utils.CommonTools;
import com.wjika.cardstore.utils.ExitManager;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/7 01:08.
 * 个人中心
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{

	private static final int REQUEST_NET_LOGOUT = 100;

	private AlertDialog alertDialog;
	private View personContactService;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return initView(inflater);
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.person_frag, null);

		view.findViewById(R.id.left_button).setVisibility(View.INVISIBLE);
		TextView leftText = (TextView) view.findViewById(R.id.left_text);
		leftText.setText(getString(R.string.person_person_center));
		leftText.setVisibility(View.VISIBLE);

		view.findViewById(R.id.person_alter_pwd).setOnClickListener(this);
		view.findViewById(R.id.person_update).setOnClickListener(this);
		personContactService = view.findViewById(R.id.person_contact_service);
		if (MainActivity.isWangPOS) {
			personContactService.setVisibility(View.GONE);
		} else {
			personContactService.setOnClickListener(this);
		}
		view.findViewById(R.id.person_feedback).setOnClickListener(this);
		view.findViewById(R.id.person_version_desc).setOnClickListener(this);
		view.findViewById(R.id.person_about_us).setOnClickListener(this);
		view.findViewById(R.id.person_logout).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.person_alter_pwd://修改找回密码
				if (UserCenter.isMain(getActivity())){
					startActivity(new Intent(getActivity(), PersonPWDActivity.class));
				}else {
					ToastUtil.shortShow(getActivity(), getString(R.string.order_call_headoffice));
				}
				break;
			case R.id.person_update://检查更新
				checkUpdateVersion();
				break;
			case R.id.person_contact_service://联系客服
				personContactService.setClickable(false);
				View dialogView = View.inflate(getActivity(), R.layout.custom_dialog_view, null);
				TextView cancel= (TextView) dialogView.findViewById(R.id.dialog_cancle);
				TextView ensure= (TextView) dialogView.findViewById(R.id.dialog_confirm);
				TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
				cancel.setText(getString(R.string.cancel));
				ensure.setText(getString(R.string.call));
				title.setText(getString(R.string.server_phone));
				alertDialog = new AlertDialog.Builder(getActivity()).setView(dialogView).create();
				dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				dialogView.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008722002")));
						alertDialog.dismiss();
					}
				});
				alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						personContactService.setClickable(true);
					}
				});
				alertDialog.show();
				break;
			case R.id.person_feedback://意见反馈
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				break;
			case R.id.person_version_desc://使用帮助
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra(WebViewActivity.EXTRA_URL, getString(R.string.help_url))
						.putExtra(WebViewActivity.EXTRA_TITLE, getString(R.string.person_user_help)));
				break;
			case R.id.person_about_us://关于我们
				startActivity(new Intent(getActivity(), AboutUsActivity.class));
				break;
			case R.id.person_logout://退出登录
				dialogView = View.inflate(getActivity(), R.layout.custom_dialog_view, null);
				cancel= (TextView) dialogView.findViewById(R.id.dialog_cancle);
				ensure= (TextView) dialogView.findViewById(R.id.dialog_confirm);
				title = (TextView) dialogView.findViewById(R.id.dialog_title);
				cancel.setText(getString(R.string.cancel));
				ensure.setText(getString(R.string.button_ok));
				title.setText(getString(R.string.person_is_quit_login));
				alertDialog = new AlertDialog.Builder(getActivity()).setView(dialogView).create();
				dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				dialogView.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						showProgressDialog();
						requestHttpData(Constants.Urls.URL_POST_USER_LOGOUT, REQUEST_NET_LOGOUT, FProtocol.HttpMethod.POST, new IdentityHashMap<String, String>());
						alertDialog.dismiss();
					}
				});
				alertDialog.show();
				break;
		}
	}

	private void checkUpdateVersion() {
		showProgressDialog();
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("version", String.valueOf(CommonTools.getVersion(getActivity())));
		requestHttpData(Constants.Urls.URL_GET_UPDATE_VERSION, BaseActivity.REQUEST_UPDATE_VERSION_CODE, FProtocol.HttpMethod.POST, param);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		if (REQUEST_NET_LOGOUT == requestCode) {
			UserCenter.clearLoginInfo(getActivity());
			startActivity(new Intent(getActivity(), LoginActivity.class));
			ExitManager.instance.exit();
		} else if (BaseActivity.REQUEST_UPDATE_VERSION_CODE == requestCode){
			//版本更新
			UpdateVersionEntity updateVersionEntity = Parsers.parseUpdateVersion(data);
			if (updateVersionEntity != null) {
				if (BaseActivity.REQUEST_SUCCESS_CODE.equals(updateVersionEntity.getRspCode())) {
					if (0 == updateVersionEntity.getUpdateType()) {
						ToastUtil.shortShow(getActivity(),getString(R.string.person_is_newst_verson));
					} else {
						String url = updateVersionEntity.getUrl() == null ? "" : updateVersionEntity.getUrl();
						String version = updateVersionEntity.getVersion() == null ? "" : updateVersionEntity.getVersion();
						startActivity(new Intent().setClass(getActivity(), UpdateActivity.class)
								.putExtra(UpdateActivity.KEY_UPDATE_TYPE, updateVersionEntity.getUpdateType())
								.putExtra(UpdateActivity.KEY_UPDATE_URL, url)
								.putExtra(UpdateActivity.KEY_UPDATE_VERSION_NAME, version)
								.putExtra(UpdateActivity.KEY_UPDATE_VERSION_DESC, updateVersionEntity.getDescribe())
								.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				} else {
					ToastUtil.shortShow(getActivity(), updateVersionEntity.getRspMsg());
				}
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		if (REQUEST_NET_LOGOUT == requestCode) {
			UserCenter.clearLoginInfo(getActivity());
			startActivity(new Intent(getActivity(), LoginActivity.class));
			ExitManager.instance.exit();
		}
		ToastUtil.shortShow(getActivity(), errorMessage);
	}
}
