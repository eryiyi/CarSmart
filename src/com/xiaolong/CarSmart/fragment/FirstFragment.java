package com.xiaolong.CarSmart.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.*;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.GoodsHotDATA;
import com.xiaolong.CarSmart.data.SlideDATA;
import com.xiaolong.CarSmart.module.GoodsHot;
import com.xiaolong.CarSmart.module.SlidePic;
import com.xiaolong.CarSmart.module.TypeBean;
import com.xiaolong.CarSmart.ui.*;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import com.xiaolong.CarSmart.widget.TagsGridView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/30.
 */
public class FirstFragment extends BaseFragment implements View.OnClickListener,OnClickContentItemListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Resources res;

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
    //分类按钮
    private TagsGridView moreparttimetyupeGridview;
    List<TypeBean> lists = new ArrayList<TypeBean>();
    private ItemTypeBeanAdapter adapterOne;
    //最热
    TagsGridView gridView;//横向滚动
    private ItemHotAdapter adpterHot;
    private List<GoodsHot> listHots = new ArrayList<>();
    //搜索框
    private EditText search_editext;
    private ImageView top_type;
    private ImageView top_sao;
    private ImageView search_btn;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();

        initView(view);

        Resources res = getActivity().getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        initData();
        getHots();
        return view;
    }

    private void initView(View view) {
        moreparttimetyupeGridview = (TagsGridView) view.findViewById(R.id.moreparttimetyupeGridview);
        lists.add(new TypeBean(res.getString(R.string.wodeshoucang) ,R.drawable.button_selector_type_one));
        lists.add(new TypeBean(res.getString(R.string.wuliuchaxun) ,R.drawable.button_selector_type_two));
        lists.add(new TypeBean(res.getString(R.string.chongzhi) ,R.drawable.button_selector_type_three));
        lists.add(new TypeBean(res.getString(R.string.quanbu), R.drawable.button_selector_type_eight));
        lists.add(new TypeBean(res.getString(R.string.dianyingpiao) ,R.drawable.button_selector_type_five));
        lists.add(new TypeBean(res.getString(R.string.xiaojinku) ,R.drawable.button_selector_type_six));

        adapterOne = new ItemTypeBeanAdapter(lists, getActivity());
        moreparttimetyupeGridview.setAdapter(adapterOne);
        moreparttimetyupeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转
                switch (position) {
                    case 0:
                        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))) {
                            Intent favourView = new Intent(getActivity(), MineFavourActivity.class);
                            startActivity(favourView);
                        } else {
                            Intent loginVIew = new Intent(getActivity(), LoginActivity.class);
                            startActivity(loginVIew);
                        }
                        break;
                    case 1:
                        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))) {
                            Intent kdView = new Intent(getActivity(), KuaidiActivity.class);
                            startActivity(kdView);
                        } else {
                            Intent loginVIew = new Intent(getActivity(), LoginActivity.class);
                            startActivity(loginVIew);
                        }

                        break;
                    case 2:
                        Intent secondzuo = new Intent(getActivity(), SecondZuoActivity.class);
                        startActivity(secondzuo);
                        break;
                    case 3:
                        //政策新闻
                        Intent newsView = new Intent(getActivity(), NewsActivity.class);
                        startActivity(newsView);
                        break;
                    case 4:
                        //daohang
                        Intent daohang = new Intent(getActivity(), DaohangActivity.class);
                        startActivity(daohang);
                        break;
                    case 5:
                        Intent secondCar = new Intent(getActivity(), SecondCarActivity.class);
                        startActivity(secondCar);
                        break;
                }
            }
        });
        //
        search_editext = (EditText) view.findViewById(R.id.search_editext);
        search_btn = (ImageView) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
//        search_editext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!StringUtil.isNullOrEmpty(search_editext.getText().toString())) {
//                    //开始执行
//                    Intent intent = new Intent(getActivity(), SearchGoodsActivity.class);
//                    intent.putExtra("cont", search_editext.getText().toString());
//                    startActivity(intent);
//                }
//            }
//        });
        top_type = (ImageView) view.findViewById(R.id.top_type);
        top_sao = (ImageView) view.findViewById(R.id.top_sao);
        top_type.setOnClickListener(this);
        top_sao.setOnClickListener(this);
    }
    private void initViewPager() {
        //幻灯片
        adapter = new SlideViewPagerAdapter(getActivity(), listpics);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
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
    private void initData() {
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
                                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                30, 30);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(getActivity());
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

    /**
     * 获取热门商品
     */
    private void getHots() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_HOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    GoodsHotDATA data = getGson().fromJson(s, GoodsHotDATA.class);
                                    if (data.getCode() == 200) {
                                        listHots.clear();
                                        listHots = data.getData();
                                        setGridView();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){

                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "hot");
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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_type:
                //点击类别
                //调用广播，刷新主页
                Intent intent1 = new Intent("switch_type");
                getActivity().sendBroadcast(intent1);
                break;
            case R.id.top_sao:
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
                break;

            case R.id.search_btn:
            {
                if (!StringUtil.isNullOrEmpty(search_editext.getText().toString())) {
//                    //开始执行
                    Intent search = new Intent(getActivity(), SearchGoodsActivity.class);
                    search.putExtra("cont", search_editext.getText().toString());
                    startActivity(search);
                }
            }
                break;
        }
    }



    private void setGridView() {
        gridView = (TagsGridView) view.findViewById(R.id.grid);//横向滚动

//        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除点击黄色背景
        adpterHot = new ItemHotAdapter(listHots, getActivity());

        gridView.setAdapter(adpterHot);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GoodsHot good = listHots.get(position);
                Intent detailView = new Intent(getActivity(), DetailGoodsActivity.class);
                detailView.putExtra("id", good.getId());
                startActivity(detailView);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    String result = bundle.getString("result");

                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

}
