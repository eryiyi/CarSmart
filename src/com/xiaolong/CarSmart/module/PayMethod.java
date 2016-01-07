package com.xiaolong.CarSmart.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/21.
 * "pay_method_id": "1",
 "pay_method": "预存款支付",
 "description": "预存款是客户在您网站上的虚拟资金帐户。",
 "logo": "/payments/logos/pay_deposit.gif"
 */
public class PayMethod implements Serializable{
    private String pay_method_id;
    private String pay_method;
    private String description;
    private String logo;

    public String getPay_method_id() {
        return pay_method_id;
    }

    public void setPay_method_id(String pay_method_id) {
        this.pay_method_id = pay_method_id;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
