package com.xiaolong.CarSmart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.module.News;

import java.util.List;

/**
 * Created by Administrator on 2015/8/3.
 */
public class ItemNewsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<News> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemNewsAdapter(List<News> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_news_adapter,null);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final News typeBean = lists.get(position);
        if(typeBean != null){
            holder.item_title.setText(typeBean.getTitle());
            holder.item_cont.setText(typeBean.getContent());
            holder.item_dateline.setText(typeBean.getCreate_time());
        }
        return convertView;
    }
    class ViewHolder {
        TextView item_cont;
        TextView item_title;
        TextView item_dateline;
    }
}
