package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.PayMethod;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class PayMethodData extends Data {
    private List<PayMethod> data;

    public List<PayMethod> getData() {
        return data;
    }

    public void setData(List<PayMethod> data) {
        this.data = data;
    }
}
