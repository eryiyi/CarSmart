package com.xiaolong.CarSmart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.module.TypeBean;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 首页 分类按钮
 */
public class ItemTypeBeanAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<TypeBean> lists;
    private Context mContect;
    Resources res;
    Drawable img_one;

    public ItemTypeBeanAdapter(List<TypeBean> lists, Context mContect){
        this.lists = lists;
        this.mContect = mContect;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        res = mContect.getResources();

        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_type_one,null);
            holder.textview = (TextView) convertView.findViewById(R.id.textview);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final TypeBean typeBean = lists.get(position);
        if(typeBean != null){
            holder.textview.setText(typeBean.getName());
            img_one = res.getDrawable(typeBean.getPic());
            img_one.setBounds(0, 0, img_one.getMinimumWidth(), img_one.getMinimumHeight());
            holder.textview.setCompoundDrawables(null, img_one, null, null);
        }
        return convertView;
    }
    class ViewHolder {
        TextView textview;
    }
}
