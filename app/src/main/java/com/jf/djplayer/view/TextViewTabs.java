package com.jf.djplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.util.DimenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/1/19.
 * 装填TextView
 * 根据用户自定义来装填不同个数TextView
 *
 */
public class TextViewTabs extends LinearLayout implements OnClickListener{

    private List<TextView> textViewList;//装填"TextView"栏目所用的集合
    private int unSelectedTextColor;//栏目未被选中时的颜色
    private int selectedTextColor;//栏目被选中时候的颜色
    private String[] itemText;//装填"TextView"文字的数组
    private float itemTextSize;//栏目里的文字尺寸
    private AdapterView.OnItemClickListener onItemClickListener;//栏目被点击监听器
    private int lastItemPosition;//记录最新栏目位置

    public TextViewTabs(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextViewTabs);
//        获取所有静态属性
        selectedTextColor = typedArray.getColor(R.styleable.TextViewTabs_textViewTabs_selectedTextColor, Color.BLUE);
        unSelectedTextColor = typedArray.getColor(R.styleable.TextViewTabs_textViewTabs_unSelectedTextColor, Color.GRAY);
        itemTextSize = typedArray.getDimension(R.styleable.TextViewTabs_textViewTabs_itemTextSize, DimenUtil.spToPx(16));
        //默认该控件不显示，如果调用者返回的栏目文字个数不等于零，该控件才能显示
        setVisibility(GONE);
        typedArray.recycle();
    }

    /**
     * 设置各栏目的文字
     * 如果当前栏目个数为零，方法无效
     * @param itemText 各个栏目的文字
     */
    public void setItemText(String[] itemText) {
        if(itemText == null){
            return;
        }
        this.itemText = itemText;
        initItemTextView(this.itemText);
    }

    public String[] getItemText(){
        return this.itemText;
    }

    //根据所给定的Item数目，创建并添加"TextView"
    private void initItemTextView(String[] itemText){
        if(itemText == null){
            return;
        }
        textViewList = new ArrayList<>(itemText.length);
        TextView textView;
        Context context = getContext();
        //"linearParams"表示这些对象所公有的属性
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        for(int i = 0;i< itemText.length;i++){
            textView = new TextView(context);
            textView.setPadding(4, 4, 4, 4);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);
            textView.setTextColor(unSelectedTextColor);
            textView.setText(itemText[i]);
            textView.setGravity(Gravity.CENTER);
            addView(textView,linearParams);
            textViewList.add(textView);
        }
        //为选中的栏目设置颜色，默认选中第一个栏目

        lastItemPosition = 0;
        textViewList.get(lastItemPosition).setTextColor(selectedTextColor);
        //显示控件
        setVisibility(VISIBLE);
    }

    /**
     * 设置当前处于哪个item
     * @param position
     */
    public void setCurrentItem(int position){
        if(textViewList == null){
            return;
        }
        if(position == lastItemPosition){
            return;
        }
        textViewList.get(lastItemPosition).setTextColor(unSelectedTextColor);
        textViewList.get(position).setTextColor(selectedTextColor);
        lastItemPosition = position;
    }

    /**
     * 为每一个TextView设置监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener) {
        if(textViewList == null ||textViewList.size()==0){
            return;
        }
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
                break;
            }
        }
        setCurrentItem(position);
        onItemClickListener.onItemClick(null,v,position,0);
    }
}
