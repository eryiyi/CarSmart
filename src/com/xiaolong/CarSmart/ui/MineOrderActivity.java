package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemMineOrderSjAdapter;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.OrdersDATA;
import com.xiaolong.CarSmart.library.PullToRefreshBase;
import com.xiaolong.CarSmart.library.PullToRefreshListView;
import com.xiaolong.CarSmart.module.Orders;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MineOrderActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ImageView back;
    private PullToRefreshListView lstv;
    private ItemMineOrderSjAdapter adapter;
    private List<Orders> lists = new ArrayList<>();

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;

    private ProgressDialog progressDialog;

    Orders ordersTmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_order_activity);
        initView();
        Resources res =getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(MineOrderActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);

        adapter = new ItemMineOrderSjAdapter(lists, MineOrderActivity.this);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });

        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Orders orders = lists.get(position-1);
                Intent detailView = new Intent(MineOrderActivity.this, DetailOrderActivity.class);
                detailView.putExtra("Orders", orders);
                startActivity(detailView);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;

        }
    }

    //获得订单列表
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ORDERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    OrdersDATA data = getGson().fromJson(s, OrdersDATA.class);
                                    if (data.getCode() == 200)
                                    {
                                        if (IS_REFRESH) {
                                            lists.clear();
                                        }
                                        lists.addAll(data.getData());
                                        lstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(MineOrderActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(MineOrderActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MineOrderActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MineOrderActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "order");
                params.put("username",getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class ));
                params.put("page_index", String.valueOf(pageIndex));
                params.put("page_size", "10");
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


    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        ordersTmp = lists.get(position);
        switch (flag){
            case 1:
                //去评价
                if(!"5".equals(ordersTmp.getStatus())){
                    Toast.makeText(MineOrderActivity.this, R.string.dingdanweiwanchegn, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent commentView = new Intent(MineOrderActivity.this, publishCommentActivity.class);
                commentView.putExtra("ordersTmp",ordersTmp);
                startActivity(commentView);
                break;
            case 2:
                //去退款
                break;
        }
    }
}
