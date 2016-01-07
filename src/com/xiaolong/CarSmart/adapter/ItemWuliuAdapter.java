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
import com.xiaolong.CarSmart.module.Orders;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 首页 热门产品
 */
public class ItemWuliuAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Orders> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemWuliuAdapter(List<Orders> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_wuliu_adapter,null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.item_prices = (TextView) convertView.findViewById(R.id.item_prices);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Orders typeBean = lists.get(position);
        if(typeBean != null){
            holder.item_cont.setText(typeBean.getGoodsData().get(0).getName());
            holder.item_prices.setText( res.getString(R.string.shifukuan) + typeBean.getPayable_amount());
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + typeBean.getGoodsData().get(0).getGoods_image(), holder.item_pic, CarSmartApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_pic;
        TextView item_cont;
        TextView item_prices;
    }
}
