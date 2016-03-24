package com.jf.djplayer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/1/19.
 * 装填TextView
 * 根据用户自定义来装填不同个数TextView
 *
 */
public class TextViewLinearLayout extends LinearLayout implements OnClickListener{

    private List<TextView> textViewList;
    private int unSelectedTextColor;
    private int selectedTextColor;
    private int textViewNumber;
    private float textViewTextSize;
    private AdapterView.OnItemClickListener onItemClickListener;

    public TextViewLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextViewLinearLayout);
//        获取所有静态属性
        textViewNumber = typedArray.getInteger(R.styleable.TextViewLinearLayout_textViewLinearLayout_textViewNumber,0);
        selectedTextColor = typedArray.getColor(R.styleable.TextViewLinearLayout_textViewLinearLayout_selectedTextColor, Color.BLUE);
        unSelectedTextColor = typedArray.getColor(R.styleable.TextViewLinearLayout_textViewLinearLayout_unSelectedTextColor, Color.GRAY);
        textViewTextSize = typedArray.getDimension(R.styleable.TextViewLinearLayout_textViewLinearLayout_textViewTextSize,16f);
        viewInit(context);
        typedArray.recycle();
    }
    private void viewInit(Context context){
        textViewList = new ArrayList<>(textViewNumber);
        TextView textView;
        setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        for(int i = 0;i<textViewNumber;i++){
            textView = new TextView(context);
            textView.setPadding(4, 4, 4, 4);
            textView.setTextSize(textViewTextSize);
            textView.setTextColor(unSelectedTextColor);
            textView.setGravity(Gravity.CENTER);
            addView(textView,linearParams);
            textViewList.add(textView);
        }
        textViewList.get(0).setTextColor(selectedTextColor);
    }

    /**
     * 为里面的各个栏目设置数据
     */
    public void setTextViewText(String[] textViewString){
        for(int i = 0;i<textViewList.size();i++){
            textViewList.get(i).setText(textViewString[i]);
        }
    }

    /**
     * 设置当前处于哪个item
     * @param position
     */
    public void setCurrentItem(int position){
        for(TextView textView:textViewList){
            textView.setTextColor(unSelectedTextColor);
        }
        textViewList.get(position).setTextColor(selectedTextColor);
    }
    /**
     * 为每一个TextView设置监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        for (TextView textView : textViewList) {
            textView.setOnClickListener(this);
        }
    }
    /*
    当TextView被按下回调函数
     */

    @Override
    public void onClick(View v) {
        int position = 0;
        for(int i = 0;i<textViewList.size();i++){
            if (textViewList.get(i)==v){
                position = i;
                setCurrentItem(position);
                break;
            }
        }
        onItemClickListener.onItemClick(null,v,position,0);
    }
}
