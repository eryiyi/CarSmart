package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.xiaolong.CarSmart.adapter.ItemKuaidiMethodAdapter;
import com.xiaolong.CarSmart.adapter.ItemPayMethodAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.PayMethodData;
import com.xiaolong.CarSmart.module.KuaidiModule;
import com.xiaolong.CarSmart.module.PayMethod;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 * 快递
 */
public class SelectKuaidiMethodActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ListView lstv;
    private ItemKuaidiMethodAdapter adapter;
    private List<KuaidiModule> lists  = new ArrayList<KuaidiModule>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_kuaidi_method_activity);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        lstv = (ListView) this.findViewById(R.id.lstv);
        back.setOnClickListener(this);
        lists.add(new KuaidiModule(getResources().getString(R.string.kuaidi), getResources().getString(R.string.kuaidi_one)));
        lists.add(new KuaidiModule(getResources().getString(R.string.kuaidi_two), getResources().getString(R.string.kuaidi_one)));
        adapter = new ItemKuaidiMethodAdapter(lists, SelectKuaidiMethodActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                KuaidiModule kuaidiModule = lists.get(i);
                Intent updateAddressView = new Intent(SelectKuaidiMethodActivity.this, OrderMakeActivity.class);
                updateAddressView.putExtra("kuaidiModule",kuaidiModule);
                setResult(004, updateAddressView);
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




}
