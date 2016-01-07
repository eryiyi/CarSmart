package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.PointHistory;

import java.util.List;

public class PointHistoryDATA extends Data {
    private List<PointHistory> data;

    public List<PointHistory> getData() {
        return data;
    }

    public void setData(List<PointHistory> data) {
        this.data = data;
    }
}
