package com.jf.djplayer.base.basefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.base.MyApplication;

/**
 * Created by JF on 2016/4/10.
 * "Fragment"基类，当前应用所使用的所有"Fragment"的基类
 */
abstract public class BaseFragment extends Fragment{

    protected View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印当前在哪一个Fragment，方便调试
        MyApplication.showLog(this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initExtra();
        layoutView = inflater.inflate(getLayoutId(), container, false);
        initView(layoutView);
        return layoutView;
    }

    /**
     * 获取要加载的布局文件Id
     * @return
     */
    abstract protected int getLayoutId();

    /**
     * 在"View"初始化前的初始化
     */
    abstract protected void initExtra();

    /**
     * 初始化View
      * @param layoutView 在"getLayoutId()"方法里面所返回的Id所对应的View
     */
    abstract protected void initView(View layoutView);
}
