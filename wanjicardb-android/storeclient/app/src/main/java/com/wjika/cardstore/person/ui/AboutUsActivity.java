package com.wjika.cardstore.person.ui;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.wjika.cardstore.R;
import com.wjika.cardstore.StoreApplication;
import com.wjika.cardstore.base.ui.ToolBarActivity;
import com.wjika.cardstore.special.ui.SpecialActivity;

import java.util.ArrayList;

/**
 * Created by Liu_Zhichao on 2016/1/13 20:45.
 * 关于我们
 */
public class AboutUsActivity extends ToolBarActivity implements GestureOverlayView.OnGesturePerformedListener{

	private GestureLibrary libraray;//手势库

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_act);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_about_us));
		TextView aboutUsVersion = (TextView) findViewById(R.id.about_us_version);
		aboutUsVersion.setText(getString(R.string.title_activity_main, StoreApplication.getAppVersion(this)));

		GestureOverlayView gesturesOverlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		libraray = GestureLibraries.fromRawResource(this, R.raw.gestures);//加载手势库对象
		libraray.load();//加载手势库
		gesturesOverlay.addOnGesturePerformedListener(this);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = libraray.recognize(gesture);//识别用户输入的手势是否存在手势库中
		if (predictions.isEmpty()){
			ToastUtil.shortShow(this, getString(R.string.person_gesture_cant_read));
		}else {
			Prediction prediction = predictions.get(0);//得到匹配的手势
			if (prediction.score > 3){//score指相似度
				if ("9".equals(prediction.name)){
					startActivity(new Intent(this, SpecialActivity.class));
				}
			}
		}
	}
}
