package com.wjika.cardstore.commoditymanage.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseFragment;
import com.wjika.cardstore.commoditymanage.adapter.CommodityCategoryAdapter;
import com.wjika.cardstore.network.Constants;
import com.wjika.cardstore.network.entities.CommodityEntity;
import com.wjika.cardstore.network.parser.Parsers;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhangzhaohui on 2016/1/12.
 * 商品管理的pager页面
 */
public class CommoditySellingFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

	private static final int REQUEST_COMMODITY_SELLING = 0x1;
	private static final int REQUEST_COMMODITY_SELLING_MORE = 0x2;
	private static final int REQUEST_COMMODITY_SOLD_OUT = 0x3;
	private static final int REQUEST_COMMODITY_SOLD_OUT_MORE = 0x4;
	private static final int REQUEST_CODE_UPDATESTATUS = 0x5;

	private Context context;
	private int status;
	private FootLoadingListView footLoadingListView;
	private CommodityCategoryAdapter commodityCategoryAdapter;
	private CommodityEntity tag;
	private RadioButton commodityManageSoldOut;
	private RadioButton commodityManageSelling;
	private Resources res;
	private String tmpId;
	private int changeStatus = -1;
	private int mSellingCount;
	private int mSellOutCount;
	private List<CommodityEntity> commodityEntities;
	private boolean canAddMore = true;

	public void setArgs(Context context, int status, RadioButton commodityManageSelling, RadioButton commodityManageSoldOut) {
		this.status = status;
		this.context = context;
		this.commodityManageSelling = commodityManageSelling;
		this.commodityManageSoldOut = commodityManageSoldOut;
		res = context.getResources();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.commodity_manage_frag, null);
		footLoadingListView = (FootLoadingListView) view.findViewById(R.id.commodity_list);
		footLoadingListView.setMode(PullToRefreshBase.Mode.BOTH);
		footLoadingListView.setOnRefreshListener(this);
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (status == CommodityManageActivity.SELLING) {
				showProgressDialog();
				loadData(true, false);
			} else if (status == CommodityManageActivity.SOLD_OUT) {
				showProgressDialog();
				loadData(false, false);
			}
		}
	}

	/**
	 * 商品数据请求
	 * @param flag 出售中 true 已下架 false
	 * @param isMore 加载更多true 刷新false
	 */
	private void loadData(boolean flag, boolean isMore) {
		//1：上架 0：下架
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("pageSize", "10");

		int requestCode;
		if (flag) {
			param.put("merchantCardStatus", "1");
			if (isMore) {
				requestCode = REQUEST_COMMODITY_SELLING_MORE;
			} else {
				requestCode = REQUEST_COMMODITY_SELLING;
			}
		} else {
			param.put("merchantCardStatus", "0");
			if (isMore) {
				requestCode = REQUEST_COMMODITY_SOLD_OUT_MORE;
			} else {
				requestCode = REQUEST_COMMODITY_SOLD_OUT;
			}
		}

		int pageNum = 1;
		if (isMore) {
			pageNum = commodityCategoryAdapter.getPage() + 1;
		}
		param.put("pageNum", String.valueOf(pageNum));
		requestHttpData(Constants.Urls.URL_GET_MERCHANT_PRODUCTS, requestCode, FProtocol.HttpMethod.POST, param);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		footLoadingListView.onRefreshComplete();
		switch (requestCode) {
			case REQUEST_COMMODITY_SELLING: {
				CommodityEntity commodityEntity = Parsers.parseCommodity(data);
				if (commodityEntity != null) {
					commodityEntities = commodityEntity.getCommodityEntities();
					mSellingCount = commodityEntity.getOnShelf();
					mSellOutCount = commodityEntity.getOffShelf();
					commodityManageSelling.setText(String.format(res.getString(R.string.commodity_on_offer), mSellingCount));
					commodityManageSoldOut.setText(String.format(res.getString(R.string.commodity_solded_out), mSellOutCount));
					if (commodityEntities.size() < 1) {
						ToastUtil.shortShow(getActivity(), getString(R.string.commodity_no_sell));
						return;
					}
					commodityCategoryAdapter = new CommodityCategoryAdapter(context, commodityEntities, this);
					footLoadingListView.setAdapter(commodityCategoryAdapter);
					canAddMore = commodityEntity.getTotalPage() > 1;
					footLoadingListView.setCanAddMore(canAddMore);
				}
				break;
			}
			case REQUEST_COMMODITY_SELLING_MORE:{
				CommodityEntity commodityEntity = Parsers.parseCommodity(data);
				if (commodityEntity != null) {
					commodityCategoryAdapter.addDatas(commodityEntity.getCommodityEntities());
					if (commodityEntity.getTotalPage() <= commodityCategoryAdapter.getPage()) {
						canAddMore = false;
						footLoadingListView.setCanAddMore(false);
					}
				}
				break;
			}
			case REQUEST_COMMODITY_SOLD_OUT: {
				CommodityEntity commodityEntity = Parsers.parseCommodity(data);
				if (commodityEntity != null) {
					commodityEntities = commodityEntity.getCommodityEntities();
					mSellingCount = commodityEntity.getOnShelf();
					mSellOutCount = commodityEntity.getOffShelf();
					commodityManageSelling.setText(String.format(res.getString(R.string.commodity_on_offer), mSellingCount));
					commodityManageSoldOut.setText(String.format(res.getString(R.string.commodity_solded_out), mSellOutCount));
					commodityCategoryAdapter = new CommodityCategoryAdapter(context, commodityEntities, this);
					footLoadingListView.setAdapter(commodityCategoryAdapter);
					canAddMore = commodityEntity.getTotalPage() > 1;
					footLoadingListView.setCanAddMore(canAddMore);
					if (commodityEntities.size() < 1) {
						ToastUtil.shortShow(getActivity(), getString(R.string.commodity_no_sold_out));
					}
				}
				break;
			}
			case REQUEST_COMMODITY_SOLD_OUT_MORE:{
				CommodityEntity commodityEntity = Parsers.parseCommodity(data);
				if (commodityEntity != null) {
					commodityCategoryAdapter.addDatas(commodityEntity.getCommodityEntities());
					if (commodityEntity.getTotalPage() <= commodityCategoryAdapter.getPage()) {
						canAddMore = false;
						footLoadingListView.setCanAddMore(false);
					}
				}
				break;
			}
			case REQUEST_CODE_UPDATESTATUS:
				tag.setMerchantCardStatus(changeStatus);
				if (commodityCategoryAdapter != null) {
					commodityCategoryAdapter.remove(tag);
					commodityCategoryAdapter.notifyDataSetChanged();
					if (status == CommodityManageActivity.SOLD_OUT) {
						commodityManageSelling.setText(String.format(res.getString(R.string.commodity_on_offer), ++mSellingCount));
						mSellOutCount = --mSellOutCount < 0 ? 0 : mSellOutCount;//保证不为负数
						commodityManageSoldOut.setText(String.format(res.getString(R.string.commodity_solded_out), mSellOutCount));
						if (0 == commodityCategoryAdapter.getCount() && canAddMore) {
							loadData(false, false);
						}
						if (0 == mSellOutCount) {
							ToastUtil.shortShow(getActivity(), getString(R.string.commodity_no_sold_out));
						}
					} else if (status == CommodityManageActivity.SELLING) {
						commodityManageSoldOut.setText(String.format(res.getString(R.string.commodity_solded_out), ++mSellOutCount));
						mSellingCount = --mSellingCount < 0 ? 0 : mSellingCount;
						commodityManageSelling.setText(String.format(res.getString(R.string.commodity_on_offer), mSellingCount));
						if (0 == commodityCategoryAdapter.getCount() && canAddMore) {
							loadData(true, false);
						}
						if (0 == mSellingCount) {
							ToastUtil.shortShow(getActivity(), getString(R.string.commodity_no_sell));
						}
					}
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		footLoadingListView.onRefreshComplete();
		switch (requestCode) {
			case REQUEST_CODE_UPDATESTATUS:
				changeStatus = -1;
				break;
		}
		ToastUtil.shortShow(context, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.commodity_list_item_putaway:
				tag = (CommodityEntity) v.getTag();
				String merchantCardId = tag.getMerchantCardId();
				int status = tag.getMerchantCardStatus();
				if (merchantCardId.equals(tmpId) && status == changeStatus) {
					return;
				}
				if (CommodityManageActivity.SOLD_OUT == tag.getMerchantCardStatus()) {
					loadData(merchantCardId, CommodityManageActivity.SELLING);
				} else if (CommodityManageActivity.SELLING == tag.getMerchantCardStatus()) {
					loadData(merchantCardId, CommodityManageActivity.SOLD_OUT);
				}
				break;
		}
	}

	/**
	 * 上下架网络请求
	 */
	private void loadData(String merchantCardId, int status) {
		showProgressDialog();
		changeStatus = status;
		tmpId = merchantCardId;
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("merchantCardId", merchantCardId);
		params.put("merchantCardStatus", String.valueOf(status));
		requestHttpData(Constants.Urls.URL_POST_PRODUCT_UPDATESTATUS, REQUEST_CODE_UPDATESTATUS, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (status == CommodityManageActivity.SELLING) {
			loadData(true, false);
		} else if (status == CommodityManageActivity.SOLD_OUT) {
			loadData(false, false);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (status == CommodityManageActivity.SELLING) {
			loadData(true, true);
		} else if (status == CommodityManageActivity.SOLD_OUT) {
			loadData(false, true);
		}
	}
}
