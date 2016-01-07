package com.xiaolong.CarSmart.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.adapter.ItemMineOrderAdapter;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.module.GoodsDat;
import com.xiaolong.CarSmart.module.Orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/15.
 * 订单详情
 */
public class DetailOrderActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
    private Orders orderVo;//传递过来的值
    private ImageView back;
    private TextView order_status;

    //收货地址
    private TextView order_name;
    private TextView order_tel;
    private TextView order_location;
    private TextView item_money;
    private TextView item_count;

    //功能按钮
    private Button button_one;
    private Button button_two;
    private ItemMineOrderAdapter adapter;
    List<GoodsDat> lists= new ArrayList<>();

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ListView lstv;
    private List<GoodsDat> listTmp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_order_activity);
        orderVo = (Orders) getIntent().getExtras().get("Orders");
        lists = orderVo.getGoodsData();
        initView();
        //填充数据
        initData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        order_status = (TextView) this.findViewById(R.id.order_status);
        order_name = (TextView) this.findViewById(R.id.order_name);
        order_tel = (TextView) this.findViewById(R.id.order_tel);
        order_location = (TextView) this.findViewById(R.id.order_location);
        item_money = (TextView) this.findViewById(R.id.item_money);
        item_count = (TextView) this.findViewById(R.id.item_count);
        button_one = (Button) this.findViewById(R.id.button_one);
        button_two = (Button) this.findViewById(R.id.button_two);
        lstv = (ListView) this.findViewById(R.id.lstv);

        button_one.setOnClickListener(this);
        button_two.setOnClickListener(this);

        button_one.setVisibility(View.GONE);
        button_two.setVisibility(View.GONE);
        back.setOnClickListener(this);
        adapter = new ItemMineOrderAdapter(lists, DetailOrderActivity.this);
        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.item_head:{

            }
                break;
            case R.id.item_nickname:
            {


            }
                break;
            case R.id.button_one:
            {
                listTmp.clear();
                //去评价
                if(lists!=null){
                    for(GoodsDat goodsDat:lists){
                        if("1".equals(goodsDat.getIsSelect())){
                            listTmp.add(goodsDat);
                        }
                    }
                }
                if(listTmp!=null && listTmp.size()>0){
                    if(listTmp.size() > 1){
                        Toast.makeText(DetailOrderActivity.this, R.string.zhinegnxuanzhong , Toast.LENGTH_SHORT).show();
                    }else {
                        Intent commentView =  new Intent(DetailOrderActivity.this, publishCommentActivity.class);
                        commentView.putExtra("goodsDat",listTmp.get(0));//订单里面的某一项
                        commentView.putExtra("ordersTmp",orderVo);//订单
                        startActivity(commentView);
                    }
                }else {
                    Toast.makeText(DetailOrderActivity.this, R.string.qingxianxuanzeshuyju, Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case R.id.button_two:
            {
                listTmp.clear();
                if(lists!=null){
                    for(GoodsDat goodsDat:lists){
                        if("1".equals(goodsDat.getIsSelect())){
                            listTmp.add(goodsDat);
                        }
                    }
                }
                if(listTmp!=null && listTmp.size()>0){
                    if(listTmp.size() > 1){
                        Toast.makeText(DetailOrderActivity.this, R.string.zhinegnxuanzhong, Toast.LENGTH_SHORT).show();
                    }else {
                        Intent commentView =  new Intent(DetailOrderActivity.this, GoBackActivity.class);
                        commentView.putExtra("goodsDat",listTmp.get(0));//订单里面的某一项
                        commentView.putExtra("ordersTmp",orderVo);//订单
                        startActivity(commentView);
                    }
                }else {
                    Toast.makeText(DetailOrderActivity.this, R.string.qingxianxuanzeshuyju, Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    void initData(){
        switch (Integer.parseInt(orderVo.getStatus())){
            // `status`  '订单状态 1生成订单,2支付订单,3取消订单,4作废订单,5完成订单',
            case 1:
                order_status.setText(R.string.shegnchengdingdan);
                break;
            case 2:
                order_status.setText(R.string.zhifudiingdan);
                break;
            case 3:
                order_status.setText(R.string.quxiaodingdan);
                break;
            case 4:
                order_status.setText(R.string.zuoifeidingdan);
                break;
            case 5:
                order_status.setText(R.string.wanchegndingdan);
                button_one.setVisibility(View.VISIBLE);
                button_two.setVisibility(View.VISIBLE);
                break;
        }
        order_name.setText(orderVo.getAccept_name());
        order_tel.setText(orderVo.getTelphone());
        order_location.setText(orderVo.getAddress());
        item_count.setText(String.format(getResources().getString(R.string.item_count_adapter), orderVo.getGoodsData().size()));
        item_money.setText(String.format(getResources().getString(R.string.item_money_adapter), Double.valueOf(orderVo.getReal_amount())));
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                if("1".equals(lists.get(position).getIsSelect())){//如果是选中的，把他弄成没选中的
                    //是选中的
                    lists.get(position).setIsSelect("0");
                    adapter.notifyDataSetChanged();
                }else {
                    //没选中
                    lists.get(position).setIsSelect("1");
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

}
