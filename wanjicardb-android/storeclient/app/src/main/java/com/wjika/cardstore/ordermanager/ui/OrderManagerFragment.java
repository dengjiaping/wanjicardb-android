package com.wjika.cardstore.ordermanager.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.OrderManagerEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.ordermanager.adapter.OrderManagerListAdapter;

import java.util.IdentityHashMap;


/**
 * 订单管理下的fragment，用来显示不同类型的订单
 * Created by kkkkk on 2016/1/25.
 */
public class OrderManagerFragment extends BaseFragment {

	private static final int REQUEST_ORDER_ALL = 0x02;
	private static final int REQUEST_ORDER_FINISH = 0x03;
	private static final int REQUEST_ORDER_READY = 0x04;
	private static final int REQUEST_ORDER_CLOSE = 0x05;
	private static final int REQUEST_ORDER_MORE = 0x06;
	private static final int DATASIZE = 10;

	private FootLoadingListView orderManagerListView;
	private OrderManagerListAdapter managerListAdapter;
	private int state;

	public void setOrderArg(int state) {
		this.state = state;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_manager_frag, null);//加载布局
		orderManagerListView = (FootLoadingListView) view.findViewById(R.id.order_manager_listview);
		orderManagerListView.setCanAddMore(true);
		orderManagerListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDatas(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDatas(true);
			}
		});
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			showProgressDialog();
			getDatas(false);
		}
	}

	/**
	 * 0 支付中 1 支付完成 2 支付取消（已关闭） 3 待支付 传空串表示全部
	 */
	private void getDatas(boolean isMore) {
		//id status page size username token
		//size 是返回数据的数量 status /0 全部 10 未支付 30 支付成功 50 已关闭/
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("pageSize", String.valueOf(DATASIZE));
		param.put("queryParam", "");
		if (isMore) {
			if(managerListAdapter != null){
				param.put("pageNum", String.valueOf(managerListAdapter.getPage() + 1));
			}
			switch (state) {
				case OrderManagerActivity.ORDER_ALL:
					param.put("cardOrderStatus", "");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_MORE, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_FINISH:
					param.put("cardOrderStatus", "1");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_MORE, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_READY:
					param.put("cardOrderStatus", "3");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_MORE, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_CLOSE:
					param.put("cardOrderStatus", "2");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_MORE, FProtocol.HttpMethod.POST, param);
					break;
			}
		} else {
			param.put("pageNum", "1");
			switch (state) {
				case OrderManagerActivity.ORDER_ALL:
					param.put("cardOrderStatus", "");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_ALL, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_FINISH:
					param.put("cardOrderStatus", "1");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_FINISH, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_READY:
					param.put("cardOrderStatus", "3");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_READY, FProtocol.HttpMethod.POST, param);
					break;
				case OrderManagerActivity.ORDER_CLOSE:
					param.put("cardOrderStatus", "2");
					requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_CLOSE, FProtocol.HttpMethod.POST, param);
					break;
			}
		}
	}

	@Override
	public void success(int requestCode, String data) {
		orderManagerListView.setOnRefreshComplete();
		super.success(requestCode, data);
	}

	@Override
	protected void parseData(int requestCode, String data) {
		switch (requestCode) {
			case REQUEST_ORDER_ALL:
				dataAnalysics(data,getString(R.string.ordermanager_no_order_buy));
				break;
			case REQUEST_ORDER_FINISH:
				dataAnalysics(data,getString(R.string.ordermanager_no_order_finish));
				break;
			case REQUEST_ORDER_READY:
				dataAnalysics(data,getString(R.string.ordermanager_no_order_ready));
				break;
			case REQUEST_ORDER_CLOSE:
				dataAnalysics(data,getString(R.string.ordermanager_no_order_close));
				break;
			case REQUEST_ORDER_MORE:
				orderManagerListView.onRefreshComplete();
				if (data != null){
					OrderManagerEntity orderManagerEntity = Parsers.parseOrderManager(data);
					managerListAdapter.addDatas(orderManagerEntity.getOrderManagerEntities());
					if (managerListAdapter.getPage() < orderManagerEntity.getTotalPage()){
						orderManagerListView.setCanAddMore(true);
					}else {
						orderManagerListView.setCanAddMore(false);
					}
				}
				break;
		}
	}

	private void dataAnalysics(String data ,String info) {
		orderManagerListView.onRefreshComplete();
		if (data != null){
			OrderManagerEntity orderManagerEntity = Parsers.parseOrderManager(data);
			if (orderManagerEntity != null && orderManagerEntity.getOrderManagerEntities() != null){
				orderManagerListView.setVisibility(View.VISIBLE);
				managerListAdapter = new OrderManagerListAdapter(getActivity(), orderManagerEntity.getOrderManagerEntities());
				orderManagerListView.setAdapter(managerListAdapter);
				if (orderManagerEntity.getTotalPage() > 1){
					orderManagerListView.setCanAddMore(true);
				}else {
					orderManagerListView.setCanAddMore(false);
				}
				if (0 == orderManagerEntity.getTotalPage()) {
					ToastUtil.shortShow(getActivity(),info);
				}
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		orderManagerListView.setOnRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}
}
