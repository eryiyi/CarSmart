package com.xiaolong.CarSmart.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.xiaolong.CarSmart.module.News;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/3.
 * 新闻详情页
 */
public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView news_detail_cont;
    private TextView news_detail_title;
    private TextView news_detail_dateline;
    private News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = (News) getIntent().getExtras().get("news");
        setContentView(R.layout.news_detail_activity);
        initView();
//        getData();
    }

    private void initView() {
        news_detail_cont = (TextView) this.findViewById(R.id.news_detail_cont);
        news_detail_dateline = (TextView) this.findViewById(R.id.news_detail_dateline);
        news_detail_title = (TextView) this.findViewById(R.id.news_detail_title);
        back = (ImageView) this.findViewById(R.id.back);

        back.setOnClickListener(this);

        news_detail_cont.setText(news.getContent());
        news_detail_title.setText(news.getTitle());
        news_detail_dateline.setText(news.getCreate_time());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    //获得信息
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_NEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    NewDATA data = getGson().fromJson(s, NewDATA.class);
                                    if (data.getCode() == 200)
                                    {
                                        Toast.makeText(NewsDetailActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(NewsDetailActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(NewsDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(NewsDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "detail");
                params.put("id", news.getId());
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
