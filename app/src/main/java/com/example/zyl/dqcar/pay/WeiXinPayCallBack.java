package com.example.zyl.dqcar.pay;

/**
 * Created by Helen Liu
 * Time :2016/6/21 0021 10:00.
 */

/**
 * 微信回调
 */
public interface WeiXinPayCallBack {

    void onWXPay(String code);

}