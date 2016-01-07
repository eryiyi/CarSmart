package com.xiaolong.CarSmart.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.adapter.SlideViewPagerAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.SlideDATA;
import com.xiaolong.CarSmart.fragment.NewsRecordFragment;
import com.xiaolong.CarSmart.fragment.NewsTopFragment;
import com.xiaolong.CarSmart.fragment.NewsTypeFragment;
import com.xiaolong.CarSmart.fragment.NewsZcFragment;
import com.xiaolong.CarSmart.module.SlidePic;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/3.
 * 政策新闻
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;
    private NewsTopFragment newsTopFragment;
    private NewsRecordFragment newsRecordFragment;
    private NewsTypeFragment newsTypeFragment;
    private NewsZcFragment newsZcFragment;

    private ImageView back;//返回

    private TextView news_new;
    private TextView news_record;
    private TextView news_type;
    private TextView news_zc;

    private TextView one_img;
    private TextView two_img;
    private TextView three_img;
    private TextView four_img;

    private FrameLayout content_frame;

    //幻灯片
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private SlidePic slidePic;
    private List<String> listpics = new ArrayList<>();
    private ViewPager viewpager;
    private LinearLayout viewGroup;
    private SlideViewPagerAdapter adapter;
    View view;
    private ProgressDialog progressDialog;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.news_activity);
        fm = getSupportFragmentManager();
        initView();
        switchFragment(R.id.news_new);

        Resources res =getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(NewsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        getSlide();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        news_new = (TextView) this.findViewById(R.id.news_new);
        news_record = (TextView) this.findViewById(R.id.news_record);
        news_type = (TextView) this.findViewById(R.id.news_type);
        news_zc = (TextView) this.findViewById(R.id.news_zc);

        one_img = (TextView) this.findViewById(R.id.one_img);
        two_img = (TextView) this.findViewById(R.id.two_img);
        three_img = (TextView) this.findViewById(R.id.three_img);
        four_img = (TextView) this.findViewById(R.id.four_img);

        content_frame = (FrameLayout) this.findViewById(R.id.content_frame);


        back.setOnClickListener(this);
        news_zc.setOnClickListener(this);
        news_new.setOnClickListener(this);
        news_record.setOnClickListener(this);
        news_type.setOnClickListener(this);
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.news_new:
                if (newsTopFragment == null) {
                    newsTopFragment = new NewsTopFragment();
                    fragmentTransaction.add(R.id.content_frame, newsTopFragment);
                } else {
                    fragmentTransaction.show(newsTopFragment);
                }
                one_img.setBackground(res.getDrawable(R.drawable.red_line));
                two_img.setBackground(res.getDrawable(R.drawable.gray_line));
                three_img.setBackground(res.getDrawable(R.drawable.gray_line));
                four_img.setBackground(res.getDrawable(R.drawable.gray_line));

                break;
            case R.id.news_record:
                if (newsRecordFragment == null) {
                    newsRecordFragment = new NewsRecordFragment();
                    fragmentTransaction.add(R.id.content_frame, newsRecordFragment);
                } else {
                    fragmentTransaction.show(newsRecordFragment);
                }
                one_img.setBackground(res.getDrawable(R.drawable.gray_line));
                two_img.setBackground(res.getDrawable(R.drawable.red_line));
                three_img.setBackground(res.getDrawable(R.drawable.gray_line));
                four_img.setBackground(res.getDrawable(R.drawable.gray_line));
                break;
            case R.id.news_type:
                if (newsTypeFragment == null) {
                    newsTypeFragment = new NewsTypeFragment();
                    fragmentTransaction.add(R.id.content_frame, newsTypeFragment);
                } else {
                    fragmentTransaction.show(newsTypeFragment);
                }
                one_img.setBackground(res.getDrawable(R.drawable.gray_line));
                two_img.setBackground(res.getDrawable(R.drawable.gray_line));
                three_img.setBackground(res.getDrawable(R.drawable.red_line));
                four_img.setBackground(res.getDrawable(R.drawable.gray_line));
                break;
            case R.id.news_zc:
                if (newsZcFragment == null) {
                    newsZcFragment = new NewsZcFragment();
                    fragmentTransaction.add(R.id.content_frame, newsZcFragment);
                } else {
                    fragmentTransaction.show(newsZcFragment);
                }
                one_img.setBackground(res.getDrawable(R.drawable.gray_line));
                two_img.setBackground(res.getDrawable(R.drawable.gray_line));
                three_img.setBackground(res.getDrawable(R.drawable.gray_line));
                four_img.setBackground(res.getDrawable(R.drawable.red_line));
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (newsTopFragment != null) {
            ft.hide(newsTopFragment);
        }
        if (newsRecordFragment != null) {
            ft.hide(newsRecordFragment);
        }
        if (newsTypeFragment != null) {
            ft.hide(newsTypeFragment);
        }
        if (newsZcFragment != null) {
            ft.hide(newsZcFragment);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==  R.id.back){
            finish();
            return;
        }else {
            switchFragment(v.getId());
        }
    }

    private void initViewPager() {
        //幻灯片
        adapter = new SlideViewPagerAdapter(NewsActivity.this,listpics);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) this.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }

    /**
     * 获取幻灯片
     */
    private void getSlide() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SLIDENEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    SlideDATA data = getGson().fromJson(s, SlideDATA.class);
                                    if (data.getCode() == 200) {
                                        slidePic = data.getData();
                                        if(slidePic != null){
                                            listpics.add(slidePic.getUrl1());
                                            listpics.add(slidePic.getUrl2());
                                            listpics.add(slidePic.getUrl3());
                                        }
                                        initViewPager();
                                    } else {
                                        Toast.makeText(NewsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(NewsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NewsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "show");
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

    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) this.findViewById(R.id.viewGroup);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                30, 30);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(NewsActivity.this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapter.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1:
                //说明点击的是viewpage
                break;
        }
    }

}
