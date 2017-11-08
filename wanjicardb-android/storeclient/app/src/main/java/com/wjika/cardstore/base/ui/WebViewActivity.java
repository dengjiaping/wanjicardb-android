package com.wjika.cardstore.base.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wjika.cardstore.R;

public class WebViewActivity extends ToolBarActivity {

	public static final String EXTRA_URL = "URL";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_RIGHT_ICON = "rightIcon";
	public static final String PRESSED_BACKICON_IMMEDIATE_BACK = "PRESSED_BACKICON_IMMEDIATE_BACK";
	public static final String LEFT_BACK_BUTTON_DISPLAY = "left_back_btn_display";
	public static final String RIGHT_CLOSE_BUTTON_DISPLAY = "right_close_btn_display";
	public static final String RIGHT_SHARE_BUTTON_DISPLAY = "right_share_btn_display";
	public static final String EXTRA_SHARE_URL = "extra_share_url";

	private TextView mTitleTextView;
	private ProgressBar mHorizontalProgress;
	private WebView mWebView;
	private ImageView backBtn;
	private boolean mIsImmediateBack = false;
	private boolean mIsLeftBtnDisplay = true;
	private boolean mIsRightBtnDisplay = true;
	private boolean mIsRightShareBtnDisplay = false;
	private String url;
	private String from;
	private String title;
	private String baseUrl;

	private Handler refreshProgressHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 >= 100) {
				if (mHorizontalProgress != null) {
					mHorizontalProgress.setVisibility(View.GONE);
				}
			} else {
				if (mHorizontalProgress != null && msg.arg1 >= 0) {
					mHorizontalProgress.setVisibility(View.VISIBLE);
					mHorizontalProgress.setProgress(msg.arg1);
				}
			}
		}

		;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview_act);
	    /*if (getIntent() != null){
            title = getIntent().getStringExtra(EXTRA_TITLE);
            if (getIntent().getData() != null && getIntent().getDataString() != null){
                String loadUrl = getIntent().getDataString();
                Uri uri = Uri.parse(loadUrl);
                url = uri.getQueryParameter("url");
                String debug = uri.getQueryParameter("debug");
                String whiteKey = uri.getHost();
                if (whiteKey != null && (whiteKey.contains("lesports.com") || "1".equals(debug) || whiteKey.contains("letv.com"))){
                    mIsRightShareBtnDisplay = true;
                }


                if (url != null && !url.contains("?")){

                    url = url + "?";
                }else {
                    url = url + "&";
                }
                String udid = DeviceUtil.getAppUniqueToken(this);
                long currentTime = SystemClock.elapsedRealtime()/1000;//此处时间服务器以秒为单位
                String key = "tooaQsrpUfvH2u3HYoDgkWLJ9RvhQ2sr";
                String auth = StringUtil.MD5(udid + currentTime + key);
                baseUrl = url + "udid=" + udid;
                url = url + "udid=" + udid + "&tm=" + currentTime + "&auth=" + auth;
                title = uri.getQueryParameter("title") == null ? "乐视体育" : uri.getQueryParameter("title");
                from = "restartApp";
            }else {
                url = getIntent().getStringExtra(EXTRA_URL);
                String shareUrl = getIntent().getStringExtra(EXTRA_SHARE_URL);
                if (shareUrl != null){
                    baseUrl = shareUrl;
                }else {
                    baseUrl = url;
                }
                from = getIntent().getStringExtra("from");
                mIsRightShareBtnDisplay = getIntent().getBooleanExtra(RIGHT_SHARE_BUTTON_DISPLAY, false);
            }

            boolean rightIcon = getIntent().getBooleanExtra(EXTRA_RIGHT_ICON, false);
            mIsImmediateBack = getIntent().getBooleanExtra(PRESSED_BACKICON_IMMEDIATE_BACK, false);
            mIsLeftBtnDisplay = getIntent().getBooleanExtra(LEFT_BACK_BUTTON_DISPLAY, true);
            mIsRightBtnDisplay =  getIntent().getBooleanExtra(RIGHT_CLOSE_BUTTON_DISPLAY, false);
//            if (title != null && title.length() > 0) {
//                mToolbar.setTitle(title);
//            }
        }*/
		url = getIntent().getStringExtra(EXTRA_URL);
		title = getIntent().getStringExtra(EXTRA_TITLE);
		if (!TextUtils.isEmpty(title)) {
			setLeftTitle(title);
		}
		mHorizontalProgress = (ProgressBar) findViewById(R.id.progress_horizontal);
		mWebView = (WebView) findViewById(R.id.webview);
		// 设置支持JavaScript
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
//		webSettings.setDatabaseEnabled(true);
//		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setGeolocationEnabled(true);
//		webSettings.setGeolocationDatabasePath(dir);
		webSettings.setDomStorageEnabled(true);

		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		if (Build.VERSION.SDK_INT > 11) {
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("wjika.com")) {
					mWebView.loadUrl(url);
					return true;
				}
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
                /*Uri uri = Uri.parse(url);
                if (url.startsWith("tel:") || url.startsWith("mailto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (uri.getScheme().equals("letvsportslaunch") && uri.getHost().equals("com.lesports.glivesports")) {
                    url = uri.getQueryParameter("url");
                    mWebView.loadUrl(url);
                    return true;
                } else {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }*/
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend, Message resend) {
				resend.sendToTarget();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(view.getContext(), getString(R.string.network_anomaly), Toast.LENGTH_SHORT).show();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (refreshProgressHandler != null) {
					if (refreshProgressHandler.hasMessages(0)) {
						refreshProgressHandler.removeMessages(0);
					}
					Message mMessage = refreshProgressHandler.obtainMessage(0, newProgress, 0, null);
					refreshProgressHandler.sendMessageDelayed(mMessage, 100);
				}
			}

			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});

		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		mWebView.loadUrl(url);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		if (backBtn != null) {
			if (mIsLeftBtnDisplay) {
				backBtn.setVisibility(View.VISIBLE);
			} else {
				backBtn.setVisibility(View.GONE);
			}

			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(backBtn.getApplicationWindowToken(), 0);

					if (mIsImmediateBack) {
						onBackPressed();
					} else {
						if (mWebView.canGoBack()) {
							mWebView.goBack();
						} else {
							onBackPressed();
						}
					}
				}
			});
		}

		final TextView closeBtn = null;
		if (closeBtn != null) {
			if (mIsRightBtnDisplay) {
				closeBtn.setVisibility(View.VISIBLE);
			} else {
				closeBtn.setVisibility(View.GONE);
			}
			closeBtn.setText(getText(R.string.close));
			closeBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(closeBtn.getApplicationWindowToken(), 0);
					onBackPressed();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onDestroy() {
		mWebView.destroy();
		super.onDestroy();
	}
}
