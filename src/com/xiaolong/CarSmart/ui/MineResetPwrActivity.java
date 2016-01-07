package com.xiaolong.CarSmart.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.xiaolong.CarSmart.data.NewDATA;
import com.xiaolong.CarSmart.data.SuccessDATA;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 */
public class MineResetPwrActivity extends BaseActivity implements View.OnClickListener {
    private EditText pwr_one;
    private EditText pwr_two;
    private EditText pwr_three;
    private ImageView back;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_resetpwr_activity);
        initView();
    }

    private void initView() {
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        pwr_three = (EditText) this.findViewById(R.id.pwr_three);
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                //确定
                if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
                    Toast.makeText(MineResetPwrActivity.this, R.string.qingshuruyuanmima,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
                    Toast.makeText(MineResetPwrActivity.this, R.string.qingshuruxinmima,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr_three.getText().toString())){
                    Toast.makeText(MineResetPwrActivity.this, R.string.qingzaicishuruxinmima,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwr_two.getText().toString().equals(pwr_three.getText().toString())){
                    Toast.makeText(MineResetPwrActivity.this, R.string.qingzaicishuruxinmima_two,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwr_one.getText().toString().equals(pwr_three.getText().toString())){
                    Toast.makeText(MineResetPwrActivity.this, R.string.qingzaicishuruxinmima_three,Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.RESET_PWR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                    if (data.getCode() == 200)
                                    {
                                        save(Constants.PASSWORD, pwr_two.getText().toString());
                                        Toast.makeText(MineResetPwrActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(MineResetPwrActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }else if(Integer.parseInt(code1) == -1){
                                    Toast.makeText(MineResetPwrActivity.this, R.string.yonghubucunzai, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == -2){
                                    Toast.makeText(MineResetPwrActivity.this, R.string.old_pwr_is_erroe, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == -3){
                                    Toast.makeText(MineResetPwrActivity.this, R.string.qingzaicishuruxinmima_two, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == -4){
                                    Toast.makeText(MineResetPwrActivity.this, R.string.wangluocuowu, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == -5){
                                    Toast.makeText(MineResetPwrActivity.this, R.string.canshucuowu, Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(MineResetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineResetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "change");
                params.put("old_password", pwr_one.getText().toString());
                params.put("new_password", pwr_two.getText().toString());
                params.put("new_rep_password", pwr_three.getText().toString());
                params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
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
