package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Orders;

import java.util.List;

public class OrdersDATA extends Data {
    private List<Orders> data;

    public List<Orders> getData() {
        return data;
    }

    public void setData(List<Orders> data) {
        this.data = data;
    }
}
