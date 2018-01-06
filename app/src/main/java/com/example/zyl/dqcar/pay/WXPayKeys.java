package com.example.zyl.dqcar.pay;

/**
 * Created by Helen Liu
 * Time :2016/6/21 0021 10:13.
 */
public class WXPayKeys {

    public static final String CALL_BACK_ERR_CODE_SUCCESS = 0 + "";//支付成功
    public static final String CALL_BACK_ERR_CODE_ERROR = -1 + "";//支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
    public static final String CALL_BACK_ERR_CODE_CANCEL = -2 + "";//支付取消，无需处理。发生场景：用户不支付了，点击取消，返回APP。

}
