package com.jf.djplayer.base.basefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by JF on 2016/4/10.
 * "Fragment"基类，当前应用所使用的所有"Fragment"的基类
 */
public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        打印当前在哪一个Fragment，方便调试
        Log.i("fragment_name",this.getClass().getSimpleName());
    }
}
