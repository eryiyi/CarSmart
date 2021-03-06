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
import com.xiaolong.CarSmart.module.Goods;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 查询结果 商品
 */
public class ItemSearchGoodsByBrandAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Goods> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemSearchGoodsByBrandAdapter(List<Goods> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_search_goods,null);
            holder.item_search_head = (ImageView) convertView.findViewById(R.id.item_search_head);
            holder.item_search_title = (TextView) convertView.findViewById(R.id.item_search_title);
            holder.item_search_money = (TextView) convertView.findViewById(R.id.item_search_money);
//            holder.item_search_cont = (TextView) convertView.findViewById(R.id.item_search_cont);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Goods cell = lists.get(position);
        if(cell != null){

            holder.item_search_title.setText(cell.getName());
//            holder.item_search_cont.setText(cell.getMarket_price());
            holder.item_search_money.setText( res.getString(R.string.money) + cell.getSell_price());
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getImg(), holder.item_search_head, CarSmartApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_search_head;
        TextView item_search_title;
        TextView item_search_money;
//        TextView item_search_cont;
    }
}
