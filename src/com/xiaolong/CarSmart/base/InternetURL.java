package com.xiaolong.CarSmart.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://chelingtong.com/interface/";
    public static final String INTERNAL_PIC = "http://chelingtong.com/";
    //登陆
    public static final String LOGIN_URL = INTERNAL + "login.php";
    //注册
    public static final String REG_URL = INTERNAL + "register.php";
    /**
     *四、产品查找
     * prod_search.php
     * */
    public static final String GOODS_FIND_URL = INTERNAL + "prod_search.php";
    /**
     * 五、产品详细
     * action:detail
     * id :产品id
     * */
    public static final String GOODS_DETAIL_URL = INTERNAL + "goods_show_detail.php";
    /**
     * 六、产品分类
     * category.php
     * */
    public static final String GOODS_TYPE_URL = INTERNAL + "category.php";

    /**
     * 七、新闻政策
     * news_show.php
     * */
    public static final String GOODS_NEWS_URL = INTERNAL + "news_show.php";
    /**
     *八、个人信息
     * member_info.php
     * */
    public static final String PERSON_URL = INTERNAL + "member_info.php";
    /**
     * 九、查看订单
     *
     * */
    public static final String ORDERS_URL = INTERNAL + "order_index.php";
    /**
     * 十、积分系统
     * */
    public static final String POINTS_URL = INTERNAL + "member_points.php";
    /**
     * 十一、快递查询
     * */
    public static final String DELIVERY_URL = INTERNAL + "delivery_check.php";
    //十三、导航栏三张图片
    public static final String SLIDENEWS_URL = INTERNAL + "banner_show.php";
    //十二、热门产品
    public static final String GOODS_HOT_URL = INTERNAL + "goods_show.php";
    /**
     * 十四、收藏接口
     * */
    public static final String COLLECT_URL = INTERNAL + "my_collect.php";
    /**
     * 十五、根据分类检索产品列表
     * */
    public static final String GOODS_SHOW_URL = INTERNAL + "goods_show.php";
    /*
    * 订单和收货地址
    * **/
    public static final String SET_ORDER = INTERNAL + "set_order.php";
    /*
    * 省市区
    * **/
    public static final String GET_AREA = INTERNAL + "area.php";
    //找回密码
    public static final String FORGET_PASS = INTERNAL + "forget_pass.php";
    //导航
    public static final String DAOHANG = INTERNAL + "get_goods_by_brand.php";
    //保存建议
    public static final String SAVE_SUGGEST = INTERNAL + "feedback.php";
    //积分历史
    public static final String POINT_HISTORY_URL = INTERNAL + "member_points.php";
    //收货地址
    public static final String ADDRESS_URL = INTERNAL + "address.php";
    //重置密码
    public static final String RESET_PWR_URL = INTERNAL + "change_pass.php";
    //goods_comment.php
    public static final String COMMENT_URL = INTERNAL + "goods_comment.php";
    //退款退货
    public static final String REFUNDMENT_URL = INTERNAL + "refundment.php";
}
