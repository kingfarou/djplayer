package com.jf.djplayer.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.InfoClass;
import com.jf.djplayer.R;
/**
 * Created by Administrator on 2015/8/12.
 * 窗体菜单键所用的"GridView"的适配器
 */
public class CustomMenuAdapter extends BaseAdapter {

    private CustomMenuBean[] customMenuBeans = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    public CustomMenuAdapter(Context context){
        this.context = context;
        SharedPreferences playPreference = context.getSharedPreferences(InfoClass.FileInfo.playerOptionsFileName, context.MODE_PRIVATE);
        int playMode = playPreference.getInt("circulationsPosition", -1);
        layoutInflater = LayoutInflater.from(context);
        customMenuBeans =
                new CustomMenuBean[]{
                        new CustomMenuBean(R.drawable.icon_activity_menu_scan_song,"扫描音乐"),
                        new CustomMenuBean(R.drawable.icon_activity_menu_sleep,"睡眠"),
                        new CustomMenuBean(R.drawable.icon_activity_menu_theme_background,"主题背景"),
                        new CustomMenuBean(R.drawable.icon_activity_menu_sound, "音效"),
                        new CustomMenuBean(R.drawable.icon_activity_menu_order, InfoClass.Options.PLAY_MODE[playMode]),
                        new CustomMenuBean(R.drawable.icon_activity_menu_listen_music,"听歌识别"),
                        new CustomMenuBean(R.drawable.icon_activity_menu_setting,"设置"),
                        new CustomMenuBean(R.drawable.activity_menu_exit,"退出")};
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return this.customMenuBeans[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_custom_activity_menu,null);
            viewHolder.customMenuImageView = (ImageView)convertView.findViewById(R.id.iv_item_custom_activity_menu);
            viewHolder.customMenuTextView = (TextView)convertView.findViewById(R.id.tv_item_custom_activity_menu);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.customMenuImageView.setImageResource(this.customMenuBeans[position].customMenuIvResource);
        viewHolder.customMenuTextView.setText(this.customMenuBeans[position].customMenuTvString);
        return convertView;
    }

    class ViewHolder{
        public ImageView customMenuImageView = null;
        public TextView customMenuTextView = null;
    }

    //定义内部类来保存资源
    class CustomMenuBean {
        public int customMenuIvResource = -1;//要显示的图片ID
        public String customMenuTvString = null;//要显示的文字内容
        public CustomMenuBean(int customMenuIvResource, String customMenuTvString){
            this.customMenuIvResource = customMenuIvResource;
            this.customMenuTvString = customMenuTvString;
        }
    }
}
