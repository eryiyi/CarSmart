package com.xiaolong.CarSmart.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.adapter.ItemCartAdapter;
import com.xiaolong.CarSmart.adapter.OnClickContentItemListener;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.dao.DBHelper;
import com.xiaolong.CarSmart.dao.ShoppingCart;
import com.xiaolong.CarSmart.ui.LoginActivity;
import com.xiaolong.CarSmart.ui.OrderMakeActivity;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/30.
 * 购物车
 */
public class FourFragment extends BaseFragment implements View.OnClickListener,OnClickContentItemListener {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.four_fragment, null);
        initView(view);
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

        return view;
    }

    private void initView(View view) {
        login = (Button) view.findViewById(R.id.login);
        number = (TextView) view.findViewById(R.id.number);
        no_login = (RelativeLayout) view.findViewById(R.id.no_login);
        is_login = (LinearLayout) view.findViewById(R.id.is_login);
        heji = (TextView) view.findViewById(R.id.heji);
        qujiesuan = (TextView) view.findViewById(R.id.qujiesuan);

        login.setOnClickListener(this);
        qujiesuan.setOnClickListener(this);

        lstv = (ListView) view.findViewById(R.id.lstv);

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
                //登录
                Intent loginView = new Intent(getActivity(), LoginActivity.class);
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
                        Intent orderMakeView = new Intent(getActivity(), OrderMakeActivity.class);
                        orderMakeView.putExtra("listsgoods",arrayList);
                        startActivity(orderMakeView);
                    }else{
                        Toast.makeText(getActivity(),R.string.cart_error_one, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),R.string.cart_error_one, Toast.LENGTH_SHORT).show();
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
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    void getData(){
        //取出数据,查询所有的购物车
        lists.clear();
        lists = DBHelper.getInstance(getActivity()).getShoppingList();
        //购物车
        adapter = new ItemCartAdapter(lists, getActivity());
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
