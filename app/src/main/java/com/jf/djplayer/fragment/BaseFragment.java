package com.jf.djplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by JF on 2016/4/10.
 */
public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        打印当前在哪一个Fragment，方便调试
        Log.i("fragment_name",this.getClass().getSimpleName());
    }
}
