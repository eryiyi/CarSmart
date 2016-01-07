package com.xiaolong.CarSmart.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.adapter.ItemCartAdapter;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.dao.DBHelper;
import com.xiaolong.CarSmart.dao.ShoppingCart;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MineCartActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private Button login;//登录按钮

    private RelativeLayout no_login;//没登录的时候
    private LinearLayout is_login;//登录的时候

    private ListView lstv;
    private ItemCartAdapter adapter;
    private List<ShoppingCart> lists = new ArrayList<>();

    private TextView heji;
    private TextView qujiesuan;

    Resources res;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        registerBoradcastReceiver();
        setContentView(R.layout.mine_cart_fragment);
        initView();
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
            //登陆了
            no_login.setVisibility(View.GONE);
            is_login.setVisibility(View.VISIBLE);
            //取购物车信息
            getData();
        }else {
            //没登录
            no_login.setVisibility(View.VISIBLE);
            is_login.setVisibility(View.GONE);
        }

    }

    public void back(View view){
        finish();
    }


    private void initView() {
        number = (TextView) this.findViewById(R.id.number);
        login = (Button) this.findViewById(R.id.login);
        no_login = (RelativeLayout) this.findViewById(R.id.no_login);
        is_login = (LinearLayout) this.findViewById(R.id.is_login);
        heji = (TextView) this.findViewById(R.id.heji);
        qujiesuan = (TextView) this.findViewById(R.id.qujiesuan);

        login.setOnClickListener(this);
        qujiesuan.setOnClickListener(this);

        lstv = (ListView) this.findViewById(R.id.lstv);

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
                //登录
                Intent loginView = new Intent(MineCartActivity.this, LoginActivity.class);
                startActivity(loginView);
                break;
            case R.id.qujiesuan:
                //结算
                if(lists != null && lists.size() > 0){
                    ArrayList<ShoppingCart> arrayList = new ArrayList<>();
                    for(int i=0;i<lists.size();i++){
                        if(lists.get(i).getIs_select().equals("0")){
                            arrayList.add(lists.get(i));
                        }
                    }
                    if(arrayList != null && arrayList.size() > 0){
                        Intent orderMakeView = new Intent(MineCartActivity.this, OrderMakeActivity.class);
                        orderMakeView.putExtra("listsgoods",arrayList);
                        startActivity(orderMakeView);
                    }else{
                        Toast.makeText(MineCartActivity.this,R.string.cart_error_one, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MineCartActivity.this,R.string.cart_error_one, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("login_success")) {
                //登录成功
                no_login.setVisibility(View.GONE);
                is_login.setVisibility(View.VISIBLE);
                getData();
            }
            if(action.equals("cart_success")){
                //加入购物车成功
                getData();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("login_success");//登录按钮
        myIntentFilter.addAction("cart_success");//加入购物车成功
        //注册广播
       registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    void getData(){
        //取出数据,查询所有的购物车
        lists.clear();
        lists = DBHelper.getInstance(MineCartActivity.this).getShoppingList();
        //购物车
        adapter = new ItemCartAdapter(lists, MineCartActivity.this);
        lstv.setAdapter(adapter);
        if(lists.size() == 0){
            qujiesuan.setText(res.getString(R.string.no_data));
        }else {
            qujiesuan.setText(res.getString(R.string.qujiesuan));
        }
        adapter.setOnClickContentItemListener(this);
        toCalculate();
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
            heji.setText(getResources().getString(R.string.countPrices) + doublePrices.toString());
        }
        getCartNum();
    }
    void getCartNum(){
        int num=0;
        if (lists != null){
            for(int i=0; i<lists.size() ;i++){
                ShoppingCart shoppingCart = lists.get(i);
                if(shoppingCart.getIs_select() .equals("0")){
                    //默认是选中的
                    num = num + Integer.parseInt(shoppingCart.getGoods_count()==null?"":shoppingCart.getGoods_count());
                }
            }
            number.setText(String.valueOf(num));
        }
    }

}
