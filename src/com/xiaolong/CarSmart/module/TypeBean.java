package com.xiaolong.CarSmart.module;

/**
 * Created by Administrator on 2015/7/31.
 * 分类按钮首页
 */
public class TypeBean {
    private String name;
    private int pic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public TypeBean(String name, int pic) {
        this.name = name;
        this.pic = pic;
    }
}
