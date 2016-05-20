package com.jf.djplayer.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by JF on 2015/11/1.
 * 自定义"GridView"，用来嵌入到"ExpandableListView"里面，
 * 需要重写"onMeasure"方法
 */
public class ExpandableChildGridView extends GridView {

    public ExpandableChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        MeasureSpec.makeMeasureSpec(int, int):将给定的数字以及模式，拼出一个MeasureSpec值
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
