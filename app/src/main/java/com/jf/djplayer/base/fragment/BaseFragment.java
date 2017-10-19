package com.jf.djplayer.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.util.LogUtil;

/**
 * Created by JF on 2016/4/10.
 * "Fragment"基类，当前应用所使用的所有"Fragment"的基类
 */
abstract public class BaseFragment extends Fragment{

    /**Fragment布局文件对象*/
    protected View layoutView;

    private static final String ON_CREATE = "--onCreate()";
    private static final String ON_STOP = "--onStop()";
    private static final String ON_DESTROY = "--onDestroy()";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印当前在哪一个Fragment，方便调试
        LogUtil.i(getClass().getSimpleName()+ON_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.i(getClass().getSimpleName()+ON_STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(getClass().getSimpleName()+ON_DESTROY);
    }
}
