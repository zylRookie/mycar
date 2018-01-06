package com.example.zyl.dqcar.pay;

/**
 * Created by Helen Liu
 * Time :2016/6/21 0021 10:01.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 微信回调广播
 */
public class WeiXinPayReceiver extends BroadcastReceiver {

    public static final String WEI_XI_PAY_CALLBACK = "WEI_XI_PAY_CALLBACK";
    public static final String WEI_XI_PAY_CALLBACK_KEY = "WEI_XI_PAY_CALLBACK_KEY";
    private  String orderId;

    private WeiXinPayCallBack weiXinPayCallBack;
    private PayCallBack payCallBack;
    public WeiXinPayReceiver(PayCallBack payCallBack) {
        this.payCallBack = payCallBack;
    }
    public WeiXinPayReceiver(PayCallBack payCallBack,String orderId) {
        this.payCallBack = payCallBack;
        this.orderId=orderId;
    }

    public void setWeiXinPayCallBack(WeiXinPayCallBack weiXinPayCallBack) {
        this.weiXinPayCallBack = weiXinPayCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String code = intent.getStringExtra(WeiXinPayReceiver.WEI_XI_PAY_CALLBACK_KEY);
            if (payCallBack != null) {
                if (code.equalsIgnoreCase(WXPayKeys.CALL_BACK_ERR_CODE_SUCCESS)) {
                    // paySuccess();
                    payCallBack.onPaySuccess(PayCallBack.PayTypeEnum.WEI_XIN_PAY, code, PayCallBack.paySuccessTip);

                } else if (code.equalsIgnoreCase(WXPayKeys.CALL_BACK_ERR_CODE_ERROR)) {
                    //payFail();
                    payCallBack.onPayFail(PayCallBack.PayTypeEnum.WEI_XIN_PAY, code, PayCallBack.payFailureTip);
                } else if (code.equalsIgnoreCase(WXPayKeys.CALL_BACK_ERR_CODE_CANCEL)) {
                    // payCancel();
                    payCallBack.onPayCancel(PayCallBack.PayTypeEnum.WEI_XIN_PAY, code, PayCallBack.payCancelTip);
                }
            }
        }
    }
}