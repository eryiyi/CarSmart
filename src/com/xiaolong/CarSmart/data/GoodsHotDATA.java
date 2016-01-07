package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.GoodsHot;

import java.util.List;

/**
 * 热门商品
 */
public class GoodsHotDATA extends Data {
    private List<GoodsHot> data;

    public List<GoodsHot> getData() {
        return data;
    }

    public void setData(List<GoodsHot> data) {
        this.data = data;
    }
}
