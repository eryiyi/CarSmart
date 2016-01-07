package com.xiaolong.CarSmart.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemCartAdapter;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.dao.DBHelper;
import com.xiaolong.CarSmart.dao.ShoppingCart;
import com.xiaolong.CarSmart.data.GoodsAddressDATA;
import com.xiaolong.CarSmart.data.OrderDATA;
import com.xiaolong.CarSmart.data.ShoppingAddressDATA;
import com.xiaolong.CarSmart.data.ShoppingAddressSingleDATA;
import com.xiaolong.CarSmart.module.*;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import com.xiaolong.CarSmart.util.zfb.PayResult;
import com.xiaolong.CarSmart.util.zfb.SignUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/8/9.
 * 生成订单
 */
public class OrderMakeActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ImageView back;
    //收货信息
    private TextView order_nickname;//收货人姓名
    private TextView order_phone;//收货人电话
    private TextView order_address;//收货人地址
    private ImageView select_address;//选择收货地址

    //付款信息
    private TextView order_pay_method;//货到付款
    private TextView order_kuaidi;//普通快递
    private TextView order_fapiao;//发票信息
    private EditText postscript;//用户附言
    private String invoice_title = "";//


    //底部价格和确认按钮
    private Button order_sure;//确认订单
    private TextView order_count;

    private List<ShoppingCart> lists;
    private ItemCartAdapter adapter;
    private ListView lstv;
    private ShoppingCart shoppingCart;

    private ShoppingAddress goodsAddress;
    private RelativeLayout select_address_liner;

    private OrdersForm SGform = new OrdersForm();

    Map<String,String> listOrders = new HashMap<String,String>();

    private ProgressDialog progressDialog;
    private String invoice = "0";
    private PayMethod payMethod;
    private KuaidiModule kuaidiModule;

    private Order tmporder;
    private TextView order_kuaidi_pay;


    //---------------------------------支付开始----------------------------------------
    // 商户PID
    public static final String PARTNER = "2088021438756659";
    // 商户收款账号
    public static final String SELLER = "82229443@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALys+oYaxqv4FYju" +
            "8C1poM6qmHLjWPnXzqEJT0NxyFXgdaK/Qe9DcpcASod9mIAdlLIxJEyYNlWeonAJ" +
            "VYW8pQ+pTVGwI9n0iaT71ldWQzcMN3Dvi/+zpgw3HxxO7HJtEIlR84pvILv1yceC" +
            "ZCqqQ4O/4SemsG00oTiTyD3SM2ZvAgMBAAECgYBLToeX6ywNC7Icu7Hljll+45yB" +
            "jri+0CJLKFoYw1uA21xYnxoEE9my54zX04uA502oafDhGYfmWLDhIvidrpP6oalu" +
            "URb/gbV5Bdcm98gGGVgm6lpK+G5N/eawXDjP0ZjxXb114Y/Hn/oVFVM9OqcujFSV" +
            "+Wg4JgJ4Mmtdr35gYQJBAPbhx030xPcep8/dL5QQMc7ddoOrfxXewKcpDmZJi2ey" +
            "381X+DhuphQ5gSVBbbunRiDCEcuXFY+R7xrgnP+viWcCQQDDpN8DfqRRl+cUhc0z" +
            "/TbnSPJkMT/IQoFeFOE7wMBcDIBoQePEDsr56mtc/trIUh/L6evP9bkjLzWJs/kb" +
            "/i25AkEAtoOf1k/4NUEiipdYjzuRtv8emKT2ZPKytmGx1YjVWKpyrdo1FXMnsJf6" +
            "k9JVD3/QZnNSuJJPTD506AfZyWS6TQJANdeF2Hxd1GatnaRFGO2y0mvs6U30c7R5" +
            "zd6JLdyaE7sNC6Q2fppjmeu9qFYq975CKegykYTacqhnX4I8KEwHYQJAby60iHMA" +
            "YfSUpu//f5LMMRFK2sVif9aqlYbepJcAzJ6zbiSG5E+0xg/MjEj/Blg9rNsqDG4R" +
            "ECGJG2nPR72O8g==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderMakeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderMakeActivity.this, OrderSuccessActivity.class);
                        intent.putExtra("order",tmporder);
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderMakeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderMakeActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(OrderMakeActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    //------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.order_make_activity);
        lists = (List<ShoppingCart>) getIntent().getExtras().get("listsgoods");
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(OrderMakeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        toCalculate();//计算金额
        getAddress();//获得默认收货地址
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        select_address_liner = (RelativeLayout) this.findViewById(R.id.select_address_liner);
        select_address_liner.setOnClickListener(this);
        order_sure = (Button) this.findViewById(R.id.order_sure);
        order_kuaidi_pay = (TextView) this.findViewById(R.id.order_kuaidi_pay);
        order_count = (TextView) this.findViewById(R.id.order_count);
        postscript = (EditText) this.findViewById(R.id.postscript);

        back.setOnClickListener(this);
        order_sure.setOnClickListener(this);

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemCartAdapter(lists , OrderMakeActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                shoppingCart = lists.get(position);
                if (shoppingCart != null) {
                    //查询商品信息，根据商品ID
//                    getGoodsById();
                }
            }
        });

        order_nickname = (TextView) this.findViewById(R.id.order_nickname);
        order_phone = (TextView) this.findViewById(R.id.order_phone);
        order_address = (TextView) this.findViewById(R.id.order_address);
        order_pay_method = (TextView) this.findViewById(R.id.order_pay_method);
        order_kuaidi = (TextView) this.findViewById(R.id.order_kuaidi);
        order_fapiao = (TextView) this.findViewById(R.id.order_fapiao);
        select_address = (ImageView) this.findViewById(R.id.select_address);
        select_address.setOnClickListener(this);
        this.findViewById(R.id.select_fapiao).setOnClickListener(this);
        order_pay_method.setOnClickListener(this);
        order_kuaidi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.order_pay_method:
                //支付方式
            {
                Intent zfmethod = new Intent(OrderMakeActivity.this, SelectPayMethodActivity.class);
                startActivityForResult(zfmethod, 0);
            }
                break;
            case R.id.order_kuaidi:
                //选择快递方式
            {
                Intent kdmethod = new Intent(OrderMakeActivity.this, SelectKuaidiMethodActivity.class);
                startActivityForResult(kdmethod, 0);
            }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.order_sure:
                if(goodsAddress ==null){
                    Toast.makeText(OrderMakeActivity.this, R.string.address_error_three, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(order_count.getText().toString()) || "￥0.0".equals(order_count.getText().toString())){
                    Toast.makeText(OrderMakeActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(payMethod == null){
                    Toast.makeText(OrderMakeActivity.this, R.string.order_error_five, Toast.LENGTH_SHORT).show();
                    return;
                } if(kuaidiModule == null){
                    Toast.makeText(OrderMakeActivity.this, R.string.order_error_six, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(lists != null){
                    for(ShoppingCart shoppingCart:lists){
                        listOrders.put(shoppingCart.getGoods_id(), shoppingCart.getGoods_count());
                    }
                }
                SGform.setList(listOrders);
                if(listOrders!=null && listOrders.size() > 0){
                    Resources res = getBaseContext().getResources();
                    String message = res.getString(R.string.please_wait).toString();
                    progressDialog = new ProgressDialog(OrderMakeActivity.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage(message);
                    progressDialog.show();
                    setOrder();
                }
                break;
            case R.id.select_address:
//                //选择收货地址
//            {
//                Intent addressView = new Intent(OrderMakeActivity.this, SelectAddressActivity.class);
//                startActivity(addressView);
//            }
                break;
            case R.id.select_address_liner:
                //选择收货地址
            {
                Intent addressView = new Intent(OrderMakeActivity.this, SelectAddressActivity.class);
                startActivityForResult(addressView, 0);
            }
                break;

            case R.id.select_fapiao:
            {
                //发票
                Intent fapiao = new Intent(OrderMakeActivity.this, SelectFapiaoActivity.class);
                startActivityForResult(fapiao, 0);
            }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 001){
            invoice =  data.getExtras().getString("community");
            invoice_title =  data.getExtras().getString("invoice_title");
            if("0".equals(invoice)){
                order_fapiao.setText(getResources().getString(R.string.bukaifapiao));
            }else {
                order_fapiao.setText(getResources().getString(R.string.kaifapiao) +"-" + invoice_title);
            }
        }
        if(resultCode == 002){
            goodsAddress= (ShoppingAddress) data.getExtras().get("goodsAddress");
            initAddress();
        }
        if(resultCode == 003){
            payMethod = (PayMethod) data.getExtras().get("payMethod");
            initPaymethod();
        }
        if(resultCode == 004){
            kuaidiModule = (KuaidiModule) data.getExtras().get("kuaidiModule");
            initZfmethod();
        }


    }
    void initPaymethod(){
        order_pay_method.setText(payMethod.getPay_method()==null?"":payMethod.getPay_method());
    }
    void initZfmethod(){
        order_kuaidi.setText(kuaidiModule.getTitle()==null?"":kuaidiModule.getTitle());

    }
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //左侧选择框按钮
                if("0".equals(lists.get(position).getIs_select())){
                    lists.get(position).setIs_select("1");
                }else {
                    lists.get(position).setIs_select("0");
                }
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
            case 2:
                //加号
                lists.get(position).setGoods_count(String.valueOf((Integer.parseInt(lists.get(position).getGoods_count()) + 1)));
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
            case 3:
                //减号
                int selectNum = Integer.parseInt(lists.get(position).getGoods_count());
                if(selectNum == 0){
                    return;
                }else {
                    lists.get(position).setGoods_count(String.valueOf((Integer.parseInt(lists.get(position).getGoods_count()) - 1)));
                    adapter.notifyDataSetChanged();
                }
                toCalculate();
                break;
        }
    }

    //计算金额总的
    void toCalculate(){
        if (lists != null){
            Double doublePrices = 0.0;
            for(int i=0; i<lists.size() ;i++){
                ShoppingCart shoppingCart = lists.get(i);
                if(shoppingCart.getIs_select() .equals("0")){
                    //默认是选中的
                    doublePrices = doublePrices + Double.parseDouble(shoppingCart.getSell_price()) * Double.parseDouble(shoppingCart.getGoods_count());
                }
            }
            order_count.setText(getResources().getString(R.string.countPrices) + doublePrices.toString());
        }
    }

    void getAddress(){
        //获得默认收货地址
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADDRESS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    ShoppingAddressDATA data = getGson().fromJson(s, ShoppingAddressDATA.class);
                                    if (data.getCode() == 200) {
                                        if(data.getData()!=null && data.getData().size()>0){
                                            goodsAddress = data.getData().get(0);
                                            initAddress();
                                        }
                                    } else {
                                        Toast.makeText(OrderMakeActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
                        Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "default");
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

    //实例化收货地址
    void initAddress(){
        order_nickname.setText(goodsAddress.getAccept_name());
        order_phone.setText(goodsAddress.getMobile());
        order_address.setText(goodsAddress.getProvince_name()+goodsAddress.getCity_name()
        +goodsAddress.getArea_name()+goodsAddress.getAddress());
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("address_success")) {
                getAddress();
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("address_success");//设置默认收货地址成功
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    //生成订单
    void setOrder(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SET_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    OrderDATA data = getGson().fromJson(s, OrderDATA.class);
                                    if(data.getCode() == 200){
                                        //清空购物车
                                        DBHelper.getInstance(OrderMakeActivity.this).removeAll();

                                        Intent intentSend = new Intent("cart_success");
                                        OrderMakeActivity.this.sendBroadcast(intentSend);
                                        tmporder = data.getData();
                                        if("11".equals(payMethod.getPay_method_id())){
                                            pay("chelingtong","chelingtong",data.getData().getPay_money(),data.getData().getOrdersn());
                                        }else {
                                            Intent intent = new Intent(OrderMakeActivity.this, OrderSuccessActivity.class);
                                            intent.putExtra("order",tmporder);
                                            startActivity(intent);
                                            finish();
                                        }


                                    } else{
                                        Toast.makeText(OrderMakeActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
                params.put("jsonStr", new Gson().toJson(listOrders));
                params.put("pay_method", payMethod.getPay_method_id());
                params.put("invoice", invoice);
                params.put("invoice_title", invoice_title);
                params.put("postscript", (postscript.getText().toString()==null?"":postscript.getText().toString()));
                params.put("prop", "");
                params.put("receiver_name", goodsAddress.getAccept_name());
                params.put("post", "");
                params.put("telephone", goodsAddress.getTelephone());
                params.put("country", "");
                params.put("province", goodsAddress.getProvince());
                params.put("county", goodsAddress.getCity());
                params.put("area", goodsAddress.getArea());
                params.put("address", goodsAddress.getAddress() );
                params.put("mobile", goodsAddress.getMobile());
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




    //---------------------------------------------------------支付宝------------------------------------------

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(String title, String cont, String amount,String sn) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(title, cont, "0.01" , sn);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderMakeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(OrderMakeActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price, String sn) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + sn + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + 1 + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"http://115.29.200.169/alipay_notify.php\"";
//        orderInfo += "&notify_url=" + "\"http://baidu.com\"";

        orderInfo += "&notify_url=" + "\""
                + "http://115.29.200.169/alipay_notify.php" + "\"";
//
//        // 服务接口名称， 固定值
//        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    //-------zfb----支付完成之后的操作
}
