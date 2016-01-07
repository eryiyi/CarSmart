package com.xiaolong.CarSmart.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.ActivityTack;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.MemberDATA;
import com.xiaolong.CarSmart.data.PointHistoryDATA;
import com.xiaolong.CarSmart.data.ProQuanDATA;
import com.xiaolong.CarSmart.data.UserDATA;
import com.xiaolong.CarSmart.module.Member;
import com.xiaolong.CarSmart.module.PointHistory;
import com.xiaolong.CarSmart.module.ProQuan;
import com.xiaolong.CarSmart.module.User;
import com.xiaolong.CarSmart.ui.*;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/30.
 */
public class FiveFragment extends BaseFragment implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private ImageView mine_set;//设置按钮
    private ImageView mine_msg;//消息按钮
    private ImageView mine_head;//头像
    private TextView mine_name;//昵称
    private TextView mine_manag;//账户管理
    private TextView mine_gz_goods;//我关注的商品
    private TextView mine_gz_dp;//我关注的店铺
    private TextView mine_record;//wode jilu

    private RelativeLayout relate_two;//我的订单

    private TextView mine_dfk;
    private TextView mine_dsh;
    private TextView mine_dpj;
    private TextView mine_back;

    private TextView mine_money;
    private TextView mine_quite;
    private TextView mine_quan;

    private TextView mine_suggest;

    private String username;//用户id
    private ProgressDialog progressDialog;
    private Member member;
    private User user;
    private TextView mine_tk;

    //代金券
    private List<ProQuan> listProQuan = new ArrayList<>();
    private List<PointHistory> pointHistories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerBoradcastReceiver();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.five_fragment, null);
        username = getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class);
        initView(view);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
            //登陆了
            Resources res = getActivity().getBaseContext().getResources();
            String message = res.getString(R.string.is_dengluing).toString();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(message);
            progressDialog.show();
            getDataUser();
            getData();
            getDJQ();
            getJfHistory();
        }
