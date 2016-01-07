package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.CarSmartApplication;
import com.xiaolong.CarSmart.MainActivity;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.ActivityTack;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.dao.DBHelper;
import com.xiaolong.CarSmart.dao.ShoppingCart;
import com.xiaolong.CarSmart.data.FavourDATA;
import com.xiaolong.CarSmart.data.GoodsDATA;
import com.xiaolong.CarSmart.module.Goods;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.DateUtil;
import com.xiaolong.CarSmart.util.StringUtil;
import com.xiaolong.CarSmart.util.URLImageParser;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
public class DetailGoodsActivity extends BaseActivity implements View.OnClickListener {

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ImageView back;
    private ImageView goods_pic;
    private TextView goods_title;
    private TextView goods_money;
    private TextView goods_money_market;
    private TextView number;
    private TextView comment_count;
    private TextView cont;
    private TextView select_num;
    private TextView goods_favour;
    private TextView goods_cart;
    private TextView add_cart;
    Resources res;
    Drawable img_one;
    private ProgressDialog progressDialog;
    public static DisplayMetrics displayMetrics;
    private String id;
    private Goods goods;
    private String htmlContent;//内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        displayMetrics=getApplicationContext().getResources().getDisplayMetrics();
        img_one = res.getDrawable(R.drawable.favour1);
        img_one.setBounds(0, 0, img_one.getMinimumWidth(), img_one.getMinimumHeight());
        setContentView(R.layout.detailgoods_activity);
        id = getIntent().getExtras().getString("id");
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(DetailGoodsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
        getCartNum();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        goods_pic = (ImageView) this.findViewById(R.id.goods_pic);
        goods_title = (TextView) this.findViewById(R.id.goods_title);
        goods_money = (TextView) this.findViewById(R.id.goods_money);
        goods_money_market = (TextView) this.findViewById(R.id.goods_money_market);
        select_num = (TextView) this.findViewById(R.id.select_num);
        comment_count = (TextView) this.findViewById(R.id.comment_count);
        cont = (TextView) this.findViewById(R.id.cont);
        goods_favour = (TextView) this.findViewById(R.id.goods_favour);
        goods_cart = (TextView) this.findViewById(R.id.goods_cart);
        number = (TextView) this.findViewById(R.id.number);
        add_cart = (TextView) this.findViewById(R.id.add_cart);

        back.setOnClickListener(this);
        goods_favour.setOnClickListener(this);
        goods_cart.setOnClickListener(this);
        add_cart.setOnClickListener(this);
        select_num.setOnClickListener(this);


    }
    void getCartNum(){
        //获得购物车商品数量
        List<ShoppingCart> listCart = DBHelper.getInstance(DetailGoodsActivity.this).getShoppingList();
        if(listCart!=null){
            number.setText(String.valueOf(listCart.size()));
        }else {
            number.setText("0");
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.goods_favour:
                //收藏
                if(StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    //没登录
                    Intent intent = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }else {
                    Resources res = getBaseContext().getResources();
                    String message = res.getString(R.string.is_dengluing).toString();
                    progressDialog = new ProgressDialog(DetailGoodsActivity.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage(message);
                    progressDialog.show();
                    saveFavour();
                }
                break;
            case R.id.goods_cart:
                //跳到购物车列表
//                ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
                Intent minecart = new Intent(DetailGoodsActivity.this, MineCartActivity.class);
                startActivity(minecart);
                break;
            case R.id.add_cart:
                //添加进购物车
                if(StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    //没登录
                    Intent intent = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }else{
                    Resources res = getBaseContext().getResources();
                    String message = res.getString(R.string.is_dengluing).toString();
                    progressDialog = new ProgressDialog(DetailGoodsActivity.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage(message);
                    progressDialog.show();
                    saveCart();
                }
                break;
        }
    }

    //获得商品详情
    private void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    GoodsDATA data = getGson().fromJson(s, GoodsDATA.class);
                                    if (data.getCode() == 200) {
                                        goods = data.getData();
                                        initData();
                                    }else {
                                        if(data.getCode() == -1){
                                            goods_favour.setCompoundDrawables(null, img_one, null, null);
                                        }
                                        Toast.makeText(DetailGoodsActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
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
                            Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "detail");
                params.put("id", id);
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
    //实例化数据
    private void initData(){
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + goods.getImg(),goods_pic, CarSmartApplication.options, animateFirstListener);
        goods_title.setText(goods.getName());
        goods_money.setText(res.getString(R.string.money) + goods.getSell_price());
        goods_money_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );//中间加横线
        goods_money_market.setText(res.getString(R.string.money)  + goods.getMarket_price());
        comment_count.setText(goods.getComments());
        select_num.setText("1");
//        htmlContent = goods.getContent();
//        if (htmlContent.indexOf("src=\"http") != -1) {//包括https
//
//        }else{
//            if(htmlContent.indexOf("src=\"/ueditor") != -1){//需要添加https
//                String str = "src=\"" + InternetURL.INTERNAL_PIC + "";
//                htmlContent = htmlContent.replaceAll("src=\"", str);
//            }
//        }
//        initContent(cont, htmlContent);

        WebView wv = (WebView) findViewById(R.id.wvHtml);
        String htmlData = goods.getContent();
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv.getSettings().setBuiltInZoomControls(true);

        htmlData = htmlData.replaceAll("&amp;", "");

        htmlData = htmlData.replaceAll("quot;", "\"");

        htmlData = htmlData.replaceAll("lt;", "<");

        htmlData = htmlData.replaceAll("gt;", ">");



        wv.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);

    }
    /**
     * 加载html数据
     * @param tv
     * @param s
     */
    private void initContent(TextView tv, String s)
    {
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        tv.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tv.setText(Html.fromHtml(s, new URLImageParser(cont, getApplicationContext()), null));
    }

    //保存收藏
    void saveFavour(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COLLECT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    FavourDATA data = getGson().fromJson(s, FavourDATA.class);
                                    if (data.getCode() == 200) {
                                        goods_favour.setCompoundDrawables(null, img_one, null, null);
                                    }else {
                                        if(data.getCode() == -1){
                                            goods_favour.setCompoundDrawables(null, img_one, null, null);
                                        }
                                        Toast.makeText(DetailGoodsActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
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
                            Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "collect");
                params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
                params.put("goods_id", id);
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
    //保存购物车
    void saveCart(){
        //先判断是否已经加入购物车
        if(DBHelper.getInstance(DetailGoodsActivity.this).isSaved(goods.getId())){
            //如果你存在
            Toast.makeText(DetailGoodsActivity.this, R.string.add_cart_is, Toast.LENGTH_SHORT).show();
        }else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setCartid(StringUtil.getUUID());
            shoppingCart.setGoods_id(goods.getId());
            shoppingCart.setEmp_id(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
            shoppingCart.setGoods_name(goods.getName());
            shoppingCart.setGoods_cover(goods.getImg());
            shoppingCart.setSell_price(goods.getSell_price());
            shoppingCart.setGoods_count("1");
            shoppingCart.setDateline(DateUtil.getCurrentDateTime());
            shoppingCart.setIs_select("0");
            DBHelper.getInstance(DetailGoodsActivity.this).addShoppingToTable(shoppingCart);
            Toast.makeText(DetailGoodsActivity.this, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("cart_success");
            DetailGoodsActivity.this.sendBroadcast(intent);
            getCartNum();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
