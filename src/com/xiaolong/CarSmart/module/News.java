package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/1.
 * 政策新闻
 */
public class News implements Serializable{
    private String id;
    private String title;
    private String content;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
