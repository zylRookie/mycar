package com.example.zyl.dqcar.pay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.zyl.dqcar.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;




public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = WXAPIFactory.createWXAPI(this, PayConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付回调
            Intent intent = new Intent(WeiXinPayReceiver.WEI_XI_PAY_CALLBACK);
            intent.putExtra(WeiXinPayReceiver.WEI_XI_PAY_CALLBACK_KEY, resp.errCode + "");
            sendBroadcast(intent);
        }
        finish();
    }
}