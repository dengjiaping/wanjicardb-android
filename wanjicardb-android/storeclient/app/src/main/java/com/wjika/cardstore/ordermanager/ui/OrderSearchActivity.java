package com.wjika.cardstore.ordermanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.SearchBarActivity;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.OrderManagerEntity;
import com.wjika.cardstore.network.parser.Parsers;
import com.wjika.cardstore.ordermanager.adapter.OrderManagerListAdapter;

import java.util.IdentityHashMap;

/**
 * 点击搜索按钮启动该搜索界面，根据输入框中输入的内容显示数据
 * Created by kkkkk on 2016/1/26.
 */
public class OrderSearchActivity extends SearchBarActivity implements View.OnClickListener {

	private static final int REQUEST_ORDER_SEARCH = 0x01;//刷新或者首次加载
	private static final int REQUEST_ORDER_SEARCH_LOAD = 0x02;//上拉加载
	private static final int DATASIZE = 10;

	private FootLoadingListView orderManagerListView;
	private OrderManagerListAdapter managerListAdapter;
	private EditText editSearch;//搜索框
	private String searchContent;//搜索框中输入的搜索的内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_manager_frag);
		initView();
		initListView();
	}

	private void initView() {
		findViewById(R.id.search_rigth_text).setOnClickListener(this);
		editSearch = (EditText) findViewById(R.id.edit_search);
	}

	private void initListView() {
		orderManagerListView = (FootLoadingListView) findViewById(R.id.order_manager_listview);//数据列表
		orderManagerListView.setMode(PullToRefreshBase.Mode.DISABLED);
		orderManagerListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDatas(false);//下拉刷新
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDatas(true);//上拉加载
			}
		});
	}

	@Override
	protected void parseData(int requestCode, String data) {
		closeProgressDialog();
		switch (requestCode) {
			case REQUEST_ORDER_SEARCH:
				orderManagerListView.onRefreshComplete();//获取到数据之后停止刷新
				if (data != null) {
					OrderManagerEntity orderManagerEntity = Parsers.parseOrderManager(data);
					setLoadingStatus(LoadingStatus.GONE);

					if (orderManagerEntity != null && orderManagerEntity.getOrderManagerEntities() != null) {
						orderManagerListView.setVisibility(View.VISIBLE);
						managerListAdapter = new OrderManagerListAdapter(this, orderManagerEntity.getOrderManagerEntities());
						orderManagerListView.setAdapter(managerListAdapter);
						if (orderManagerEntity.getTotalPage() > 1) {
							orderManagerListView.setCanAddMore(true);
						} else {
							orderManagerListView.setCanAddMore(false);
						}
						if (0 == orderManagerEntity.getOrderManagerEntities().size()) {
							ToastUtil.shortShow(this, getString(R.string.search_no_order));
						}
					}
				}
				break;
			case REQUEST_ORDER_SEARCH_LOAD:
				orderManagerListView.onRefreshComplete();
				if (data != null) {
					OrderManagerEntity orderManagerEntity = Parsers.parseOrderManager(data);
					if (orderManagerEntity != null && orderManagerEntity.getOrderManagerEntities() != null
							&& orderManagerEntity.getOrderManagerEntities().size() > 0) {
						managerListAdapter.addDatas(orderManagerEntity.getOrderManagerEntities());
					}
					orderManagerListView.setVisibility(View.VISIBLE);
					if (orderManagerEntity != null) {

						if (managerListAdapter.getPage() < orderManagerEntity.getTotalPage()) {
							orderManagerListView.setCanAddMore(true);
						} else {
							orderManagerListView.setCanAddMore(false);
						}
					}
				}
				break;
		}
	}

	/**
	 * @param requestCode  连接网络请求数据之后的请求码，用来区分不同的网络连接返回的数据
	 * @param status       状态
	 * @param errorMessage 错误信息
	 */
	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		orderManagerListView.setOnRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//点击搜索 进行输入内容相关的搜索
			case R.id.search_rigth_text:
				searchContent = editSearch.getText().toString().trim();
				InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0); //隐藏
				if (searchContent.length() > 2) {
					showProgressDialog();
					getDatas(false);
				} else if (searchContent.length() > 0 && searchContent.length() < 3) {
					ToastUtil.longShow(this, getString(R.string.search_info_least_three));
				} else {
					ToastUtil.longShow(this, getString(R.string.search_info_no_content));
				}
				break;
		}
	}

	private void getDatas(boolean isMore) {
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("pageSize", String.valueOf(DATASIZE));
		param.put("queryParam", searchContent);
		param.put("cardOrderStatus", "");
		if (isMore) {
			//上拉加载连接网络获取数据
			param.put("pageNum", String.valueOf(managerListAdapter.getPage() + 1));
			requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_SEARCH_LOAD, FProtocol.HttpMethod.POST, param);
		} else {
			//下拉刷新或者首次搜索
			param.put("pageNum", "1");
			requestHttpData(Constants.Urls.URL_GET_ORDER_MANAGER_ALL, REQUEST_ORDER_SEARCH, FProtocol.HttpMethod.POST, param);
		}
	}
}
