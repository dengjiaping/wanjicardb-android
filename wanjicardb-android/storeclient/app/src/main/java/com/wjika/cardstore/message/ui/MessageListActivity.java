package com.wjika.cardstore.message.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.message.adapter.MessageAdapter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.MessageEntity;
import com.wjika.cardstore.network.entities.PageEntity;
import com.wjika.cardstore.network.parser.Parsers;

import java.util.IdentityHashMap;

/**
 * Created by Liu_Zhichao on 2016/1/13 10:55.
 * 消息列表
 */
public class MessageListActivity extends ToolBarActivity implements AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener2<ListView>{

	private static final int REQUEST_CODE_MESSAGE_INFO = 100;
	private static final int REQUEST_CODE_MESSAGE_MORE = 200;

	private FootLoadingListView messageListView;
	private MessageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_act);
		initView();
		showProgressDialog();
		loadData(false);
	}

	private void initView() {
		setLeftTitle(getString(R.string.dynamic_system_info));
		messageListView = (FootLoadingListView) findViewById(R.id.message_list_view);
		messageListView.setOnItemClickListener(this);
		messageListView.setOnRefreshListener(this);
	}

	private void loadData(boolean ismore) {
//		消息类型1：系统消息 2：消费消息 3：订单信息
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("type", "1");
		param.put("pageSize", "10");
		if (ismore){
			param.put("pageNum", String.valueOf(adapter.getPage() + 1));
			requestHttpData(Constants.Urls.URL_GET_MESSAGE_INFO, REQUEST_CODE_MESSAGE_MORE, FProtocol.HttpMethod.POST, param);
		}else {
			param.put("pageNum", "1");
			requestHttpData(Constants.Urls.URL_GET_MESSAGE_INFO, REQUEST_CODE_MESSAGE_INFO, FProtocol.HttpMethod.POST, param);
		}
	}

	@Override
	public void success(int requestCode, String data) {
		messageListView.onRefreshComplete();
		super.success(requestCode, data);
	}

	@Override
	protected void parseData(int requestCode, String data) {
		PageEntity<MessageEntity> pageEntity = Parsers.parseMessage(data);
		if (REQUEST_CODE_MESSAGE_INFO == requestCode){
			if (pageEntity != null && pageEntity.getResult() != null){
				adapter = new MessageAdapter(this, pageEntity.getResult());
				messageListView.setAdapter(adapter);
				if (pageEntity.getTotalPage() > 1){
					messageListView.setCanAddMore(true);
				}else {
					messageListView.setCanAddMore(false);
				}
				if (0 == pageEntity.getTotalPage()) {
					ToastUtil.shortShow(this, getString(R.string.dynamic_no_message));
				}
			}
		}else if (REQUEST_CODE_MESSAGE_MORE == requestCode){
			if (pageEntity != null && pageEntity.getResult() != null && pageEntity.getResult().size() > 0){
				adapter.addDatas(pageEntity.getResult());
				if (adapter.getPage() < pageEntity.getTotalPage()){
					messageListView.setCanAddMore(true);
				}else {
					messageListView.setCanAddMore(false);
				}
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		messageListView.onRefreshComplete();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MessageEntity messageEntity = adapter.getItem(position);
		Intent intent = new Intent(this, MessageInfoActivity.class);
		intent.putExtra("message", messageEntity);
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(false);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(true);
	}
}
