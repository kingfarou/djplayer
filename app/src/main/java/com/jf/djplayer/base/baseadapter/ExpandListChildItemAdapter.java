package com.jf.djplayer.base.baseadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.jf.djplayer.R;

/**
 * Created by JF on 2015/11/1.
 * ExpandableListView
 * 里的子栏目的GridView的适配器
 */
public class ExpandListChildItemAdapter extends BaseAdapter {

    private String[] text;//GridView每个Item文字
    private int[] icon;//GridView每个Item图片
    private Context context;

    /**
     * default constructor
     * @param text the text for all item's title
     * @param icon the icon for all item's icon
     */
    public ExpandListChildItemAdapter(Context context, String[] text, int[] icon){
        this.context = context;
        this.text = text;
        this.icon = icon;
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        Button itemButton = null;//喜欢
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder =  new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_view_child,null);
            viewHolder.itemButton = (Button)convertView.findViewById(R.id.btn_item_grid_view_child);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.itemButton.setText(this.text[position]);
//        动态设置每一个按钮的drawable
        Drawable buttonDrawable = context.getResources().getDrawable(icon[position]);
        buttonDrawable.setBounds(0,0,buttonDrawable.getMinimumWidth(),buttonDrawable.getMinimumHeight());
        viewHolder.itemButton.setCompoundDrawables(null, buttonDrawable, null, null);
        return convertView;
    }
}