//        getJF();
        return view;
    }

    private void initView(View v) {
        mine_set = (ImageView) v.findViewById(R.id.mine_set);
        mine_msg = (ImageView) v.findViewById(R.id.mine_msg);
        mine_head = (ImageView) v.findViewById(R.id.mine_head);
        mine_name = (TextView) v.findViewById(R.id.mine_name);
        mine_quite = (TextView) v.findViewById(R.id.mine_quite);
        mine_manag = (TextView) v.findViewById(R.id.mine_manag);
        mine_gz_goods = (TextView) v.findViewById(R.id.mine_gz_goods);
        mine_gz_dp = (TextView) v.findViewById(R.id.mine_gz_dp);
        mine_record = (TextView) v.findViewById(R.id.mine_record);
        relate_two = (RelativeLayout) v.findViewById(R.id.relate_two);
        mine_dfk = (TextView) v.findViewById(R.id.mine_dfk);
        mine_dsh = (TextView) v.findViewById(R.id.mine_dsh);
        mine_dpj = (TextView) v.findViewById(R.id.mine_dpj);
        mine_back = (TextView) v.findViewById(R.id.mine_back);
        mine_money = (TextView) v.findViewById(R.id.mine_money);
        mine_quan = (TextView) v.findViewById(R.id.mine_quan);
        mine_suggest = (TextView) v.findViewById(R.id.mine_suggest);
        mine_tk = (TextView) v.findViewById(R.id.mine_tk);

        mine_record.setOnClickListener(this);
        relate_two.setOnClickListener(this);
        mine_suggest.setOnClickListener(this);
        mine_tk.setOnClickListener(this);
        mine_manag.setOnClickListener(this);
        mine_head.setOnClickListener(this);
        mine_name.setOnClickListener(this);
        mine_dfk.setOnClickListener(this);
        mine_dsh.setOnClickListener(this);
        mine_dpj.setOnClickListener(this);
        mine_back.setOnClickListener(this);
        mine_quite.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relate_two:
                //订单
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    Intent orderView = new Intent(getActivity(), MineOrderActivity.class);
                    startActivity(orderView);
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.mine_suggest:
                //提建议
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    Intent intent = new Intent(getActivity(), SuggestActivity.class);
                    startActivity(intent);
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.mine_tk:
                //退款
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    Intent orderView = new Intent(getActivity(), MineOrderActivity.class);
                    startActivity(orderView);
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.mine_record:
                Intent record = new Intent(getActivity(), PointHistoryActivity.class);
                ArrayList<PointHistory> pointHistoriesTmp = new ArrayList<PointHistory>();
                if(pointHistories!=null && pointHistories.size() > 0){
                    for(PointHistory pointHistory:pointHistories){
                        pointHistoriesTmp.add(pointHistory);
                    }
                }
                record.putExtra("pointHistories",pointHistoriesTmp);
                startActivity(record);
                break;
            case R.id.mine_head:
            case R.id.mine_name:
            case R.id.mine_manag:
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    //账户收货地址管理
                    Intent intent = new Intent(getActivity(), MineZhanghuActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }

                break;
            case R.id.mine_dfk:
            case R.id.mine_dsh:
            case R.id.mine_dpj:
            case R.id.mine_back:
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class))){
                    Intent orderView = new Intent(getActivity(), MineOrderActivity.class);
                    startActivity(orderView);
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.mine_quite:
                save(Constants.PASSWORD, "");
                Intent loginV = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginV);
                break;
        }
    }

    //获得个人信息
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.PERSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    MemberDATA data = getGson().fromJson(s, MemberDATA.class);
                                    if (data.getCode() == 200) {
                                        member = data.getData();
                                        initData();
                                    }else {
                                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "member");
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

    private void getDataUser() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.PERSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    UserDATA data = getGson().fromJson(s, UserDATA.class);
                                    if (data.getCode() == 200) {
                                        user = data.getData();
                                        initDataUser();
                                    }else {
                                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.login_error_three, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "user");
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

    //获得会员积分
    private void getJF() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.POINTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {


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
                        Toast.makeText(getActivity(), R.string.login_error_three, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "check");
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

    //获得代金券
    private void getDJQ() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.POINTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            ProQuanDATA data = getGson().fromJson(s, ProQuanDATA.class);
                            if (data.getCode() == 200) {
                                listProQuan = data.getData();
                                mine_quan.setText(listProQuan.size());
                            }else if(data.getCode() == -1) {
                            }else{
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
                        Toast.makeText(getActivity(), R.string.login_error_three, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "get_prop");
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

    //实例化
     void initDataUser() {
         imageLoader.displayImage(InternetURL.INTERNAL +user.getHead_ico(), mine_head, CarSmartApplication.txOptions, animateFirstListener);
         mine_name.setText(user.getUsername() +"\n" + (user.getTrue_name()==null?"":user.getTrue_name()));
    }
    void initData(){
        mine_gz_goods.setText((member.getProp_count()==null?"0": member.getProp_count()) + "\n" + getResources().getString(R.string.muqianjifen));
        mine_gz_dp.setText((member.getOrder_deal_count()==null?"0":member.getOrder_deal_count()) + "\n" + getResources().getString(R.string.dingdanjiaoyizongliang));
        mine_record.setText(member.getTotal_pay() + "\n" + getResources().getString(R.string.zongzhifujine));
//        mine_money.setText(member.getBalance()==null?"0":member.getBalance());
//        mine_quan.setText(member.getProp_count()==null?"0":member.getProp_count());
    }
    private void getJfHistory() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.POINT_HISTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    PointHistoryDATA data = getGson().fromJson(s, PointHistoryDATA.class);
                                    if(data.getCode() == 200){
                                        pointHistories = data.getData();

                                    }else {
                                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "record");
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

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("login_success")) {
                //登录成功
                getDataUser();
                getData();
                getDJQ();
                getJfHistory();
            }
            if("edit_user_success".equals(action)){
                getDataUser();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("login_success");
        myIntentFilter.addAction("edit_user_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }


}
