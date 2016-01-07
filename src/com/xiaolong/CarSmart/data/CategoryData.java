package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.GoodsTypeBig;

import java.util.List;

/**
 * Created by Administrator on 2015/8/3.
 * 分类
 */
public class CategoryData extends Data {
    private List<GoodsTypeBig> data;

    public List<GoodsTypeBig> getData() {
        return data;
    }

    public void setData(List<GoodsTypeBig> data) {
        this.data = data;
    }
}
