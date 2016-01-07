package com.xiaolong.CarSmart.module;

/**
 * Created by Administrator on 2015/8/18.
 */
public class Daohang {
    private String id;
    private String name;
    private String logo;
    private String url;
    private String description;
    private String sort;
    private String category_ids;
    private String mum;
    private String sortLetters;  //显示数据拼音的首字母

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(String category_ids) {
        this.category_ids = category_ids;
    }

    public String getMum() {
        return mum;
    }

    public void setMum(String mum) {
        this.mum = mum;
    }
}
