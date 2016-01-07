package com.xiaolong.CarSmart.data;

import com.xiaolong.CarSmart.module.News;

import java.util.List;

/**
 * 新闻
 */
public class NewsDATA extends Data {
    private List<News> data;

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
