package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/1.
 *   "order_deal_count": "0",
 "total_pay": 0,
 "prop_count": 0
 */
public class Member implements Serializable{
    private String order_deal_count;
    private String total_pay;
    private String prop_count;


    public String getOrder_deal_count() {
        return order_deal_count;
    }

    public void setOrder_deal_count(String order_deal_count) {
        this.order_deal_count = order_deal_count;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }

    public String getProp_count() {
        return prop_count;
    }

    public void setProp_count(String prop_count) {
        this.prop_count = prop_count;
    }
}
