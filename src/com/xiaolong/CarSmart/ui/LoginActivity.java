package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.HttpUtils;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31.
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;//返回
    private ImageView head;//头像
    private EditText username;
    private EditText password;
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView findpwr;//找回密码
    private ImageView switch_open_close;//开关
    private int tmpId = 0;
    Resources re;

    boolean isMobileNet, isWifiNet;
    private ProgressDialog progressDialog;
    private String mobile_str;
    private String password_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        re = getResources();
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        head = (ImageView) this.findViewById(R.id.head);
        username = (EditText) this.findViewById(R.id.username);
        password = (EditText) this.findViewById(R.id.password);
        login = (Button) this.findViewById(R.id.login);
        register = (TextView) this.findViewById(R.id.register);
        findpwr = (TextView) this.findViewById(R.id.findpwr);
        switch_open_close = (ImageView) this.findViewById(R.id.switch_open_close);


        login.setOnClickListener(this);
        back.setOnClickListener(this);
        head.setOnClickListener(this);
        register.setOnClickListener(this);
        findpwr.setOnClickListener(this);
        switch_open_close.setOnClickListener(this);

        if (username != null) {
            mobile_str = getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class);
            username.setText(mobile_str);
        }
        if (password != null) {
            password_str = getGson().fromJson(getSp().getString(Constants.PASSWORD, ""), String.class);
            password.setText(password_str);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.head:
                //头像点击
                break;
            case R.id.login:
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
                mobile_str = username.getText().toString();
                password_str = password.getText().toString();
                //登陆点击
                if(StringUtil.isNullOrEmpty(mobile_str)){
                    Toast.makeText(LoginActivity.this, R.string.login_error_one,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(password_str)){
                    Toast.makeText(LoginActivity.this, R.string.login_error_two,Toast.LENGTH_LONG).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.is_dengluing).toString();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                login();
                break;
            case R.id.findpwr:
                //找回密码
                Intent forgetView = new Intent(LoginActivity.this, ForgetPassOneActivity.class);
                startActivity(forgetView);
                break;
            case R.id.register:
                //注册
                Intent regView = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(regView);
                break;
            case R.id.switch_open_close:
                if(tmpId == 0){
                    switch_open_close.setImageDrawable(re.getDrawable(R.drawable.switch_close));
                    tmpId =1;
                    //密码不可见
                    //设置密码为隐藏的
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    switch_open_close.setImageDrawable(re.getDrawable(R.drawable.switch_open));
                    tmpId =0;
                    //显示密码
                    //设置EditText的密码为可见的
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }
    //登录
    private void login() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        hiddenKeyBoard(username);
                        hiddenKeyBoard(password);
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                    if (data.getCode() == 200) {
                                        Toast.makeText(LoginActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                        //去广播
                                        Intent intent = new Intent("login_success");
                                        LoginActivity.this.sendBroadcast(intent);
                                        save(Constants.MOBILE, mobile_str);
                                        save(Constants.PASSWORD, password_str);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }else if(Integer.parseInt(code1)  == -1){
                                    Toast.makeText(LoginActivity.this, R.string.login_error_one_one, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1)  == -2){
                                    Toast.makeText(LoginActivity.this, R.string.login_error_one_two, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1)  == -3){
                                    Toast.makeText(LoginActivity.this, R.string.login_error_one_three, Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(LoginActivity.this, R.string.login_error_three, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, R.string.login_error_three, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "login");
                params.put("username", mobile_str);
                params.put("password", StringUtil.md5(password_str));
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
