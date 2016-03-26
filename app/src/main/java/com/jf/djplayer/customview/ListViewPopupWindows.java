package com.jf.djplayer.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jf.djplayer.R;

import java.util.List;

/**
 * Created by JF on 2016/3/26.
 * 自定义的使用“ListView”做内容的popupWindows
 */
public class ListViewPopupWindows extends PopupWindow{

    private ListView mListView;//唯一一个视图控件
    private AdapterView.OnItemClickListener mOnItemClickListener;//ListView的监听器

    public ListViewPopupWindows(Context context, String[] data){
        super(context);

//        listView的初始化
        mListView = (ListView)LayoutInflater.from(context).inflate(R.layout.list_view_popup_windows,null);
        mListView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data));

//        popupWindows的初始化
        setContentView(mListView);
        int width = (int)(((Activity)context).getWindowManager().getDefaultDisplay().getWidth()*0.5);
//        宽度设置成屏幕宽度的一半
        setWidth(width);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mOnItemClickListener = listener;
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

}
