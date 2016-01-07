package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/23.
 */
public class PointHistory implements Serializable {
    private String id;
    private String user_id;
    private String datetime;
    private String value;
    private String intro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
