package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.xiaolong.CarSmart.data.CardDATA;
import com.xiaolong.CarSmart.data.SuccessDATA;
import com.xiaolong.CarSmart.util.HttpUtils;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31.
 * 找回密码1
 */
public class ForgetPassOneActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText mobile;
//    private EditText password;
    private EditText card;
    private Button button_djs;
    private Button next;
    private TextView quest;

    boolean isMobileNet, isWifiNet;
    private ProgressDialog progressDialog;

    private String mobile_str;
//    private String password_str;
    private String card_str;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass_one_activity);
        res = getResources();
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        card = (EditText) this.findViewById(R.id.card);
        mobile = (EditText) this.findViewById(R.id.mobile);
//        password = (EditText) this.findViewById(R.id.password);
        button_djs = (Button) this.findViewById(R.id.button_djs);
        next = (Button) this.findViewById(R.id.next);
        quest = (TextView) this.findViewById(R.id.quest);


        back.setOnClickListener(this);
        button_djs.setOnClickListener(this);
        next.setOnClickListener(this);
        quest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.button_djs:
                    //点击获得验证码
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
                    mobile_str = mobile.getText().toString();
                    if(StringUtil.isNullOrEmpty(mobile_str)){
                        Toast.makeText(this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    button_djs.setClickable(false);//不可点击
                    MyTimer myTimer = new MyTimer(60000,1000);
                    myTimer.start();
                    getCard();
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
                    mobile_str = mobile.getText().toString();
                    card_str = card.getText().toString();
                    if(StringUtil.isNullOrEmpty(mobile_str)){
                        Toast.makeText(this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(card_str)){
                        Toast.makeText(this, R.string.reg_error_five, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getReg();
                    break;
                case R.id.quest:
                    //问题
                    break;
            }
    }

    //获得验证码
    private void getCard() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.FORGET_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        hiddenKeyBoard(mobile);
                        hiddenKeyBoard(card);
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CardDATA data = getGson().fromJson(s, CardDATA.class);
                                    if (data.getCode() == 200) {
                                        Toast.makeText(ForgetPassOneActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(ForgetPassOneActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ForgetPassOneActivity.this, R.string.reg_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPassOneActivity.this, R.string.reg_error_two, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "send");
                params.put("username", mobile_str);
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


    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            button_djs.setText(res.getString(R.string.daojishi_three));
            button_djs.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            button_djs.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }


    //校验验证码
    private void getReg() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.FORGET_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        hiddenKeyBoard(mobile);
                        hiddenKeyBoard(card);
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                    if (data.getCode() == 200) {
                                        Toast.makeText(ForgetPassOneActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForgetPassOneActivity.this, ForgetPassTwoActivity.class);
                                        intent.putExtra("mobile_str", mobile_str);
                                        intent.putExtra("card_str", card_str);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(ForgetPassOneActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ForgetPassOneActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPassOneActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "check");
                params.put("username", mobile_str);
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
