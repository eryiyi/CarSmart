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
import com.xiaolong.CarSmart.module.GoodsHot;
import com.xiaolong.CarSmart.util.StringUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 首页 热门产品
 */
public class ItemHotAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodsHot> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemHotAdapter(List<GoodsHot> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_index_goods_hot,null);
            holder.item_index_foot_one_pic = (ImageView) convertView.findViewById(R.id.item_index_foot_one_pic);
            holder.item_index_foot_one_zhekou = (TextView) convertView.findViewById(R.id.item_index_foot_one_zhekou);
            holder.item_index_foot_one_title = (TextView) convertView.findViewById(R.id.item_index_foot_one_title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsHot typeBean = lists.get(position);
        DecimalFormat   df   =   new DecimalFormat("#####0.00");
//        System.out.println(df.format(d));
        if(typeBean != null){
            Double zhekou ;
            if(!StringUtil.isNullOrEmpty(typeBean.getDiscount())){
                zhekou = Double.valueOf(typeBean.getDiscount())*10;
            }else {
                zhekou = 1.0;
            }
            holder.item_index_foot_one_zhekou.setText(df.format(zhekou)+res.getString(R.string.zhe));
            holder.item_index_foot_one_title.setText( res.getString(R.string.money) + typeBean.getSell_price());
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + typeBean.getImg(), holder.item_index_foot_one_pic, CarSmartApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_index_foot_one_pic;
        TextView item_index_foot_one_zhekou;
        TextView item_index_foot_one_title;
    }
}
