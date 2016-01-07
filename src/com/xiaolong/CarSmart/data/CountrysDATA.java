package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Country;

import java.util.List;

/**
 * 短信验证码
 */
public class CountrysDATA extends Data {
    private List<Country> data;

    public List<Country> getData() {
        return data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }
}
