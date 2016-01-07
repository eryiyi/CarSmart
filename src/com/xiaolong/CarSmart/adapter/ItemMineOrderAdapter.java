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
import com.xiaolong.CarSmart.module.GoodsDat;
import com.xiaolong.CarSmart.module.GoodsHot;
import com.xiaolong.CarSmart.module.Orders;
import com.xiaolong.CarSmart.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemMineOrderAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodsDat> lists;
    private Context mContect;
    Resources res;

    private OnClickContentItemListener onClickContentItemListener;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }
    public ItemMineOrderAdapter(List<GoodsDat> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_detail_order,null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_select = (ImageView) convertView.findViewById(R.id.item_select);
            holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
            holder.item_prices = (TextView) convertView.findViewById(R.id.item_prices);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsDat typeBean = lists.get(position);
        if(typeBean != null){
            holder.item_content.setText(typeBean.getName());
            holder.item_prices.setText(res.getString(R.string.money) + typeBean.getGoods_price());
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + typeBean.getGoods_image(), holder.item_pic, CarSmartApplication.options, animateFirstListener);
            if("1".equals(typeBean.getIsSelect())){
                holder.item_select.setImageResource(R.drawable.select_one);
            }else {
                holder.item_select.setImageResource(R.drawable.select_two);
            }
            holder.item_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 1, typeBean);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_pic;
        ImageView item_select;
        TextView item_content;
        TextView item_prices;
    }
}
