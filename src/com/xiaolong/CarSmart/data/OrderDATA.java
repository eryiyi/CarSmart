package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Order;

/**
 * 短信验证码
 */
public class OrderDATA extends Data {
    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
