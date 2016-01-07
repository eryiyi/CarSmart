package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/19.
 */
public class GoodsDat implements Serializable{
    private String goods_id;
    private String goods_image;
    private String goods_price;
    private String name;
    private String goodsno;
    private String value;
    private String isSelect;

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsno() {
        return goodsno;
    }

    public void setGoodsno(String goodsno) {
        this.goodsno = goodsno;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
