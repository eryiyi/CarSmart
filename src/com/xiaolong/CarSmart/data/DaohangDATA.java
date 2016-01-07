package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Daohang;

import java.util.List;

/**
 * 短信验证码
 */
public class DaohangDATA extends Data {
    private List<Daohang> data;

    public List<Daohang> getData() {
        return data;
    }

    public void setData(List<Daohang> data) {
        this.data = data;
    }
}
