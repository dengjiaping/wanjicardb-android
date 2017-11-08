package com.wjika.cardstore.consumption.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.seuic.android.PrinterListener;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.SearchBarActivity;
import com.wjika.cardstore.consumption.adapter.ConsumptionAdapter;
import com.wjika.cardstore.consumption.utils.LklPrintTools;
import com.wjika.cardstore.consumption.utils.QFPrintTools;
import com.wjika.cardstore.consumption.utils.WangPrintTools;
import com.wjika.cardstore.consumption.utils.ZBPrintTools;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.PageEntity;
import com.wjika.cardstore.network.parser.Parsers;

import java.util.IdentityHashMap;

import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.execption.DeviceStatusException;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * Created by Liu_Zhichao on 2016/2/17 15:18.
 * 消费搜索
 */
public class ConsumptionSearchActivity extends SearchBarActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>{

	private static final int REQUEST_NET_CONSUMPTION_LIST = 100;
	private static final int REQUEST_NET_CONSUMPTION_MORE = 200;
	private static final int REQUEST_NET_CONSUMPTION_REFUND = 300;

	private FootLoadingListView consumptionSearchList;
	private ConsumptionAdapter adapter;
	private String content;
	private ConsumptionEntity consumptionEntity;
	private Printer printer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumption_search_act);
		initView();
	}

	private void initView() {
		try {
			printer = WeiposImpl.as().openPrinter();
		} catch (DeviceStatusException e) {
			e.printStackTrace();
		}

		consumptionSearchList = (FootLoadingListView) findViewById(R.id.consumption_search_list);
		consumptionSearchList.setMode(PullToRefreshBase.Mode.DISABLED);
		consumptionSearchList.setOnRefreshListener(this);
		mTextSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.search_rigth_text:
				content = mEditSearch.getText().toString().trim();
				if (TextUtils.isEmpty(content) || content.length() < 3) {
					ToastUtil.shortShow(this, getString(R.string.search_info_least_three));
					return;
				}
				showProgressDialog();
				loadData(true);
				break;
			case R.id.consumption_item_refund:
				consumptionEntity = (ConsumptionEntity) v.getTag();
				if (consumptionEntity != null) {
					View dialogView = View.inflate(this, R.layout.custom_dialog_view, null);
					final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();
					TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
					title.setText(getString(R.string.consumption_is_sure_refunded));
					TextView cancel = (TextView) dialogView.findViewById(R.id.dialog_cancle);
					cancel.setText(getString(R.string.cancel));
					dialogView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
					TextView confirm = (TextView) dialogView.findViewById(R.id.dialog_confirm);
					confirm.setText(getString(R.string.button_ok));
					confirm.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							showProgressDialog();
							IdentityHashMap<String, String> param = new IdentityHashMap<>();
							param.put("orginalTranNo", consumptionEntity.getTransNo());
							requestHttpData(Constants.Urls.URL_POST_CONSUMPTION_REFUND, REQUEST_NET_CONSUMPTION_REFUND, FProtocol.HttpMethod.POST, param);
						}
					});
					alertDialog.show();
				}
				break;
			case R.id.consumption_item_print:
				consumptionEntity = (ConsumptionEntity) v.getTag();
				if (consumptionEntity != null) {
					if (MainActivity.isWangPOS && printer != null) {
						WangPrintTools.printNormal(printer, consumptionEntity, this);
					} else if (MainActivity.isQFPOS && MainActivity.printer != null) {
						QFPrintTools.printNormal(this, MainActivity.printer, new PrinterListener.Stub() {
							@Override
							public void OnPrintSuccess() throws RemoteException {
								QFPrintTools.printBarCode(ConsumptionSearchActivity.this, MainActivity.printer, consumptionEntity.getTransNo());
							}

							@Override
							public void OnPrintFail(int returnCode) throws RemoteException {
							}
						}, consumptionEntity);
					} else if (MainActivity.isLklPOS && MainActivity.printerDev != null) {
						LklPrintTools.printNormal(this, MainActivity.printerDev, consumptionEntity);
					} else if (MainActivity.isZBPOS) {
						ZBPrintTools.printNormal(this, consumptionEntity, null);
					}
				}
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		consumptionSearchList.onRefreshComplete();
		super.success(requestCode, data);
	}

	private void loadData(boolean isRefresh) {
		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0); //隐藏
		if (isRefresh){
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("consumerRecordStatus", "100");
			param.put("branchId", "");
			param.put("beginDate", "");
			param.put("endDate", "");
			param.put("searchKey", content);
			param.put("pageNum", "1");
			param.put("pageSize", "10");
			requestHttpData(Constants.Urls.URL_GET_CONSUMPTION_LIST, REQUEST_NET_CONSUMPTION_LIST, FProtocol.HttpMethod.POST, param);
		} else {
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("consumerRecordStatus", "100");
			param.put("branchId", "");
			param.put("beginDate", "");
			param.put("endDate", "");
			param.put("searchKey", content);
			param.put("pageNum", String.valueOf(adapter.getPage() + 1));
			param.put("pageSize", "10");
			requestHttpData(Constants.Urls.URL_GET_CONSUMPTION_LIST, REQUEST_NET_CONSUMPTION_MORE, FProtocol.HttpMethod.POST, param);
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_CONSUMPTION_LIST == requestCode) {
			PageEntity<ConsumptionEntity> pageEntity = Parsers.parseConsumption(data);
			if (pageEntity != null ) {

					if (pageEntity.getResult()!= null) {
						adapter = new ConsumptionAdapter(this, pageEntity.getResult(), this);
						consumptionSearchList.setAdapter(adapter);
						if (pageEntity.getTotalPage() > 1){
							consumptionSearchList.setCanAddMore(true);
						}else {
							consumptionSearchList.setCanAddMore(false);
						}
						if (0 == pageEntity.getTotalPage()) {
							ToastUtil.shortShow(this, getString(R.string.search_no_order));
						}
					}
			}
		} else if (REQUEST_NET_CONSUMPTION_MORE == requestCode) {
			PageEntity<ConsumptionEntity> pageEntity = Parsers.parseConsumption(data);
			if (pageEntity != null && pageEntity.getResult() != null && pageEntity.getResult().size() > 0){
				adapter.addDatas(pageEntity.getResult());
				if (adapter.getPage() < pageEntity.getTotalPage()){
					consumptionSearchList.setCanAddMore(true);
				} else {
					consumptionSearchList.setCanAddMore(false);
				}
			}
		} else if (REQUEST_NET_CONSUMPTION_REFUND == requestCode) {
			ToastUtil.shortShow(this,getString(R.string.consumption_refunded_succeed));
			consumptionEntity.setTranStatus(3);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		consumptionSearchList.onRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(false);
	}
}
