package com.jf.djplayer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/1/19.
 * 带有一个ImageView和一个
 * TextView的LinearLayout
 */
public class ImageTextLinearLayout extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    public ImageTextLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextLinearLayout);
//        创建里面两个空间
        imageView = new ImageView(context);
        textView = new TextView(context);

//        给个控件设置属性
        imageView.setImageResource(typedArray.getResourceId(R.styleable.ImageTextLinearLayout_imageTextLinearLayout_icon, R.drawable.djplayer));
        textView.setTextSize(typedArray.getDimension(R.styleable.ImageTextLinearLayout_imageTextLinearLayout_textSize,16));
        textView.setText(typedArray.getString(R.styleable.ImageTextLinearLayout_imageTextLinearLayout_text));
//        设置线性布局自身属性
        setOrientation(LinearLayout.HORIZONTAL);//设置布局横向排布
        setGravity(Gravity.CENTER_VERTICAL);//所有内容竖向居中
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        将空间都添加进去
        addView(imageView,layoutParams);
//        设置两个控件距离
        int leftMargin = (int)typedArray.getDimension(R.styleable.ImageTextLinearLayout_imageTextLinearLayout_distance, 8f);
        layoutParams.setMargins( leftMargin ,0,0, 0);
        addView(textView, layoutParams);
        typedArray.recycle();
    }

    /**
     * 设置ImageView的图片
     * @param drawableId 图片ID
     */
    public void setImageIcon(int drawableId){
        imageView.setImageResource(drawableId);
    }

    /**
     * 设置文字的内容的
     * @param text 文字内容
     */
    public void setText(String text){
        textView.setText(text);
    }
}
