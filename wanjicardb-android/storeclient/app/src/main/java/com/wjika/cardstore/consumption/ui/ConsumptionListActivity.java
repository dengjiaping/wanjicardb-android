package com.wjika.cardstore.consumption.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.seuic.android.PrinterListener;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.consumption.adapter.ConsumptionAdapter;
import com.wjika.cardstore.consumption.utils.LklPrintTools;
import com.wjika.cardstore.consumption.utils.QFPrintTools;
import com.wjika.cardstore.consumption.utils.WangPrintTools;
import com.wjika.cardstore.consumption.utils.ZBPrintTools;
import com.wjika.cardstore.consumption.widget.CheckSpinnerView;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.BranchEntity;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.PageEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.StringUtil;
import com.wjika.cardstore.utils.TimeUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.execption.DeviceStatusException;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * Created by Liu_Zhichao on 2016/1/24 22:24.
 * 消费管理，消费列表
 */
public class ConsumptionListActivity extends ToolBarActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>{

	private static final int REQUEST_ACT_SELECT_DATE = 10;
	private static final int REQUEST_NET_CONSUMPTION_LIST = 100;
	private static final int REQUEST_NET_CONSUMPTION_MORE = 200;
	private static final int REQUEST_NET_BRANCH_LIST = 300;
	private static final int REQUEST_NET_CONSUMPTION_REFUND = 400;
	public static final String END_TIME = "000000";
	public static final String OTHER_END_TIME = "235959";

	@ViewInject(R.id.consumption_subbranch)
	private LinearLayout consumptionSubbranch;
	@ViewInject(R.id.consumption_subbranch_tv)
	private TextView consumptionSubbranchTV;
	@ViewInject(R.id.consumption_date)
	private LinearLayout consumptionDate;
	@ViewInject(R.id.consumption_date_tv)
	private TextView consumptionDateTV;
	@ViewInject(R.id.consumption_status)
	private LinearLayout consumptionStatus;
	@ViewInject(R.id.consumption_status_tv)
	private TextView consumptionStatusTV;
	@ViewInject(R.id.consumption_list)
	private FootLoadingListView consumptionList;

