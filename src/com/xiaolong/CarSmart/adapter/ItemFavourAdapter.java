package com.xiaolong.CarSmart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.CarSmartApplication;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.module.Favour;

import java.util.List;

/**
 * Created by Administrator on 2015/8/3.
 * 收藏列表
 */
public class ItemFavourAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Favour> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemFavourAdapter(List<Favour> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_favour_adapter,null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_money = (TextView) convertView.findViewById(R.id.item_money);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Favour typeBean = lists.get(position);
        if(typeBean != null){
            holder.item_title.setText(typeBean.getName());
            holder.item_money.setText(res.getString(R.string.money) + typeBean.getSell_price());
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + typeBean.getImg(), holder.item_pic, CarSmartApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_pic;
        TextView item_title;
        TextView item_money;
    }
}
