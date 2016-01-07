package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemMineAddressAdapter;
import com.xiaolong.CarSmart.adapter.ItemPayMethodAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.PayMethodData;
import com.xiaolong.CarSmart.data.ShoppingAddressDATA;
import com.xiaolong.CarSmart.module.PayMethod;
import com.xiaolong.CarSmart.module.ShoppingAddress;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 * zhifu fangshi
 */
public class SelectPayMethodActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ListView lstv;
    private ItemPayMethodAdapter adapter;
    private List<PayMethod> lists  = new ArrayList<PayMethod>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pay_method_activity);
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(SelectPayMethodActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        lstv = (ListView) this.findViewById(R.id.lstv);
        back.setOnClickListener(this);
        adapter = new ItemPayMethodAdapter(lists, SelectPayMethodActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PayMethod payMethod = lists.get(i);
                Intent updateAddressView = new Intent(SelectPayMethodActivity.this, OrderMakeActivity.class);
                updateAddressView.putExtra("payMethod",payMethod);
                setResult(003, updateAddressView);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SET_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    PayMethodData data = getGson().fromJson(s, PayMethodData.class);
                                    if (data.getCode() == 200) {
                                        lists.clear();
                                        lists.addAll(data.getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(SelectPayMethodActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(SelectPayMethodActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectPayMethodActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "pay");
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
