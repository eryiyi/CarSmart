package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.City;

import java.util.List;

/**
 * 短信验证码
 */
public class CitysDATA extends Data {
    private List<City> data;

    public List<City> getData() {
        return data;
    }

    public void setData(List<City> data) {
        this.data = data;
    }
}
