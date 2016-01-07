package com.xiaolong.CarSmart.ui;

import android.os.Bundle;
import android.view.View;
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
import com.xiaolong.CarSmart.module.GoodsDat;
import com.xiaolong.CarSmart.module.Order;
import com.xiaolong.CarSmart.module.Orders;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 */
public class publishCommentActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private Orders ordersTmp;
    private GoodsDat goodsDat;
    private EditText cont;
    private RatingBar rat;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersTmp = (Orders) getIntent().getExtras().get("ordersTmp");
        goodsDat = (GoodsDat) getIntent().getExtras().get("goodsDat");
        setContentView(R.layout.publish_comment_activity);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        cont = (EditText) this.findViewById(R.id.cont);
        sure = (Button) this.findViewById(R.id.sure);
        rat = (RatingBar) this.findViewById(R.id.rat);
        sure.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                //提交
                if(StringUtil.isNullOrEmpty(cont.getText().toString())){
                    Toast.makeText(publishCommentActivity.this, getResources().getString(R.string.qingshurupinglunneirong), Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COMMENT_URL,
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
                                        Toast.makeText(publishCommentActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(publishCommentActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(publishCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(publishCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "comment");
                params.put("goods_id", goodsDat.getGoods_id());
                params.put("order_no", ordersTmp.getOrder_no());
                params.put("contents", cont.getText().toString());
                params.put("point", String.valueOf(rat.getRating()));
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
