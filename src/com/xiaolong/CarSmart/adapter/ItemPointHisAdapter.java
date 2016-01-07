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
import com.xiaolong.CarSmart.module.PointHistory;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemPointHisAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<PointHistory> lists;
    private Context mContect;
    Resources res;
    DecimalFormat df = new DecimalFormat("0.00");

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemPointHisAdapter(List<PointHistory> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_point_history,null);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_count = (TextView) convertView.findViewById(R.id.item_count);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final PointHistory cell = lists.get(position);
        if(cell != null){

            holder.item_title.setText(cell.getIntro());
            holder.item_count.setText(cell.getValue());
            holder.item_dateline.setText(cell.getDatetime());
        }
        return convertView;
    }
    class ViewHolder {
        TextView item_title;
        TextView item_count;
        TextView item_dateline;
    }
}
