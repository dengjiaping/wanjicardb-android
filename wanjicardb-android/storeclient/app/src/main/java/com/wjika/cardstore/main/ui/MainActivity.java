package com.wjika.cardstore.main.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.util.Debug;
import com.seuic.android.PosdService;
import com.seuic.android.Printer;
import com.wjika.cardstore.R;
import com.wjika.cardstore.base.ui.BaseTabsActivity;
import com.wjika.cardstore.home.fragment.HomeFragment;
import com.wjika.cardstore.message.fragment.MessageFragment;
import com.wjika.cardstore.person.fragment.PersonFragment;
import com.wjika.cardstore.utils.ExitManager;
import com.yunnex.printlib.PrintUtils;

import java.util.LinkedList;
import java.util.Queue;

import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * Created by Liu_Zhichao on 2016/1/6 21:09.
 * 首页、动态、消息总页面
 */
public class MainActivity extends BaseTabsActivity {

	public static final long DIFF_DEFAULT_BACK_TIME = 2000;

	private static final String HOME = "home";
	private static final String MESSAGE = "message";
	private static final String PERSON = "person";
	public static double amount;

	private View homeView;
	private View messageView;
	private View personView;
	private long mBackTime = -1;

	public static boolean isWangPOS = false;
	public static boolean isQFPOS = false;
	public static boolean isLklPOS = false;
	public static boolean isZBPOS = false;

	private ServiceConnection posdConnection;
	private ServiceConnection conn;
	private PosdService posdService;
	public static Printer printer;
	public static AidlPrinter printerDev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabHost.setCurrentTab(0);
		checkUpdateVersion();
		initWangPOS();
		initQFPOS();
		initLklPOS();
		initZBPOS();
	}

	/**
	 * WeiposImpl的初始化（init函数）和销毁（destroy函数），
	 * 最好分别放在一级页面的onCreate和onDestroy中执行。 其他子页面不用再调用，可以直接获取能力对象并使用。
	 */
	private void initWangPOS() {
		WeiposImpl.as().init(this, new Weipos.OnInitListener() {

			@Override
			public void onInitOk() {
				try {
					// 设备可能没有打印机，open会抛异常
					WeiposImpl.as().openPrinter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				isWangPOS = true;
				Toast.makeText(MainActivity.this, getString(R.string.main_equipment_finish), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(String message) {

			}
		});
	}

	/**
	 * 初始化钱方POS
	 */
	private void initQFPOS() {
		posdConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {

				posdService = PosdService.Stub.asInterface( service );
				if ( null != posdService ) {
					try {
						printer = Printer.Stub.asInterface( posdService.getPrinter() );
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					isQFPOS = true;
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				posdService = null;
				printer = null;
			}
		};
		try {
			bindService( new Intent( "com.seuic.android.PosdService" ), posdConnection, BIND_AUTO_CREATE );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化拉卡拉POS
	 */
	private void initLklPOS() {
		conn = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
				Debug.d("aidlService服务连接成功");
				if(serviceBinder != null){
					AidlDeviceService serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
					try {
						printerDev = AidlPrinter.Stub.asInterface(serviceManager.getPrinter());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Debug.d("AidlService服务断开了");
			}
		};
		boolean flag = bindService(new Intent("lkl_cloudpos_mid_service"), conn, Context.BIND_AUTO_CREATE);
		if(flag){
			isLklPOS = true;
			Debug.d("服务绑定成功");
		}else{
			isLklPOS = false;
			Debug.d("服务绑定失败");
		}
	}

	/**
	 * 初始化掌贝POS机
	 */
	private void initZBPOS() {
		try {
			Queue<PrintUtils.PrintData> printDatas = new LinkedList<>();
			printDatas.add(PrintUtils.getByte("初始化..."));
			if (PrintUtils.bindIntent(printDatas) != null) {
				isZBPOS = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void addTabs() {
		homeView = getTabView(getString(R.string.main_firstpager), R.drawable.home_tabs_icon);
		messageView = getTabView(getString(R.string.main_dynamic), R.drawable.message_tabs_icon);
		personView = getTabView(getString(R.string.main_person), R.drawable.person_tabs_icon);
		mTabHost.addTab(mTabHost.newTabSpec(HOME).setIndicator(homeView), HomeFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(MESSAGE).setIndicator(messageView), MessageFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(PERSON).setIndicator(personView), PersonFragment.class, null);
	}

	public View getTabView(String name, int iconRes) {
		View view = View.inflate(this, R.layout.main_tabs_item, null);
		TextView homeTabsText = (TextView) view.findViewById(R.id.main_tabs_text);
		homeTabsText.setText(name);
		homeTabsText.setCompoundDrawablesWithIntrinsicBounds(0, iconRes,0,0);
		return view;
	}

	@Override
	public void onTabChanged(String tabId) {
		super.onTabChanged(tabId);
		switch (tabId){
			case HOME:
				((TextView) homeView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.home_title_bg));
				((TextView) messageView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				((TextView) personView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				break;
			case MESSAGE:
				((TextView) messageView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.home_title_bg));
				((TextView) homeView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				((TextView) personView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				break;
			case PERSON:
				((TextView) personView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.home_title_bg));
				((TextView) homeView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				((TextView) messageView.findViewById(R.id.main_tabs_text)).setTextColor(res.getColor(R.color.title_text));
				break;
		}
	}

	@Override
	public void onBackPressed() {
		long nowTime = System.currentTimeMillis();
		long diff = nowTime - mBackTime;
		if (diff >= DIFF_DEFAULT_BACK_TIME) {
			mBackTime = nowTime;
			Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
		} else {
			ExitManager.instance.exit();
		}
	}

	@Override
	public void onDestroy() {
		if (posdConnection != null) {
			unbindService(posdConnection);
		}
		if (conn != null) {
			unbindService(conn);
		}
		super.onDestroy();
		// 注意：destroy函数在一级根页面的onDestroy调用，以防止在二级页面或者返回到一级页面中
		// 使用weipos能力对象（例如：Printer）抛出服务未初始化的异常.
		WeiposImpl.as().destroy();
	}
}
