package com.jf.djplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by JF on 2015/11/1.
 */
public class ExpandableGridView extends GridView {

    public ExpandableGridView(Context context, AttributeSet attrs) {
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
