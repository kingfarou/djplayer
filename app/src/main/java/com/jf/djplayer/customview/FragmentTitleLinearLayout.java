package com.jf.djplayer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/1/19.
 * 用来统一Fragment的标题的
 * 左边一个返回箭头
 * 然后一个Fragment标题
 * 最右边有一个搜索图标
 * 还有一个指示更多图标
 */
public class FragmentTitleLinearLayout extends LinearLayout implements OnClickListener{

    private ImageView searchIv;//搜索
    private ImageView moreIv;//更多
    private ImageView titleImageView;//这个就是标题图片
    private LinearLayout titleLinearLayout;//标题图片还有文字放在一个横向线性布局里面
    private TextView titleTextView;//这个就是标题文字
    private TypedArray typedArray;
    private FragmentTitleListener fragmentTitleListener;

    public FragmentTitleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        分别创建各个控件
        titleImageView = new ImageView(context);
        titleTextView = new TextView(context);
        searchIv = new ImageView(context);
        titleLinearLayout = new LinearLayout(context);
        moreIv = new ImageView(context);
//        解析XML文件的属性
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.FragmentTitleLinearLayout);
        setAttribute();
        typedArray.recycle();
    }

//    获取并且设置各个控件属性
    private void setAttribute() {
//        LinearLayout设置属性
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
//        标题图片的初始化
        titleImageView.setImageResource(typedArray.getResourceId(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_titleIcon, R.drawable.ic_app));
        titleImageView.setPadding(16,24,0,24);
//        标题文字的初始化
        titleTextView.setText(typedArray.getString(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_titleText));
        titleTextView.setTextSize(typedArray.getDimension(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_titleTextSize, 8f));
        titleTextView.setTextColor(typedArray.getColor(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_titleTextColor, Color.WHITE));
        titleTextView.setPadding(0,24,16,24);
//        标题图片和标题文字集合在一起
        LinearLayout.LayoutParams titleLinearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        下行表示标题文字和图片之间的距离
        titleLinearLayoutParams.setMargins((int) typedArray.getDimension(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_distance, 8f), 0, 0, 0);
        titleLinearLayout.setOrientation(HORIZONTAL);
        titleLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
//        titleLinearLayout.setPadding(16, 16, 16, 16);
        titleLinearLayout.addView(titleImageView);
        titleLinearLayout.addView(titleTextView, titleLinearLayoutParams);
//        moreIv的初始化
        moreIv.setPadding(24, 24, 24, 24);
        moreIv.setVisibility(typedArray.getInteger(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_moreImageVisibility, View.VISIBLE));
        moreIv.setImageResource(R.drawable.icon_more);
//        searchIv初始化的
        searchIv.setVisibility(typedArray.getInteger(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_searchImageVisibility, View.VISIBLE));
        searchIv.setImageResource(R.drawable.icon_search);
        searchIv.setPadding(0,24,0,24);
//        各个控件添加进去
        addView(titleLinearLayout);
//        addView(fragmentTitle,linearLayoutParams);
//        这个空间只是用来占位置的
        addView(new TextView(getContext()), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));//这个用来占空间的
        addView(searchIv);
        addView(moreIv);
    }

    /**
     * 为标题上各个按钮设置监听
     * @param fragmentTitleListener
     */
    public void setItemClickListener(FragmentTitleListener fragmentTitleListener){
        this.fragmentTitleListener = fragmentTitleListener;
        titleLinearLayout.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
    }

    /**
     * 设置标题文字内容
     */
    public void setTitleText(String titleText){
        titleTextView.setText(titleText);
    }
    @Override
    public void onClick(View v) {
        if(v == titleLinearLayout){
            fragmentTitleListener.onTitleClick();
        }else if (v == searchIv ){
            fragmentTitleListener.onSearchIvOnclick();
        }else if(v == moreIv){
            fragmentTitleListener.onMoreIvOnclick();
        }
    }

    /**
     * 点击事件的监听器
     * 分别监听标题点击
     * 搜索按钮点击事件
     *“更多”按钮点击事件监听
     */
    public interface FragmentTitleListener {

        /**
         * 如果标题被按下了
         */
        void onTitleClick();

        /**
         * 如果搜索按钮被按下了
         */
         void onSearchIvOnclick();

        /**
         * 如果更多按钮被按下了
         */
         void onMoreIvOnclick();
    }
}
