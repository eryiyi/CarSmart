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
import com.xiaolong.CarSmart.module.KuaidiModule;
import com.xiaolong.CarSmart.module.PayMethod;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/6
 * Time: 14:06
 * 支付方式
 */
public class ItemKuaidiMethodAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<KuaidiModule> lists;
    private Context mContext;
    Resources res;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemKuaidiMethodAdapter(List<KuaidiModule> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
        res = mContext.getResources();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_kuaidi_method_adapter, null);
//            holder.item_nickname = (ImageView) convertView.findViewById(R.id.item_nickname);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final KuaidiModule cell = lists.get(position);
        if (cell != null) {
            holder.item_title.setText(cell.getTitle());
            holder.item_cont.setText(cell.getCont());
//            imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getLogo(), holder.item_nickname, CarSmartApplication.txOptions, animateFirstListener);
        }

        return convertView;
    }

    class ViewHolder {
//        ImageView item_nickname;
        TextView item_title;
        TextView item_cont;
    }

}