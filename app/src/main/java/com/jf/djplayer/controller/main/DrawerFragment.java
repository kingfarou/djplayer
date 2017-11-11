package com.jf.djplayer.controller.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;

/**
 * Created by jf on 2016/6/7.
 * 主页面-侧滑菜单
 */
public class DrawerFragment extends BaseFragment implements View.OnClickListener{

    private View optionView;//中间那些选项按钮

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_drawer, container, false);
        initView(layoutView);
        return layoutView;
    }

    private void initView(View layoutView) {
        optionView = layoutView.findViewById(R.id.option_view_fragment_drawer);
        optionView.findViewById(R.id.tv_item_fragment_drawer_sleep_setting).setOnClickListener(this);
        optionView.findViewById(R.id.tv_item_fragment_drawer_setting).setOnClickListener(this);
        optionView.findViewById(R.id.tv_item_fragment_drawer_scan_music).setOnClickListener(this);

        //设置页面退出按钮监听
        layoutView.findViewById(R.id.tv_fragment_drawer_exit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //如果按下睡眠设置
            case R.id.tv_item_fragment_drawer_sleep_setting:
                new SleepSettingsDialog().show(getChildFragmentManager(), "SleepSettingsDialog");
                break;
            //如果按下设置按钮
            case R.id.tv_item_fragment_drawer_setting:
                break;
            //如果按下音乐扫描
            case R.id.tv_item_fragment_drawer_scan_music:
                break;
            //如果按下退出按钮
            case R.id.tv_fragment_drawer_exit:
                ((MainActivity)getActivity()).exitApp();
                break;
            default:break;
        }
    }
}
