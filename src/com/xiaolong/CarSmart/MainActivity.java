package com.xiaolong.CarSmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.umeng.update.UmengUpdateAgent;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.fragment.*;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    protected static final String TAG = "MainActivity";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private FirstFragment oneFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourFragment fourFragment;
    private FiveFragment fiveFragment;

    private TextView foot_index;
    private TextView foot_goods;
    private TextView foot_mine;
    private TextView foot_find;
    private TextView foot_type;

    private long waitTime = 2000;
    private long touchTime = 0;

    Drawable img_one, img_two, img_three, img_four,img_five,
            img_one_pressed, img_two_pressed, img_three_pressed, img_four_pressed,img_five_pressed;
    Resources res;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        UmengUpdateAgent.update(this);
        setContentView(R.layout.main);
        res = getResources();
        img_one = res.getDrawable(R.drawable.foot_one_n);
        img_one.setBounds(0, 0, img_one.getMinimumWidth(), img_one.getMinimumHeight());
        img_two = res.getDrawable(R.drawable.foot_two_n);
        img_two.setBounds(0, 0, img_two.getMinimumWidth(), img_two.getMinimumHeight());
        img_three = res.getDrawable(R.drawable.foot_three_n);
        img_three.setBounds(0, 0, img_three.getMinimumWidth(), img_three.getMinimumHeight());
        img_four = res.getDrawable(R.drawable.foot_four_n);
        img_four.setBounds(0, 0, img_four.getMinimumWidth(), img_four.getMinimumHeight());
        img_five = res.getDrawable(R.drawable.foot_five_n);
        img_five.setBounds(0, 0, img_five.getMinimumWidth(), img_five.getMinimumHeight());


        img_one_pressed = res.getDrawable(R.drawable.foot_one_p);
        img_one_pressed.setBounds(0, 0, img_one_pressed.getMinimumWidth(), img_one_pressed.getMinimumHeight());
        img_two_pressed = res.getDrawable(R.drawable.foot_two_p);
        img_two_pressed.setBounds(0, 0, img_two_pressed.getMinimumWidth(), img_two_pressed.getMinimumHeight());
        img_three_pressed = res.getDrawable(R.drawable.foot_three_p);
        img_three_pressed.setBounds(0, 0, img_three_pressed.getMinimumWidth(), img_three_pressed.getMinimumHeight());
        img_four_pressed = res.getDrawable(R.drawable.foot_four_p);
        img_four_pressed.setBounds(0, 0, img_four_pressed.getMinimumWidth(), img_four_pressed.getMinimumHeight());
        img_five_pressed = res.getDrawable(R.drawable.foot_five_p);
        img_five_pressed.setBounds(0, 0, img_five_pressed.getMinimumWidth(), img_five_pressed.getMinimumHeight());

        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_index);
    }

    private void initView() {
        foot_index = (TextView) this.findViewById(R.id.foot_index);
        foot_goods = (TextView) this.findViewById(R.id.foot_goods);
        foot_type = (TextView) this.findViewById(R.id.foot_type);
        foot_find = (TextView) this.findViewById(R.id.foot_find);
        foot_mine = (TextView) this.findViewById(R.id.foot_mine);
        foot_index.setOnClickListener(this);
        foot_mine.setOnClickListener(this);
        foot_find.setOnClickListener(this);
        foot_type.setOnClickListener(this);
        foot_goods.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switchFragment(v.getId());
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_index:
                if (oneFragment == null) {
                    oneFragment = new FirstFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_index.setCompoundDrawables(null, img_one_pressed, null, null);
                foot_type.setCompoundDrawables(null, img_two, null, null);
                foot_find.setCompoundDrawables(null, img_three, null, null);
                foot_goods.setCompoundDrawables(null, img_four, null, null);
                foot_mine.setCompoundDrawables(null, img_five, null, null);

                foot_index.setTextColor(this.getResources().getColor(R.color.red));
                foot_type.setTextColor(this.getResources().getColor(R.color.white));
                foot_find.setTextColor(this.getResources().getColor(R.color.white));
                foot_goods.setTextColor(this.getResources().getColor(R.color.white));
                foot_mine.setTextColor(this.getResources().getColor(R.color.white));

                break;
            case R.id.foot_type:
                if (secondFragment == null) {
                    secondFragment = new SecondFragment();
                    fragmentTransaction.add(R.id.content_frame, secondFragment);
                } else {
                    fragmentTransaction.show(secondFragment);
                }

                foot_index.setCompoundDrawables(null, img_one, null, null);
                foot_type.setCompoundDrawables(null, img_two_pressed, null, null);
                foot_find.setCompoundDrawables(null, img_three, null, null);
                foot_goods.setCompoundDrawables(null, img_four, null, null);
                foot_mine.setCompoundDrawables(null, img_five, null, null);

                foot_index.setTextColor(this.getResources().getColor(R.color.white));
                foot_type.setTextColor(this.getResources().getColor(R.color.red));
                foot_find.setTextColor(this.getResources().getColor(R.color.white));
                foot_goods.setTextColor(this.getResources().getColor(R.color.white));
                foot_mine.setTextColor(this.getResources().getColor(R.color.white));

                break;
            case R.id.foot_find:
                if (thirdFragment == null) {
                    thirdFragment = new ThirdFragment();
                    fragmentTransaction.add(R.id.content_frame, thirdFragment);
                } else {
                    fragmentTransaction.show(thirdFragment);
                }
                foot_index.setCompoundDrawables(null, img_one, null, null);
                foot_type.setCompoundDrawables(null, img_two, null, null);
                foot_find.setCompoundDrawables(null, img_three_pressed, null, null);
                foot_goods.setCompoundDrawables(null, img_four, null, null);
                foot_mine.setCompoundDrawables(null, img_five, null, null);

                foot_index.setTextColor(this.getResources().getColor(R.color.white));
                foot_type.setTextColor(this.getResources().getColor(R.color.white));
                foot_find.setTextColor(this.getResources().getColor(R.color.red));
                foot_goods.setTextColor(this.getResources().getColor(R.color.white));
                foot_mine.setTextColor(this.getResources().getColor(R.color.white));

                break;
            case R.id.foot_goods:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_index.setCompoundDrawables(null, img_one, null, null);
                foot_type.setCompoundDrawables(null, img_two, null, null);
                foot_find.setCompoundDrawables(null, img_three, null, null);
                foot_goods.setCompoundDrawables(null, img_four_pressed, null, null);
                foot_mine.setCompoundDrawables(null, img_five, null, null);

                foot_index.setTextColor(this.getResources().getColor(R.color.white));
                foot_type.setTextColor(this.getResources().getColor(R.color.white));
                foot_find.setTextColor(this.getResources().getColor(R.color.white));
                foot_goods.setTextColor(this.getResources().getColor(R.color.red));
                foot_mine.setTextColor(this.getResources().getColor(R.color.white));

                break;
            case R.id.foot_mine:
                if (fiveFragment == null) {
                    fiveFragment = new FiveFragment();
                    fragmentTransaction.add(R.id.content_frame, fiveFragment);
                } else {
                    fragmentTransaction.show(fiveFragment);
                }

                foot_index.setCompoundDrawables(null, img_one, null, null);
                foot_type.setCompoundDrawables(null, img_two, null, null);
                foot_find.setCompoundDrawables(null, img_three, null, null);
                foot_goods.setCompoundDrawables(null, img_four, null, null);
                foot_mine.setCompoundDrawables(null, img_five_pressed, null, null);

                foot_index.setTextColor(this.getResources().getColor(R.color.white));
                foot_type.setTextColor(this.getResources().getColor(R.color.white));
                foot_find.setTextColor(this.getResources().getColor(R.color.white));
                foot_goods.setTextColor(this.getResources().getColor(R.color.white));
                foot_mine.setTextColor(this.getResources().getColor(R.color.red));
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (secondFragment != null) {
            ft.hide(secondFragment);
        }
        if (thirdFragment != null) {
            ft.hide(thirdFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
        if (fiveFragment != null) {
            ft.hide(fiveFragment);
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("switch_type")) {
                switchFragment(R.id.foot_type);
            }
            if(action.equals("switch_cart")){
                switchFragment(R.id.foot_goods);
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("switch_type");
        myIntentFilter.addAction("switch_cart");
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }
}
