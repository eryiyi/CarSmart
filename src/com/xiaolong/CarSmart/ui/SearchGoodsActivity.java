package com.xiaolong.CarSmart.ui;

import android.content.Intent;
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
import com.xiaolong.CarSmart.adapter.ItemSearchGoodsAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.SearchGoodsDATA;
import com.xiaolong.CarSmart.library.PullToRefreshBase;
import com.xiaolong.CarSmart.library.PullToRefreshListView;
import com.xiaolong.CarSmart.module.ProSearch;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/2.
 * 查询商品
 */
public class SearchGoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText top_cont;//关键词
    private ItemSearchGoodsAdapter adapter;
    private List<ProSearch> listgoods = new ArrayList<>();
    private PullToRefreshListView lstv;

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;

    private String cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cont = getIntent().getExtras().getString("cont");
        setContentView(R.layout.searchgoods_activity);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        top_cont = (EditText) this.findViewById(R.id.top_cont);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemSearchGoodsAdapter(listgoods, SearchGoodsActivity.this);
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
                ProSearch good = listgoods.get(position-1);
                Intent detailView = new Intent(SearchGoodsActivity.this, DetailGoodsActivity.class);
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
                String content = top_cont.getText().toString();//要搜的内容
                if (!StringUtil.isNullOrEmpty(content)) {

                    getData();
                }
            }
        });
        top_cont.setText(cont);
    }

    //查询商品-按照关键词
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_FIND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SearchGoodsDATA data = getGson().fromJson(s, SearchGoodsDATA.class);
                                    if (data.getCode() == 200) {
                                        if (IS_REFRESH) {
                                            listgoods.clear();
                                        }
                                        listgoods.addAll(data.getData().getGoods());
                                        lstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(SearchGoodsActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SearchGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "search");
                params.put("search_name", top_cont.getText().toString());
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
