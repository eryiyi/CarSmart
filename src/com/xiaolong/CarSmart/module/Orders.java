package com.xiaolong.CarSmart.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/8/1.
 * 订单
 */
public class Orders implements Serializable{
    private String id;
    private String order_no;
    private String user_id;
    private String pay_type;
    private String distribution;
    private String status;
    private String pay_status;
    private String distribution_status;
    private String accept_name;
    private String postcode;
    private String telphone;
    private String country;
    private String province;
    private String city;
    private String area;
    private String address;
    private String mobile;
    private String payable_amount;
    private String real_amount;
    private String payable_freight;
    private String real_freight;
    private String pay_time;
    private String send_time;
    private String create_time;
    private String completion_time;
    private String invoice;
    private String postscript;
    private String note;
    private String if_del;
    private String insured;
    private String if_insured;
    private String pay_fee;
    private String invoice_title;
    private String taxes;
    private String promotions;
    private String discount;
    private String order_amount;
    private String prop;
    private String accept_time;
    private String exp;
    private String point;
    private String type;
    private String trade_no;
    private String takeself;
    private String freight_name;
    private List<GoodsDat> goodsData;

    public List<GoodsDat> getGoodsData() {
        return goodsData;
    }

    public void setGoodsData(List<GoodsDat> goodsData) {
        this.goodsData = goodsData;
    }

    public String getFreight_name() {
        return freight_name;
    }

    public void setFreight_name(String freight_name) {
        this.freight_name = freight_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getDistribution_status() {
        return distribution_status;
    }

    public void setDistribution_status(String distribution_status) {
        this.distribution_status = distribution_status;
    }

    public String getAccept_name() {
        return accept_name;
    }

    public void setAccept_name(String accept_name) {
        this.accept_name = accept_name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(String real_amount) {
        this.real_amount = real_amount;
    }

    public String getPayable_freight() {
        return payable_freight;
    }

    public void setPayable_freight(String payable_freight) {
        this.payable_freight = payable_freight;
    }

    public String getReal_freight() {
        return real_freight;
    }

    public void setReal_freight(String real_freight) {
        this.real_freight = real_freight;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIf_del() {
        return if_del;
    }

    public void setIf_del(String if_del) {
        this.if_del = if_del;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public String getIf_insured() {
        return if_insured;
    }

    public void setIf_insured(String if_insured) {
        this.if_insured = if_insured;
    }

    public String getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(String pay_fee) {
        this.pay_fee = pay_fee;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTakeself() {
        return takeself;
    }

    public void setTakeself(String takeself) {
        this.takeself = takeself;
    }
}
