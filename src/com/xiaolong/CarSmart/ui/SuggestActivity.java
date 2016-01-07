package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.SuccessDATA;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/19.
 */
public class SuggestActivity extends BaseActivity implements View.OnClickListener {
    private EditText publish_good_title;
    private EditText publish_good_cont;

    private ProgressDialog progressDialog;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_activity);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        publish_good_title = (EditText) this.findViewById(R.id.publish_good_title);
        publish_good_cont = (EditText) this.findViewById(R.id.publish_good_cont);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    public void save(View view){
        //保存
        if(StringUtil.isNullOrEmpty(publish_good_title.getText().toString())){
            Toast.makeText(SuggestActivity.this, getResources().getString(R.string.biaotibunengweikong), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(publish_good_cont.getText().toString())){
            Toast.makeText(SuggestActivity.this, getResources().getString(R.string.neirongbunegnweikong), Toast.LENGTH_SHORT).show();
            return;
        }
        Resources res =this.getBaseContext().getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        saveData();
    }
    void saveData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_SUGGEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(SuggestActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(SuggestActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(SuggestActivity.this, R.string.save_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "issue");
                params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
                params.put("title", publish_good_title.getText().toString());
                params.put("content",publish_good_cont.getText().toString());
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
