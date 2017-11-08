package com.wjika.cardstore.consumption.widget;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wjika.cardstore.R;
import com.wjika.cardstore.consumption.adapter.CheckSpinnerAdapter;
import com.wjika.cardstore.utils.CommonTools;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/2/16 10:19.
 * 选择下拉
 */
public class CheckSpinnerView {

	private Context mContext;
	private OnSpinnerItemClickListener onSpinnerItemClickListener;
	private PopupWindow.OnDismissListener onDismissListener;
	private PopupWindow popupWindow;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	private View spinnerView;
	private ListView consumptionListview;
	private CheckSpinnerAdapter adapter;

	public CheckSpinnerView(Context mContext, OnSpinnerItemClickListener onSpinnerItemClickListener, PopupWindow.OnDismissListener onDismissListener) {
		this.mContext = mContext;
		this.onSpinnerItemClickListener = onSpinnerItemClickListener;
		this.onDismissListener = onDismissListener;
		screenWidth = CommonTools.getScreenWidth(mContext);
		screenHeight = CommonTools.getScreenHeight(mContext) - CommonTools.dip2px(mContext, 88) - CommonTools.getStatusBarHeight(mContext);
	}

	/**
	 * 初始化 PopupWindow
	 * @param view
	 */
	private void initPopuWindow(View view) {
		/* 第一个参数弹出显示view 后两个是窗口大小 */
		popupWindow = new PopupWindow(view, screenWidth, screenHeight);
		/* 设置背景显示 */
		popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.check_spinner_bg));
		/* 设置触摸外面时消失 */
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.setTouchable(true);
		/* 设置点击menu以外其他地方以及返回键退出 */
		popupWindow.setFocusable(true);
		//1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
		view.setFocusableInTouchMode(true);
	}

	public void showSpinnerPop(View view, Animation animation, List<String> list, int position) {
		if (popupWindow == null){
			spinnerView = View.inflate(mContext, R.layout.consumption_spinner_view, null);
			initPopuWindow(spinnerView);
			consumptionListview = (ListView) spinnerView.findViewById(R.id.consumption_listview);
		}

		adapter = new CheckSpinnerAdapter(mContext, list, position);
		consumptionListview.setAdapter(adapter);

		popupWindow.setOnDismissListener(onDismissListener);
		view.setAnimation(animation);
		popupWindow.showAsDropDown(view, -4 , 2);
		consumptionListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (onSpinnerItemClickListener != null){
					onSpinnerItemClickListener.onItemClickListener1(parent, view, position, id);
				}
			}
		});
	}

	public void close() {
		popupWindow.dismiss();
	}

	public interface OnSpinnerItemClickListener{
		void onItemClickListener1(AdapterView<?> parent, View view, int position, long id);
	}
}