	private ConsumptionAdapter adapter;
	private CheckSpinnerView branchCheckSpinner;
	private CheckSpinnerView dateCheckSpinner;
	private CheckSpinnerView statusCheckSpinner;
	private List<String> subbranchList = new ArrayList<>();
	private List<String> dateList = new ArrayList<>();
	private List<String> statusList = new ArrayList<>();
	private int subbranchPosition;
	private int datePosition;
	private int statusPosition;
	private String branchId = "";
	private int status = 100;
	private String mBeginDate = "";
	private String mEndDate = "";
	private List<BranchEntity> branchs;
	private ConsumptionEntity consumptionEntity;
	private Printer printer;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumption_list_act);
		ViewInjectUtils.inject(this);
		initView();
		initEndSpinner();
		loadBranchData();
		loadData(true);
	}

	private void loadBranchData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_SUBBRANCH_LIST, REQUEST_NET_BRANCH_LIST, FProtocol.HttpMethod.POST, new IdentityHashMap<String, String>());
	}

	private void initView() {
		setLeftTitle((String) getText(R.string.home_frag_consume_manager));
		mBtnTitleRight.setImageResource(R.drawable.search);
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setOnClickListener(this);

		try {
			printer = WeiposImpl.as().openPrinter();
		} catch (DeviceStatusException e) {
			e.printStackTrace();
		}

		consumptionList.setMode(PullToRefreshBase.Mode.BOTH);
		consumptionList.setOnRefreshListener(this);

		if (!UserCenter.isMain(this)) {
			consumptionSubbranchTV.setText(UserCenter.getMerName(this));
		}
		consumptionDate.setOnClickListener(this);
		consumptionStatus.setOnClickListener(this);
	}

	/**
	 * 初始化时间和状态下拉控件
	 */
	private void initEndSpinner() {
		dateList.add(getString(R.string.consumption_all_times));
		dateList.add(getString(R.string.consumption_time_today));
		dateList.add(getString(R.string.consumption_time_three_today));
		dateList.add(getString(R.string.consumption_time_week));
		dateList.add(getString(R.string.consumption_time_more));
		dateCheckSpinner = new CheckSpinnerView(this, new CheckSpinnerView.OnSpinnerItemClickListener() {
			@Override
			public void onItemClickListener1(AdapterView<?> parent, View view, int position, long id) {
				consumptionDateTV.setText(dateList.get(position));
				long cTime = new Date().getTime();
				if (1 == position) {//今天
					datePosition = position;
					mBeginDate = TimeUtil.formatTime(cTime, TimeUtil.SIMPLE_DATE_PATTERN) + END_TIME;
					mEndDate = TimeUtil.formatTime(cTime, TimeUtil.TIME_FORMAT_20);
					loadData(true);
				} else if (2 == position) {//近三天
					datePosition = position;
					mBeginDate = TimeUtil.formatTime(TimeUtil.addDays(-3).getTime(), TimeUtil.SIMPLE_DATE_PATTERN) + END_TIME;
					mEndDate = TimeUtil.formatTime(cTime, TimeUtil.TIME_FORMAT_20);
					loadData(true);
				} else if (3 == position) {//近一周
					datePosition = position;
					mBeginDate = TimeUtil.formatTime(TimeUtil.addDays(-7).getTime(), TimeUtil.SIMPLE_DATE_PATTERN) + END_TIME;
					mEndDate = TimeUtil.formatTime(cTime, TimeUtil.TIME_FORMAT_20);
					loadData(true);
				} else if (4 == position) {//更多时间
					startActivityForResult(new Intent(ConsumptionListActivity.this, DateSelectActivity.class), REQUEST_ACT_SELECT_DATE);
				} else {//全部时间
					datePosition = position;
					mBeginDate = "";
					mEndDate = "";
					loadData(true);
				}
				dateCheckSpinner.close();
			}
		}, new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				consumptionDate.setSelected(false);
			}
		});

		statusList.add(getString(R.string.consumption_all_status));
		statusList.add(getString(R.string.consumption_consume_succeed));
		statusList.add(getString(R.string.consumption_refunded));
		statusList.add(getString(R.string.consumption_reversal_close));
		statusCheckSpinner = new CheckSpinnerView(this, new CheckSpinnerView.OnSpinnerItemClickListener() {
			@Override
			public void onItemClickListener1(AdapterView<?> parent, View view, int position, long id) {
				statusPosition = position;
				consumptionStatusTV.setText(statusList.get(position));
				if (1 == position) {
					status = 1;
				} else if (2 == position) {
					status = 3;
				} else if (3 == position) {
					status = 4;
				} else {
					status = 100;
				}
				statusCheckSpinner.close();
				loadData(true);
			}
		}, new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				consumptionStatus.setSelected(false);
			}
		});
	}

	/**
	 * 初始化分店的下拉控件
	 */
	private void initSpinner() {
		if (UserCenter.isMain(this)) {
			subbranchList.add(getString(R.string.consumption_all_stores));
			if (branchs != null && branchs.size() > 0){
				for (BranchEntity branchEntity : branchs) {
					subbranchList.add(branchEntity.getMerchantName());
				}
			}
		} else {
			subbranchList.add(UserCenter.getMerName(this));
		}
		branchCheckSpinner = new CheckSpinnerView(this, new CheckSpinnerView.OnSpinnerItemClickListener() {
			@Override
			public void onItemClickListener1(AdapterView<?> parent, View view, int position, long id) {
				subbranchPosition = position;
				consumptionSubbranchTV.setText(subbranchList.get(position));
				if (UserCenter.isMain(ConsumptionListActivity.this) && 0 != position) {
					branchId = branchs.get(position - 1).getId();
				} else {
					branchId = "";
				}
				branchCheckSpinner.close();
				showProgressDialog();
				loadData(true);
			}
		}, new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				consumptionSubbranch.setSelected(false);
			}
		});
		consumptionSubbranch.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && REQUEST_ACT_SELECT_DATE == requestCode) {
			String sTime = data.getStringExtra("sTime");
			String eTime = data.getStringExtra("eTime");
			if (!TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
				datePosition = 4;
				mBeginDate = sTime + END_TIME;
				mEndDate = eTime + OTHER_END_TIME;
				loadData(true);
			}
		}
	}

	private void loadData(boolean isRefresh) {
		//1支付成功, 0等待支付,3已退款, 4 已冲正 100 全部
		//日期传空串表示全部，分店id也是100表示全部
		if (isRefresh){
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("consumerRecordStatus", String.valueOf(status));
			param.put("branchId", branchId);
			param.put("beginDate", mBeginDate);
			param.put("endDate", mEndDate);
			param.put("searchKey", "");
			param.put("pageNum", "1");
			param.put("pageSize", "10");
			requestHttpData(Constants.Urls.URL_GET_CONSUMPTION_LIST, REQUEST_NET_CONSUMPTION_LIST, FProtocol.HttpMethod.POST, param);
		} else {
			int page = 0;
			if (adapter != null) {
				page = adapter.getPage();
			}
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("consumerRecordStatus", String.valueOf(status));
			param.put("branchId", branchId);
			param.put("beginDate", mBeginDate);
			param.put("endDate", mEndDate);
			param.put("searchKey", "");
			param.put("pageNum", String.valueOf(page + 1));
			param.put("pageSize", "10");
			requestHttpData(Constants.Urls.URL_GET_CONSUMPTION_LIST, REQUEST_NET_CONSUMPTION_MORE, FProtocol.HttpMethod.POST, param);
		}
	}

	@Override
	public void success(int requestCode, String data) {
		consumptionList.onRefreshComplete();
		super.success(requestCode, data);
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_CONSUMPTION_LIST == requestCode){
			PageEntity<ConsumptionEntity> pageEntity = Parsers.parseConsumption(data);
			if (pageEntity != null && pageEntity.getResult() != null){
				consumptionList.setVisibility(View.VISIBLE);
				adapter = new ConsumptionAdapter(this, pageEntity.getResult(), this);
				consumptionList.setAdapter(adapter);
				if (pageEntity.getTotalPage() > 0) {
					consumptionList.setCanAddMore(true);
				} else {
					consumptionList.setCanAddMore(false);
					ToastUtil.shortShow(this, getString(R.string.consumption_no_order));
				}
			}
		} else if (REQUEST_NET_CONSUMPTION_MORE == requestCode){
			PageEntity<ConsumptionEntity> pageEntity = Parsers.parseConsumption(data);
			if (pageEntity != null && pageEntity.getResult() != null && pageEntity.getResult().size() > 0){
				adapter.addDatas(pageEntity.getResult());
				if (adapter.getPage() < pageEntity.getTotalPage()){
					consumptionList.setCanAddMore(true);
				} else {
					consumptionList.setCanAddMore(false);
				}
			}
		} else if (REQUEST_NET_BRANCH_LIST == requestCode) {
			branchs = Parsers.parseBranch(data);
			initSpinner();
		} else if (REQUEST_NET_CONSUMPTION_REFUND == requestCode) {
			ToastUtil.shortShow(this, getString(R.string.consumption_refunded_succeed));
			consumptionEntity.setTranStatus(3);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		consumptionList.onRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.right_button:
				startActivity(new Intent(this, ConsumptionSearchActivity.class));
				break;
			case R.id.consumption_subbranch:
				if (consumptionSubbranch.isSelected()) {
					consumptionSubbranch.setSelected(false);
				} else {
					consumptionSubbranch.setSelected(true);
				}
				if (subbranchList != null && branchCheckSpinner != null){
					branchCheckSpinner.showSpinnerPop(consumptionSubbranch, null, subbranchList, subbranchPosition);
				}
				break;
			case R.id.consumption_date:
				if (consumptionDate.isSelected()) {
					consumptionDate.setSelected(false);
				} else {
					consumptionDate.setSelected(true);
				}
				if (dateList != null && dateCheckSpinner != null){
					dateCheckSpinner.showSpinnerPop(consumptionDate, null, dateList, datePosition);
				}
				break;
			case R.id.consumption_status:
				if (consumptionStatus.isSelected()) {
					consumptionStatus.setSelected(false);
				} else {
					consumptionStatus.setSelected(true);
				}
				if (statusList != null && statusCheckSpinner != null){
					statusCheckSpinner.showSpinnerPop(consumptionStatus, null, statusList, statusPosition);
				}
				break;
			case R.id.consumption_item_refund:
				consumptionEntity = (ConsumptionEntity) v.getTag();
				if (consumptionEntity != null && !StringUtil.isEmpty(consumptionEntity.getTransNo())) {
					View dialogView = View.inflate(this, R.layout.custom_dialog_view, null);
					alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();
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
				} else {
					ToastUtil.shortShow(this, "退款失败");
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
								QFPrintTools.printBarCode(ConsumptionListActivity.this, MainActivity.printer, consumptionEntity.getTransNo());
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
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		loadData(true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		loadData(false);
	}
}
