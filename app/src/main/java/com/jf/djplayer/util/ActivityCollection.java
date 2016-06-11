package com.jf.djplayer.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2015/11/18.
 * 这个集合的设置是为了能够退出应用所有窗体
 */
public class ActivityCollection {
    public static List<Activity> MY_ACTIVITIES = new ArrayList<>(4);
    public static void addActivities(Activity activity){
        MY_ACTIVITIES.add(activity);
    }
    public static void removeActivity(Activity activity){
        MY_ACTIVITIES.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:MY_ACTIVITIES){
            activity.finish();
        }
    }
}



