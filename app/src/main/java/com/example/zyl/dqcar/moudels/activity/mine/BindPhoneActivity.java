package com.example.zyl.dqcar.moudels.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.SendPhoneCodeBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: Zhaoyl
 * Date: 2017/9/20 11:46
 * Description: 绑定手机
 * PackageName: BindPhoneActivity
 * Copyright: 端趣网络
 **/

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {


    TextView tvGetIdentifyCode;
    EditText etPhone;
    EditText etIdentifyCode;
    String verificationCode;


    int totalTimes = 5;//多少秒
    long during = 1000;//间隔时间毫秒
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            totalTimes--;
            if (totalTimes == 0) {
                tvGetIdentifyCode.setEnabled(true);
                tvGetIdentifyCode.setText("获取验证码");
            } else {
                tvGetIdentifyCode.setText(totalTimes + "秒后重发");
                handler.postDelayed(this, during);
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_bindphone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvTitle)).setText("绑定手机");

        etPhone = $(R.id.etPhone);
        tvGetIdentifyCode = $(R.id.tvGetIdentifyCode);
        etIdentifyCode = $(R.id.etIdentifyCode);

        $(R.id.btnNext).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        tvGetIdentifyCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetIdentifyCode:
                String phone = etPhone.getText() + "";
                if (TextUtils.isEmpty(phone) || !CheckUtil.checkPhone(phone)) {
                    ToastUtil.getInstance().show("请输入正确手机号");
                    return;
                }
                sendMsg(phone);
                break;
            case R.id.btnNext:
                next();
                break;
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }


    private void sendMsg(String phone) {
        //【msgType】短信验证类型：1.注册短信 2.修改密码短信
        totalTimes = 60;
        tvGetIdentifyCode.setText(totalTimes + "秒后重发");
        tvGetIdentifyCode.setEnabled(false);
        handler.postDelayed(runnable, during);
        //验证码请求
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_PHONEBIND
                        + "?cellNumber=" + phone
                , new HttpManager.ResponseCallback<SendPhoneCodeBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(SendPhoneCodeBean o) {
                        if (o.response.respCode.equals("000000")) {
                            ToastUtil.getInstance().show("发送成功");
                            verificationCode = o.response.verificationCode;
                        }
                    }
                });

    }


    private void next() {
        //手机号，验证码
        final String phone = etPhone.getText() + "";
        if (TextUtils.isEmpty(phone) || !CheckUtil.checkPhone(phone)) {
            ToastUtil.getInstance().show("请输入正确手机号");
            return;
        }
        final String identifyCode = etIdentifyCode.getText() + "";
        if (TextUtils.isEmpty(identifyCode) && !verificationCode.equals(identifyCode)) {
            ToastUtil.getInstance().show("请输入正确验证码");
            return;
        }

        HttpManager.getInstance().get(WebAPI.Login.LOGIN_WECHABIND
                        + "?phone=" + phone
                        + "&code=" + identifyCode
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("绑定成功");
                            BaseSharedPreferences.setPhoneNumber(BindPhoneActivity.this, phone);
                            EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_BINDPHONE));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });


    }
}
