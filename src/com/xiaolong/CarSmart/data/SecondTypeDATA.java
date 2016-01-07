package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.SecondType;

import java.util.List;

/**
 * 短信验证码
 */
public class SecondTypeDATA extends Data {
    private List<SecondType> data;

    public List<SecondType> getData() {
        return data;
    }

    public void setData(List<SecondType> data) {
        this.data = data;
    }
}
