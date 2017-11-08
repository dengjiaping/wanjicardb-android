package com.wjika.cardstore.storemanage.ui;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.common.viewinject.annotation.ViewInject;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.utils.ViewInjectUtils;

import java.util.Calendar;

/**
 * Created by zhangzhaohui on 2016/1/13.
 * 修改营业时间
 */
public class StoreManageShopHoursActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.store_manage_open_shop)
	private LinearLayout storeManageOpenShop;
	@ViewInject(R.id.store_manage_open_time)
	private TextView storeManageOpenTime;
	@ViewInject(R.id.store_manage_close_shop)
	private LinearLayout storeManageCloseShop;
	@ViewInject(R.id.store_manage_close_time)
	private TextView storeManageCloseTime;
	@ViewInject(R.id.store_manage_save_time)
	private TextView storeManageSaveTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_manage_shop_hours_act);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_shop_fix_time));
		storeManageOpenShop.setOnClickListener(this);
		storeManageCloseShop.setOnClickListener(this);
		storeManageSaveTime.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_manage_open_shop:
				final Calendar openCalendar = Calendar.getInstance();
				final TimePickerDialog openTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						String am_pm = "";
						openCalendar.set(Calendar.HOUR, hourOfDay);
						openCalendar.set(Calendar.MINUTE, minute);
						if (openCalendar.get(Calendar.AM_PM) == Calendar.PM) {
							am_pm = getString(R.string.morning);
						} else if (openCalendar.get(Calendar.AM_PM) == Calendar.AM) {
							am_pm = getString(R.string.afternoon);
						}
						String strHrsToShow = String.format("%02d", (openCalendar.get(Calendar.HOUR) == -1) ? "12" : openCalendar.get(Calendar.HOUR));
						storeManageOpenTime.setText(am_pm + " " + strHrsToShow + ":" + String.format("%02d", openCalendar.get(Calendar.MINUTE)));
					}
				}, 8, 0, false);
				openTimePickerDialog.setMessage(getString(R.string.store_shop_select_time));
				openTimePickerDialog.setCancelable(true);
				openTimePickerDialog.show();
				break;
			case R.id.store_manage_close_shop:
				final Calendar closeCalendar = Calendar.getInstance();
				final TimePickerDialog closeTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

						String am_pm = "";
						closeCalendar.set(Calendar.HOUR, hourOfDay);
						closeCalendar.set(Calendar.MINUTE, minute);
						if (closeCalendar.get(Calendar.AM_PM) == Calendar.PM) {
							am_pm = getString(R.string.morning);
						} else if (closeCalendar.get(Calendar.AM_PM) == Calendar.AM) {
							am_pm = getString(R.string.afternoon);
						}
						String strHrsToShow = String.format("%02d", (closeCalendar.get(Calendar.HOUR) == -1) ? "12" : closeCalendar.get(Calendar.HOUR));
						storeManageCloseTime.setText(am_pm + " " + strHrsToShow + ":" + String.format("%02d", closeCalendar.get(Calendar.MINUTE)));
					}
				}, 8, 0, false);
				closeTimePickerDialog.setMessage(getString(R.string.store_shop_select_time));
				closeTimePickerDialog.setCancelable(true);
				closeTimePickerDialog.show();
				break;
			case R.id.store_manage_save_time:
				String openTime = storeManageOpenTime.getText().toString();
				String closeTime = storeManageCloseTime.getText().toString();
				Intent intent = new Intent();
				intent.putExtra(StoreManageDetailActivity.OPEN_TIME, openTime);
				intent.putExtra(StoreManageDetailActivity.CLOSE_TIME, closeTime);
				setResult(RESULT_OK, intent);
				finish();
				break;
		}
	}
}
