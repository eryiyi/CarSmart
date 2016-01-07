package com.xiaolong.CarSmart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.xiaolong.CarSmart.MainActivity;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.base.ActivityTack;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.module.Order;

/**
 * Created by Administrator on 2015/8/10.
 * 订单成功页
 */
public class OrderSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView order_num;
    private TextView order_jine;
    private TextView order_pay_method;
    private TextView button_save;
    private TextView button_one;
    private TextView button_two;
    private TextView button_three;

    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success_activity);
        order = (Order) getIntent().getExtras().get("order");
        initView();
    }

    private void initView() {
        order_num = (TextView) this.findViewById(R.id.order_num);
        order_jine = (TextView) this.findViewById(R.id.order_jine);
        order_pay_method = (TextView) this.findViewById(R.id.order_pay_method);
        button_save = (TextView) this.findViewById(R.id.button_save);
        button_one = (TextView) this.findViewById(R.id.button_one);
        button_two = (TextView) this.findViewById(R.id.button_two);
        button_three = (TextView) this.findViewById(R.id.button_three);

        button_save.setOnClickListener(this);
        button_one.setOnClickListener(this);
        button_two.setOnClickListener(this);
        button_three.setOnClickListener(this);

        order_num.setText(order.getOrdersn());
        order_jine.setText(order.getPay_money() + getResources().getString(R.string.order_init));
        if("0".equals(order.getPay_way())){
            order_pay_method.setText(getResources().getString(R.string.order_pay));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
                break;
            case R.id.button_one:
                Intent orderView = new Intent(OrderSuccessActivity.this, MineOrderActivity.class);
                startActivity(orderView);
                break;
            case R.id.button_two:
                ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
                break;
            case R.id.button_three:
                ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
                break;
        }
    }
}
