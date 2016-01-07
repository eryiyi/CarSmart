package com.xiaolong.CarSmart.module;

import java.util.List;

/**
 * Created by Administrator on 2015/8/2.
 */
public class SearchGoods {
    private List<ProSearch> goods;
    private List<ProSearchShop> shop;

    public List<ProSearch> getGoods() {
        return goods;
    }

    public void setGoods(List<ProSearch> goods) {
        this.goods = goods;
    }

    public List<ProSearchShop> getShop() {
        return shop;
    }

    public void setShop(List<ProSearchShop> shop) {
        this.shop = shop;
    }
}
