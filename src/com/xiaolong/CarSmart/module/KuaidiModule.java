package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/22.
 */
public class KuaidiModule implements Serializable {
    private String title;
    private String cont;

    public KuaidiModule(String title, String cont) {
        this.title = title;
        this.cont = cont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

}
