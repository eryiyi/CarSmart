package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.CheckCode;

/**
 * 短信验证码
 */
public class CardDATA extends Data {
    private CheckCode data;

    public CheckCode getData() {
        return data;
    }

    public void setData(CheckCode data) {
        this.data = data;
    }
}
