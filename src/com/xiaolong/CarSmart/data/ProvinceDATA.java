package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Province;

import java.util.List;

/**
 * 短信验证码
 */
public class ProvinceDATA extends Data {
    private List<Province> data;

    public List<Province> getData() {
        return data;
    }

    public void setData(List<Province> data) {
        this.data = data;
    }
}
