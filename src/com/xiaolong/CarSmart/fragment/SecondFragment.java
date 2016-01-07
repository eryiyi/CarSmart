package com.xiaolong.CarSmart.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.CategoryData;
import com.xiaolong.CarSmart.module.GoodsTypeBig;
import com.xiaolong.CarSmart.ui.MipcaActivityCapture;
import com.xiaolong.CarSmart.ui.SearchGoodsActivity;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/30.
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private List<GoodsTypeBig> toolsList = new ArrayList<>();
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflaters;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem=0;
    private ShopAdapter shopAdapter;

    private View viewf;
    private ProgressDialog progressDialog;
    //搜索框
    private EditText search_editext;
    private Button top_sao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewf = inflater.inflate(R.layout.second_fragment, null);
        scrollView=(ScrollView) viewf.findViewById(R.id.tools_scrlllview);
        shopAdapter=new ShopAdapter(getActivity().getSupportFragmentManager());
        inflaters=LayoutInflater.from(getActivity());
        initView();
        Resources res = getActivity().getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        //获得大类
        getBigType();
        return viewf;
    }

    private void initView() {
        top_sao = (Button) viewf.findViewById(R.id.top_sao);
        top_sao.setOnClickListener(this);
        search_editext = (EditText) viewf.findViewById(R.id.search_editext);
        search_editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!StringUtil.isNullOrEmpty(search_editext.getText().toString())){
                    //开始执行
                    Intent intent = new Intent(getActivity(), SearchGoodsActivity.class);
                    intent.putExtra("cont", search_editext.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.top_sao:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(View v) {
        LinearLayout toolsLayout=(LinearLayout) v.findViewById(R.id.tools);
        toolsTextViews=new TextView[toolsList.size()];
        views=new View[toolsList.size()];

        for (int i = 0; i < toolsList.size(); i++) {
            View view=inflaters.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView=(TextView) view.findViewById(R.id.text);
            textView.setText(toolsList.get(i).getName());
            toolsLayout.addView(view);
            toolsTextViews[i]=textView;
            views[i]=view;
        }
        changeTextColor(0);

        initPager(viewf);
    }

    private View.OnClickListener toolsItemListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };



    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager(View v) {
        shop_pager=(ViewPager) v.findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if(shop_pager.getCurrentItem()!=arg0)shop_pager.setCurrentItem(arg0);
            if(currentItem!=arg0){
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem=arg0;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * ViewPager 加载选项卡
     * @author Administrator
     *
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment =new Fragment_pro_type();
            Bundle bundle=new Bundle();
            GoodsTypeBig goodsTypeBig=toolsList.get(arg0);
            bundle.putString("typename",goodsTypeBig.getName());
            bundle.putParcelable("goodsTypeBig", goodsTypeBig);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return toolsList.size();
        }
    }


    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if(i!=id){
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    //获得类别
    private void getBigType() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CategoryData data = getGson().fromJson(s, CategoryData.class);
                                    if (data.getCode() == 200) {
                                        toolsList.addAll(data.getData());
                                        showToolsView(viewf);
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
                params.put("action", "category");
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
