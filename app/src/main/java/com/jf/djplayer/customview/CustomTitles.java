package com.jf.djplayer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/1/19.
 * 自定义的当前应用所用标题
 * 左边一个返回箭头
 * 然后一个Fragment标题
 * 最右边有一个搜索图标
 * 还有一个指示更多图标
 */
public class CustomTitles extends LinearLayout implements OnClickListener{

    private TextView tv_title_text;
    private ImageView iv_search;//搜索
    private ImageView iv_menu;//更多
//    private ImageView titleImageView;//这个就是标题图片
//    private LinearLayout titleLinearLayout;//标题图片还有文字放在一个横向线性布局里面
//    private TextView titleTextView;//这个就是标题文字
    private FragmentTitleListener fragmentTitleListener;

    public CustomTitles(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置标题背景颜色
        setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
        LayoutInflater.from(context).inflate(R.layout.custom_titles, this);
//        从布局文件里找到各个控件
//        titleLinearLayout = (LinearLayout)rootView.findViewById(R.id.ll_fragment_title_linear_layout);
//        titleImageView = (ImageView)rootView.findViewById(R.id.iv_fragment_title_title_image);
        //标题文字
        tv_title_text = (TextView)findViewById(R.id.tv_custom_titles_title_text);
        //搜索按钮
        iv_search = (ImageView)findViewById(R.id.iv_custom_titles_search);
        //菜单按钮
        iv_menu = (ImageView)findViewById(R.id.iv_custom_titles_menu);

//        读取XML文件的属性，并加各个属性设置到控件上
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitles);
//        titleImageView.setImageResource(typedArray.getResourceId(R.styleable.FragmentTitleLinearLayout_fragmentTitleLinearLayout_titleIcon, R.drawable.ic_return));
        //标题"TextView"属性设置
        Drawable drawable = context.getResources().getDrawable(typedArray.getResourceId(R.styleable.CustomTitles_custom_titles_title_icon, R.drawable.ic_return));
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_title_text.setCompoundDrawables(drawable, null, null, null);
        tv_title_text.setText(typedArray.getString(R.styleable.CustomTitles_custom_titles_title_text));

        //搜索按钮属性设置
        iv_search.setVisibility(typedArray.getInteger(R.styleable.CustomTitles_custom_titles_search_visibility, View.VISIBLE));

        //菜单按钮属性设置
        iv_menu.setVisibility(typedArray.getInteger(R.styleable.CustomTitles_custom_titles_menu_visibility, View.VISIBLE));
        typedArray.recycle();
    }

        /*xml属性对应动态设置的方法--start*/
    /**
     * 设置标题图片资源
     * @param resourceId 标题图片
     */
    public void setTitleIcon(int resourceId){
//        titleImageView.setImageResource(resourceId);
        Drawable drawable = getContext().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_title_text.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置标题文字内容
     * @param titleText 标题文字
     */
    public void setTitleText(String titleText){
        tv_title_text.setText(titleText);
    }

    /**
     * 设置“菜单”按钮是不是可见的
     * @param visibility
     */
    public void setMenuVisibility(int visibility){
        iv_menu.setVisibility(visibility);
    }

    /**
     * 设置“搜索”按钮是不是可见的
     * @param visibility
     */
    public void setSearchVisibility(int visibility){
        iv_search.setVisibility(visibility);
    }
            /*xml属性对应动态设置的方法--end*/

    /**
     * 为标题上各个按钮设置监听
     * @param fragmentTitleListener
     */
    public void setTitleClickListener(FragmentTitleListener fragmentTitleListener){
        this.fragmentTitleListener = fragmentTitleListener;
//        titleLinearLayout.setOnClickListener(this);
        tv_title_text.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    //    各个按钮点击事件响应方法
    @Override
    public void onClick(View v) {
        switch(v.getId()){
//            case R.id.ll_fragment_title_linear_layout:
//                fragmentTitleListener.onTitleClick();
//                break;
            case R.id.tv_custom_titles_title_text:
                fragmentTitleListener.onTitleClick();
                break;
            case R.id.iv_custom_titles_search:
                fragmentTitleListener.onSearchIvOnclick();
                break;
            case R.id.iv_custom_titles_menu:
                fragmentTitleListener.onMoreIvOnclick();
                break;
            default:
                break;
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
