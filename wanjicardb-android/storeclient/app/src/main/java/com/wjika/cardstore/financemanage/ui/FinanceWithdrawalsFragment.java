package com.wjika.cardstore.financemanage.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.financemanage.adapter.FinanceManageAdapter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.FinancePageEntity;
import com.wjika.cardstore.network.parser.Parsers;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/12.
 * 财务管理的pager界面
 */
public class FinanceWithdrawalsFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

	private LinearLayout financeManageWithdrawDepositMoney;
	private FootLoadingListView footLoadingListView;
	private RadioGroup financeManageCategory;
	private ViewPager financeManageWithdrawals;
	private ListView refreshableView;

	private Context context;
	private FinanceManageAdapter financeManageAdapter;
	private String status;
	private int height;
	private int viewpagerHeight;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					break;
			}
		}
	};


	public void setArgs(Context context, String status, LinearLayout financeManageWithdrawDepositMoney, RadioGroup financeManageCategory, ViewPager financeManageWithdrawals) {
		this.status = status;
		this.context = context;
		this.financeManageWithdrawDepositMoney = financeManageWithdrawDepositMoney;
		this.financeManageCategory = financeManageCategory;
		this.financeManageWithdrawals = financeManageWithdrawals;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.finance_manage_withdraw_frag, null);
		footLoadingListView = (FootLoadingListView) view.findViewById(R.id.finance_list);
		refreshableView = footLoadingListView.getRefreshableView();
		refreshableView.setOnTouchListener(new View.OnTouchListener() {
			int startY;
			int endY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				height = financeManageWithdrawDepositMoney.getHeight();
				viewpagerHeight = financeManageWithdrawals.getHeight();
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startY = (int) event.getY();
						if (FinanceManageActivity.scrollFlagFirst) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									financeManageWithdrawals.measure(financeManageWithdrawals.getWidth(), height + viewpagerHeight);
									LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) financeManageWithdrawals.getLayoutParams();
									layoutParams.height = height + viewpagerHeight;
									financeManageWithdrawals.setLayoutParams(layoutParams);
									FinanceManageActivity.scrollFlagFirst = false;
								}
							});
						}
						break;
					case MotionEvent.ACTION_MOVE:
						endY = (int) event.getY();
						if (endY - startY < -105 && FinanceManageActivity.scrollFlag) {
							ObjectAnimator animatorHead = ObjectAnimator.ofFloat(financeManageWithdrawDepositMoney, "translationY", 0, -height);
							ObjectAnimator animatorHeadPager = ObjectAnimator.ofFloat(financeManageCategory, "translationY", 0, -height);
							ObjectAnimator animatorPager = ObjectAnimator.ofFloat(financeManageWithdrawals, "translationY", 0, -height);
							AnimatorSet animatorSet = new AnimatorSet();
							animatorSet.playTogether(animatorHead, animatorHeadPager, animatorPager);
							animatorSet.setDuration(500);
							animatorSet.start();
							animatorSet.addListener(new Animator.AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animation) {
								}

								@Override
								public void onAnimationEnd(Animator animation) {
									FinanceManageActivity.scrollFlag = false;
								}

								@Override
								public void onAnimationCancel(Animator animation) {
								}

								@Override
								public void onAnimationRepeat(Animator animation) {
								}
							});
						}
						break;
					case MotionEvent.ACTION_UP:
						break;
				}
				return false;
			}
		});
		footLoadingListView.setOnRefreshListener(this);
		return view;
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			showProgressDialog();
			initData(false);
		}
	}

	private void initData(boolean isMore) {
		if (isMore) {
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("category", status);
			param.put("pageNum", String.valueOf(financeManageAdapter.getPage() + 1));
			param.put("pageSize", String.valueOf(20));
			requestHttpData(Constants.Urls.URL_GET_MERCHANT_FINANCE, FinanceManageActivity.REQUEST_CODE_FINANCE_MORE, FProtocol.HttpMethod.POST, param);
		} else {
			IdentityHashMap<String, String> param = new IdentityHashMap<>();
			param.put("category", status);
			param.put("pageNum", String.valueOf(1));
			param.put("pageSize", String.valueOf(20));
			requestHttpData(Constants.Urls.URL_GET_MERCHANT_FINANCE, FinanceManageActivity.REQUEST_CODE_FINANCE, FProtocol.HttpMethod.POST, param);
		}
	}

	@Override
	public void success(int requestCode, String data) {
		footLoadingListView.onRefreshComplete();
		super.success(requestCode, data);
	}

	@Override
	protected void parseData(int requestCode, String data) {
		switch (requestCode) {
			case FinanceManageActivity.REQUEST_CODE_FINANCE:
				footLoadingListView.onRefreshComplete();
				FinancePageEntity financePageEntity = Parsers.parseFinanceList(data);
				if (financePageEntity != null && financePageEntity.getFinancePageEntities() != null) {
					List<FinancePageEntity> financePageEntities = financePageEntity.getFinancePageEntities();
					footLoadingListView.setVisibility(View.VISIBLE);
					financeManageAdapter = new FinanceManageAdapter(context, financePageEntities);
					footLoadingListView.setAdapter(financeManageAdapter);
					if (financePageEntity.getTotalPage() > 1) {
						footLoadingListView.setCanAddMore(true);
					} else {
						footLoadingListView.setCanAddMore(false);
					}
					if (0 == financePageEntity.getTotalPage()) {
						if (FinanceManageActivity.ALL.equals(status)) {
							ToastUtil.shortShow(getActivity(), getString(R.string.finance_manager_no_recorder));
						} else if (FinanceManageActivity.INCOME.equals(status)) {
							ToastUtil.shortShow(getActivity(), getString(R.string.finance_manager_no_income));
						} else if (FinanceManageActivity.WITHDRAW.equals(status)) {
							ToastUtil.shortShow(getActivity(), getString(R.string.finance_manager_no_withdraw));
						}
					}
				}
				break;
			case FinanceManageActivity.REQUEST_CODE_FINANCE_MORE:
				footLoadingListView.onRefreshComplete();
				FinancePageEntity parseFinanceList = Parsers.parseFinanceList(data);
				if (parseFinanceList != null && parseFinanceList.getFinancePageEntities() != null && parseFinanceList.getFinancePageEntities().size() > 0) {
					financeManageAdapter.addDatas(parseFinanceList.getFinancePageEntities());
					if (parseFinanceList.getTotalPage() < parseFinanceList.getTotalPage()) {
						footLoadingListView.setCanAddMore(true);
					} else {
						footLoadingListView.setCanAddMore(false);
					}
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		footLoadingListView.onRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		initData(false);
		if (!FinanceManageActivity.scrollFlag && refreshableView.getFirstVisiblePosition() == 0) {
			ObjectAnimator animatorHead = ObjectAnimator.ofFloat(financeManageWithdrawDepositMoney, "translationY", -height, 0);
			ObjectAnimator animatorHeadPager = ObjectAnimator.ofFloat(financeManageCategory, "translationY", -height, 0);
			ObjectAnimator animatorPager = ObjectAnimator.ofFloat(financeManageWithdrawals, "translationY", -height, 0);
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(animatorHead, animatorHeadPager, animatorPager);
			animatorSet.setDuration(500);
			animatorSet.start();
			FinanceManageActivity.scrollFlag = true;
			financeManageWithdrawDepositMoney.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		initData(true);
	}
}
