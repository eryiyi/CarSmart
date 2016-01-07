package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Goods;

import java.util.List;

/**
 * 商品详情
 */
public class GoodsListDATA extends Data {
    private List<Goods> data;

    public List<Goods> getData() {
        return data;
    }

    public void setData(List<Goods> data) {
        this.data = data;
    }
}
