package com.wjika.cardstore.consumption.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.consumption.utils.LklPrintTools;
import com.wjika.cardstore.consumption.utils.QFPrintTools;
import com.wjika.cardstore.consumption.utils.WangPrintTools;
import com.wjika.cardstore.consumption.utils.ZBPrintTools;
import com.wjika.cardstore.login.utils.UserCenter;
import com.wjika.cardstore.main.ui.MainActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.utils.TimeUtil;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.Calendar;
import java.util.IdentityHashMap;

import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.execption.DeviceStatusException;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * Created by Liu_Zhichao on 2016/2/16 17:07.
 * 日期选择，统计打印
 */
public class DateSelectActivity extends ToolBarActivity implements View.OnClickListener{

	private static final int REQUEST_NET_STATISTICS = 100;

	@ViewInject(R.id.date_select_begin)
	private RelativeLayout dateSelectBegin;
	@ViewInject(R.id.date_select_end)
	private RelativeLayout dateSelectEnd;
	@ViewInject(R.id.date_select_stime)
	private TextView dateSelectStime;
	@ViewInject(R.id.date_select_etime)
	private TextView dateSelectEtime;
	@ViewInject(R.id.date_select_show)
	private TextView dateSelectShow;
	@ViewInject(R.id.date_select_statistics)
	private TextView dateSelectStatistics;

	private String sTime;
	private long startTime;
	private String eTime;
	private long endTime;
	private Calendar calendar;
	private Printer printer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_select_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.consumption_time_more));
		if (MainActivity.isWangPOS || MainActivity.isQFPOS || MainActivity.isLklPOS || MainActivity.isZBPOS) {
			dateSelectStatistics.setVisibility(View.VISIBLE);
		}

		try {
			printer = WeiposImpl.as().openPrinter();
		} catch (DeviceStatusException e) {
			e.printStackTrace();
		}

		calendar = Calendar.getInstance();
		startTime = calendar.getTimeInMillis();
		endTime = calendar.getTimeInMillis();
		sTime = TimeUtil.getCurrentTime(TimeUtil.SIMPLE_DATE_PATTERN);
		eTime = TimeUtil.getCurrentTime(TimeUtil.SIMPLE_DATE_PATTERN);
		dateSelectStime.setText(TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_ONE));
		dateSelectEtime.setText(TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_ONE));

		dateSelectBegin.setOnClickListener(this);
		dateSelectEnd.setOnClickListener(this);
		dateSelectShow.setOnClickListener(this);
		dateSelectStatistics.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.date_select_begin:
			{
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar sCalendar = Calendar.getInstance();
						sCalendar.set(year, monthOfYear, dayOfMonth);
						startTime = sCalendar.getTimeInMillis();
						sTime = TimeUtil.formatTime(startTime, TimeUtil.SIMPLE_DATE_PATTERN);
						dateSelectStime.setText(TimeUtil.formatTime(startTime, TimeUtil.TIME_FORMAT_ONE));
					}
				}, year, month, day);
				DatePicker date = datePickerDialog.getDatePicker();
				date.setMaxDate(calendar.getTimeInMillis());
				datePickerDialog.show();
				break;
			}
			case R.id.date_select_end:
			{
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar eCalendar = Calendar.getInstance();
						eCalendar.set(year, monthOfYear, dayOfMonth);
						endTime = eCalendar.getTimeInMillis();
						eTime = TimeUtil.formatTime(endTime, TimeUtil.SIMPLE_DATE_PATTERN);
						dateSelectEtime.setText(TimeUtil.formatTime(endTime, TimeUtil.TIME_FORMAT_ONE));
					}
				}, year, month, day);
				DatePicker date = datePickerDialog.getDatePicker();
				date.setMaxDate(calendar.getTimeInMillis());
				datePickerDialog.show();
				break;
			}
			case R.id.date_select_show:
				if (startTime > endTime) {
					ToastUtil.shortShow(this, getString(R.string.consumption_time_start_finish));
				} else {
					setResult(RESULT_OK, new Intent().putExtra("sTime", sTime).putExtra("eTime", eTime));
					finish();
				}
				break;
			case R.id.date_select_statistics:
				if (startTime > endTime) {
					ToastUtil.shortShow(this,  getString(R.string.consumption_time_start_finish));
				} else {
					showProgressDialog();
					IdentityHashMap<String, String> param = new IdentityHashMap<>();
					param.put("beginTime", sTime + ConsumptionListActivity.END_TIME);
					param.put("endTime", eTime + ConsumptionListActivity.OTHER_END_TIME);
					requestHttpData(Constants.Urls.URL_GET_STATISTICS_DATA, REQUEST_NET_STATISTICS, FProtocol.HttpMethod.POST, param);
				}
				break;
		}
	}

	@Override
	protected void parseData(int requestCode, String data) {
		if (REQUEST_NET_STATISTICS == requestCode) {
			StatisticsEntity statisticsEntity = Parsers.parseStatistics(data);
			if (statisticsEntity != null) {
				statisticsEntity.setStime(sTime);
				statisticsEntity.setEtime(eTime);
				if (MainActivity.isWangPOS && printer != null) {
					WangPrintTools.printStatistics(printer, statisticsEntity, UserCenter.getMerName(this));
				} else if (MainActivity.isQFPOS && MainActivity.printer != null) {
					QFPrintTools.printStatistics(this, MainActivity.printer, null, statisticsEntity);
				} else if(MainActivity.isLklPOS && MainActivity.printerDev != null) {
					LklPrintTools.printStatistics(this,MainActivity.printerDev, statisticsEntity);
				} else if (MainActivity.isZBPOS) {
					ZBPrintTools.printNormal(this, null, statisticsEntity);
				}
			}
		}
	}
}
