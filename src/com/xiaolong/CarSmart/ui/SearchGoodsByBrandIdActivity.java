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
import com.xiaolong.CarSmart.adapter.ItemSearchGoodsByBrandAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.GoodsListDATA;
import com.xiaolong.CarSmart.library.PullToRefreshBase;
import com.xiaolong.CarSmart.library.PullToRefreshListView;
import com.xiaolong.CarSmart.module.Goods;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/2.
 * 按照分类查询商品
 */
public class SearchGoodsByBrandIdActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText top_cont;//关键词
    private ItemSearchGoodsByBrandAdapter adapter;
    private List<Goods> listgoods = new ArrayList<Goods>();

    private String brand_id;
    private String cont = "";

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;
    private PullToRefreshListView pklstv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brand_id = getIntent().getExtras().getString("brand_id");
        setContentView(R.layout.searchgoods_activity);
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(SearchGoodsByBrandIdActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        top_cont = (EditText) this.findViewById(R.id.top_cont);
        pklstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemSearchGoodsByBrandAdapter(listgoods, SearchGoodsByBrandIdActivity.this);
        pklstv.setMode(PullToRefreshBase.Mode.BOTH);
        pklstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

        pklstv.setAdapter(adapter);

        pklstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Goods good = listgoods.get(position-1);
                Intent detailView = new Intent(SearchGoodsByBrandIdActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", good.getId());
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
//                String content = top_cont.getText().toString();//要搜的内容
//                if (!StringUtil.isNullOrEmpty(content)) {
//                    getData();
//                }
            }
        });
        top_cont.setText(cont);
    }

    //查询商品
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.DAOHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    GoodsListDATA data = getGson().fromJson(s, GoodsListDATA.class);
                                    if (data.getCode() == 200) {
                                        if (IS_REFRESH) {
                                            listgoods.clear();
                                        }
                                        listgoods.addAll(data.getData());
                                        pklstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(SearchGoodsByBrandIdActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }else {
                            Toast.makeText(SearchGoodsByBrandIdActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SearchGoodsByBrandIdActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "goods");
                params.put("category_id", brand_id);
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
