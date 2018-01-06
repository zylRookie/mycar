package com.example.zyl.dqcar.pay;

/**
 * Created by Helen Liu
 * Time :2016/6/21 0021 09:58.
 */

/**
 * 支付回调
 */
public interface PayCallBack {
    String paySuccessTip = "支付成功";
    String payFailureTip = "支付失败";
    String payCancelTip = "取消支付";
    String payResultConfirm = "支付结果确认中";
    String netWorkError = "网络失败";

    public enum PayTypeEnum {
        ALIPAY, WEI_XIN_PAY, UPPAY,BALANCE_PAY;
    }


    //支付成功
    void onPaySuccess(PayTypeEnum payTypeEnum, String payCode, String tipContent);

    //支付失败
    void onPayFail(PayTypeEnum payTypeEnum, String payCode, String tipContent);

    //支付取消
    void onPayCancel(PayTypeEnum payTypeEnum, String payCode, String tipContent);

    //支付确认中
    void onPayConfirm(PayTypeEnum payTypeEnum, String payCode, String tipContent);

    //网络连接失败
    void onNetWorkError(String tipContent);
}