package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemSecondOneAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.SecondTypeDATA;
import com.xiaolong.CarSmart.library.PullToRefreshBase;
import com.xiaolong.CarSmart.library.PullToRefreshListView;
import com.xiaolong.CarSmart.module.SecondType;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/18.
 * 在制作
 */
public class SecondZuoActivity extends BaseActivity implements View.OnClickListener {
    private List<SecondType> listgoods = new ArrayList<SecondType>();
    private ImageView back;
    private EditText top_cont;//关键词
    private ItemSecondOneAdapter adapter;
    private PullToRefreshListView lstv;

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;
    private ProgressDialog progressDialog;

    private String cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondcar_activity);
        initView();
        Resources res =getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(SecondZuoActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        getData();

    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        top_cont = (EditText) this.findViewById(R.id.top_cont);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemSecondOneAdapter(listgoods, SecondZuoActivity.this);
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

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SecondType good = listgoods.get(position - 1);
                Intent detailView = new Intent(SecondZuoActivity.this, SearchGoodsByCategoryActivity.class);
                detailView.putExtra("category_id", good.getId());
                startActivity(detailView);
            }
        });


        top_cont.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = top_cont.getText().toString();//要搜的内容
                if (!StringUtil.isNullOrEmpty(content)) {

                    getData();
                }
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

    //获得类别
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SecondTypeDATA data = getGson().fromJson(s, SecondTypeDATA.class);
                                    if (data.getCode() == 200) {
                                        if (IS_REFRESH) {
                                            listgoods.clear();
                                        }
                                        listgoods.addAll(data.getData());
                                        lstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(SecondZuoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }  else {
                        Toast.makeText(SecondZuoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SecondZuoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "categoryID");
                params.put("category_id", "255");
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
