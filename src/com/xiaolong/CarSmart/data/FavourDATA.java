package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.Favour;

import java.util.List;

/**
 * 收藏
 */
public class FavourDATA extends Data {
    private List<Favour> data;

    public List<Favour> getData() {
        return data;
    }

    public void setData(List<Favour> data) {
        this.data = data;
    }
}
