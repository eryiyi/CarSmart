package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.GoodsByType;

import java.util.List;

/**
 * Created by Administrator on 2015/8/3.
 */
public class GoodsByTypeData extends Data {
    private List<GoodsByType> data;

    public List<GoodsByType> getData() {
        return data;
    }

    public void setData(List<GoodsByType> data) {
        this.data = data;
    }
}
