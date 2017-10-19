package com.jf.djplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/1/19.
 * 自定义的当前应用所用标题，对外提供以下四个控件：
 * 1>标题图片；2>标题文字；3>搜索按钮；4>菜单按钮。
 * 其中标题图片和文字在内部是同一个控件实现，由"TextView"及其"drawableLeft"属性实现
 * 标题图片和文字为必选，一定显示，搜索按钮和菜单按钮为可选，可以隐藏。
 * 提供三个点击事件监听：标题点击：onTitleClickListener、搜索点击：onSearchClickListener、菜单点击：onMenuClickListener
 */
public class TitleBar extends LinearLayout implements OnClickListener{

    private TextView titleTextTv;       //标题文字
    private ImageView searchIv;         //搜索按钮
    private ImageView menuIv;           //菜单按钮

    private OnTitleClickListener onTitleClickListener;      //标题栏点击事件监听器
    private OnSearchClickListener onSearchClickListener;    //搜索按钮点击事件的监听器
    private OnMenuClickListener onMenuClickListener;        //菜单按钮点击事件的监听器

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //背景颜色、绘制布局
        setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);

        //标题文字，搜索按钮，菜单按钮
        titleTextTv = (TextView)findViewById(R.id.tv_layout_title_bar_title_text);
        searchIv = (ImageView)findViewById(R.id.iv_layout_title_bar_search);
        menuIv = (ImageView)findViewById(R.id.iv_layout_title_bar_menu);

        //读取XML文件的属性，并加各个属性设置到控件上
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        //标题"TextView"属性设置
        Drawable drawable = context.getResources().getDrawable(typedArray.getResourceId(R.styleable.TitleBar_titleIcon, R.drawable.ic_return));
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        titleTextTv.setCompoundDrawables(drawable, null, null, null);
        titleTextTv.setText(typedArray.getString(R.styleable.TitleBar_titleText));

        //搜索按钮属性设置
        searchIv.setVisibility(typedArray.getInteger(R.styleable.TitleBar_searchIconVisibility, View.VISIBLE));
        //菜单按钮属性设置
        menuIv.setVisibility(typedArray.getInteger(R.styleable.TitleBar_menuIconVisibility, View.VISIBLE));

        typedArray.recycle();
    }

        /*xml属性对应动态设置的方法--start*/
    /**
     * 设置标题图片资源
     * @param resourceId 标题图片
     */
    public void setTitleIcon(int resourceId){
        Drawable drawable = getContext().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        titleTextTv.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置标题文字内容
     * @param titleText 标题文字
     */
    public void setTitleText(String titleText){
        titleTextTv.setText(titleText);
    }

    /**
     * 设置“菜单”按钮是不是可见的
     * @param visibility
     */
    public void setMenuVisibility(int visibility){
        menuIv.setVisibility(visibility);
    }

    /**
     * 设置“搜索”按钮是不是可见的
     * @param visibility
     */
    public void setSearchVisibility(int visibility){
        searchIv.setVisibility(visibility);
    }
            /*xml属性对应动态设置的方法--end*/

    /*以下，是标题栏三个按钮的监听器设置，分别是标题按钮，搜索按钮，菜单按钮，
    由于不是每个页面都会具备三个按钮，有些页面可能只有一个两个，所以将它们的点击事件接口分开来写*/
    /**
     * 为标题栏的标题按钮设置点击事件的监听器
     * @param onTitleClickListener 实现了"OnTitleClickListener"接口的类
     */
    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener){
        this.onTitleClickListener = onTitleClickListener;
        titleTextTv.setOnClickListener(this);
    }

    /**
     * 为标题栏搜索按钮设置点击事件的监听器
     * @param onSearchClickListener 实现了"OnSearchClickListener"接口的类
     */
    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener){
        this.onSearchClickListener = onSearchClickListener;
        searchIv.setOnClickListener(this);
    }

    /**
     * 为标题栏菜单按钮设置点击事件
     * @param onMenuClickListener 实现了"OnMenuClickListener"接口的类
     */
    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener){
        this.onMenuClickListener = onMenuClickListener;
        menuIv.setOnClickListener(this);
    }

    //点击事件响应方法
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_layout_title_bar_title_text){
            //点击标题
            if(onTitleClickListener != null) {
                onTitleClickListener.onTitleClick(v);
            }
        }else if(id == R.id.iv_layout_title_bar_search){
            //点击搜索
            if(onSearchClickListener != null){
                onSearchClickListener.onSearchClick(v);
            }
        }else if(id == R.id.iv_layout_title_bar_menu){
            //点击菜单
            if(onMenuClickListener != null){
                onMenuClickListener.onMenuClick(v);
            }
        }
    }

    /*标题栏点击事件监听器，分别对应点击标题，点击搜索，点击菜单_start*/
    /**标题点击事件监听器*/
    public interface OnTitleClickListener{
        /**
         * 当标题被点击时，回调该方法
         * @param titleView 标题View对象
         */
        void onTitleClick(View titleView);
    }

    /**搜索按钮点击事件的监听器**/
    public interface OnSearchClickListener{
        /**
         * 当搜索按钮被点击时，回调该方法
         * @param SearchView 搜索按钮View对象
         */
        void onSearchClick(View SearchView);
    }

    /**菜单按钮点击事件的监听器*/
    public interface OnMenuClickListener{
        /**
         * 菜单按钮被点击时，回调该方法
         * @param menuView 菜单按钮View对象
         */
        void onMenuClick(View menuView);
    }
    /*标题栏点击事件监听器，分别对应点击标题，点击搜索，点击菜单_end*/
}
