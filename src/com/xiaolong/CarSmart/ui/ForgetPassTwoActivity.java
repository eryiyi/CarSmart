package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.SuccessDATA;
import com.xiaolong.CarSmart.util.HttpUtils;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31.
 * 找回密码2
 */
public class ForgetPassTwoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText sureword;
    private EditText password;

    private Button next;
    private TextView quest;

    boolean isMobileNet, isWifiNet;
    private ProgressDialog progressDialog;

    private String mobile_str;
    private String password_str;
    private String password_str_two;
    private String card_str;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile_str = getIntent().getExtras().getString("mobile_str");
        card_str = getIntent().getExtras().getString("card_str");
        setContentView(R.layout.forget_pass_two_activity);
        res = getResources();
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);

        password = (EditText) this.findViewById(R.id.password);
        sureword = (EditText) this.findViewById(R.id.sureword);
        next = (Button) this.findViewById(R.id.next);
        quest = (TextView) this.findViewById(R.id.quest);

        back.setOnClickListener(this);
        next.setOnClickListener(this);
        quest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;

                case R.id.next:
                    //下一步
                    try {
                        isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
                        isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
                        if (!isMobileNet && !isWifiNet) {
                            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    password_str = sureword.getText().toString();
                    password_str_two = password.getText().toString();
                    if(StringUtil.isNullOrEmpty(password_str)){
                        Toast.makeText(this, R.string.reg_error_three, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(password_str_two)){
                        Toast.makeText(this, R.string.forget_error_one, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!password_str.equals(password_str_two)){
                        Toast.makeText(this, R.string.forget_error_two, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reset();
                    break;
                case R.id.quest:
                    //问题
                    break;
            }
    }



    private void reset() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.FORGET_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            hiddenKeyBoard(sureword);
                            hiddenKeyBoard(password);
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                    if (data.getCode() == 200) {
                                        Toast.makeText(ForgetPassTwoActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(ForgetPassTwoActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ForgetPassTwoActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPassTwoActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "forget");
                params.put("username", mobile_str);
                params.put("password",  StringUtil.md5(password_str));
                params.put("check_code", card_str);
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

    public void hiddenKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

}
