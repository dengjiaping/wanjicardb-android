package com.wjika.cardstore.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Liu_ZhiChao on 2015/9/8 15:11.
 * 统一关闭activity
 */
public class ExitManager {

	public static ExitManager instance = new ExitManager();
	private List<Activity> activityList = new LinkedList<>();

	private ExitManager(){
	}

	public void addActivity(Activity activity){
		activityList.add(activity);
	}

	public void exit(){
		for(Activity activity : activityList){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
		activityList.clear();
	}

	public void remove(Activity activity) {
		if (activity != null && activityList.contains(activity)){
			activityList.remove(activity);
		}
	}
}
