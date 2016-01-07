package com.xiaolong.CarSmart.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.CarSmartApplication;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.KuaidiData;
import com.xiaolong.CarSmart.module.Orders;
import com.xiaolong.CarSmart.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/20.
 */
public class DetailWlActivity extends BaseActivity implements View.OnClickListener {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private ImageView back;
    private Orders orders;
    private TextView detial_order_no;
    private TextView detail_name;
    private TextView detail_address;
    private TextView detail_tel;
    private TextView detail_num;
    private TextView detail_cont;
    private TextView pay_method;
    private TextView wuliu;
    private ImageView detail_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_minecart_activity);
        orders = (Orders) getIntent().getExtras().get("orders");
        initView();
        initData();
        getData();
    }

    private void initData() {
        detial_order_no.setText( getResources().getString(R.string.dingdanhao) + orders.getOrder_no());
        detail_name.setText(orders.getAccept_name());
        detail_tel.setText(orders.getMobile());
        detail_address.setText(orders.getAddress());
//        wuliu.setText(orders.getAddress());
        detail_cont.setText( orders.getGoodsData().get(0).getName());
        detail_num.setText(orders.getGoodsData().get(0).getValue());
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + orders.getGoodsData().get(0).getGoods_image(),
                detail_pic, CarSmartApplication.options, animateFirstListener);

    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        detial_order_no = (TextView) this.findViewById(R.id.detial_order_no);
        detail_name = (TextView) this.findViewById(R.id.detail_name);
        detail_tel = (TextView) this.findViewById(R.id.detail_tel);
        detail_num = (TextView) this.findViewById(R.id.detail_num);
        detail_cont = (TextView) this.findViewById(R.id.detail_cont);
        detail_address = (TextView) this.findViewById(R.id.detail_address);
        pay_method = (TextView) this.findViewById(R.id.pay_method);
        wuliu = (TextView) this.findViewById(R.id.wuliu);
        detail_pic = (ImageView) this.findViewById(R.id.detail_pic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.DELIVERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            KuaidiData data= getGson().fromJson(s, KuaidiData.class);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DetailWlActivity.this, R.string.login_error_three, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "check");
                params.put("company", "shunfeng");
                params.put("delivery_code", "");
                params.put("delivery_code", orders.getId());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

}
