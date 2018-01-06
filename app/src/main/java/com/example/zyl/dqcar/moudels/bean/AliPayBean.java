package com.example.zyl.dqcar.moudels.bean;

/**
 * Created by yongliangzhao on 2017/9/21.
 */

public class AliPayBean {

    /**
     * success : true
     * errorCode : null
     * errorFields : null
     * msg : 成功
     * data : {"totalAmount":"100","orderSn":"010017092115033975200102605733","notifyUrl":"http://pay.willowspace.cn/alipay/callback","orderString":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017090208523217&biz_content=%7B%22body%22%3A%22%E9%A9%B9%E5%B8%81%E5%85%85%E5%80%BC%22%2C%22goods_type%22%3A%220%22%2C%22out_trade_no%22%3A%22010017092115033975200102605733%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E9%A9%B9%E5%B8%81%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%22100%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpay.willowspace.cn%2Falipay%2Fcallback&sign=jdSuyqGC6bvMt5bj5UJfu%2B%2F4voLrMVrNRUE8DKxHShcwX7uNL9DyKO%2BcY%2FW1FZvnMqgWPZc3mgf2Ah5QxbcKGe9V217EEdxO%2Bh%2Fo1wlLl9AHOsa8jf71VcSuB9tgIlkYCjxGAjxMro6cQo5zEi6S6psfc%2FnPZ0m43DApbl2UikRoS%2FanEkCGr6V0hg9sOwFpTJse7RZVNQ2xMKcJagtOndI7hSZUi78HteBy13uU73zRSOyr9zVe3zqOsiQYa2K%2B3k3%2FDe3N4fwOmR0SURlRdqL54E0WNPsRcTC1pX6LSIqKMW3EtEANwikAYahtaBfWV4tcYWvlYRGzQdA8UzRfEQ%3D%3D&sign_type=RSA2&timestamp=2017-09-21+15%3A03%3A39&version=1.0"}
     */

    public boolean success;
    public Object errorCode;
    public Object errorFields;
    public String msg;
    public DataBean data;


    public static class DataBean {
        /**
         * totalAmount : 100
         * orderSn : 010017092115033975200102605733
         * notifyUrl : http://pay.willowspace.cn/alipay/callback
         * orderString : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017090208523217&biz_content=%7B%22body%22%3A%22%E9%A9%B9%E5%B8%81%E5%85%85%E5%80%BC%22%2C%22goods_type%22%3A%220%22%2C%22out_trade_no%22%3A%22010017092115033975200102605733%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E9%A9%B9%E5%B8%81%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%22100%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpay.willowspace.cn%2Falipay%2Fcallback&sign=jdSuyqGC6bvMt5bj5UJfu%2B%2F4voLrMVrNRUE8DKxHShcwX7uNL9DyKO%2BcY%2FW1FZvnMqgWPZc3mgf2Ah5QxbcKGe9V217EEdxO%2Bh%2Fo1wlLl9AHOsa8jf71VcSuB9tgIlkYCjxGAjxMro6cQo5zEi6S6psfc%2FnPZ0m43DApbl2UikRoS%2FanEkCGr6V0hg9sOwFpTJse7RZVNQ2xMKcJagtOndI7hSZUi78HteBy13uU73zRSOyr9zVe3zqOsiQYa2K%2B3k3%2FDe3N4fwOmR0SURlRdqL54E0WNPsRcTC1pX6LSIqKMW3EtEANwikAYahtaBfWV4tcYWvlYRGzQdA8UzRfEQ%3D%3D&sign_type=RSA2&timestamp=2017-09-21+15%3A03%3A39&version=1.0
         */
        public String totalAmount;
        public String orderSn;
        public String notifyUrl;
        public String orderString;
    }
}
