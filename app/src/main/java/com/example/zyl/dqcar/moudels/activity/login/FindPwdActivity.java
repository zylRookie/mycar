package com.example.zyl.dqcar.moudels.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.SendPhoneCodeBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/7/26 10:51
 * Description: 找回密码
 * PackageName: FindPwdActivity
 * Copyright: 端趣网络
 **/
public class FindPwdActivity extends BaseActivity implements View.OnClickListener {


    TextView tvGetIdentifyCode;
    CheckBox registerCheckBox;
    EditText etPhone;
    EditText etIdentifyCode;
    String isCheckedS = "true";
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
        return R.layout.activity_findmypwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        etPhone = $(R.id.etPhone);
        registerCheckBox = $(R.id.registerCheckBox);
        tvGetIdentifyCode = $(R.id.tvGetIdentifyCode);
        etIdentifyCode = $(R.id.etIdentifyCode);

        $(R.id.btnNext).setOnClickListener(this);
        tvGetIdentifyCode.setOnClickListener(this);
        registerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheckedS = "true";
                } else {
                    isCheckedS = "false";
                }
            }
        });
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
        }
    }


    private void sendMsg(String phone) {
        //【msgType】短信验证类型：1.注册短信 2.修改密码短信
        totalTimes = 60;
        tvGetIdentifyCode.setText(totalTimes + "秒后重发");
        tvGetIdentifyCode.setEnabled(false);
        handler.postDelayed(runnable, during);
        //验证码请求
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_RESETPASSWORDCODE
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
        String phone = etPhone.getText() + "";
        if (TextUtils.isEmpty(phone) || !CheckUtil.checkPhone(phone)) {
            ToastUtil.getInstance().show("请输入正确手机号");
            return;
        }
        final String identifyCode = etIdentifyCode.getText() + "";
        if (TextUtils.isEmpty(identifyCode) && !verificationCode.equals(identifyCode)) {
            ToastUtil.getInstance().show("请输入正确验证码");
            return;
        }
        Log.e("AAA", "next: " + isCheckedS);
        if (isCheckedS.equals("false")) {
            ToastUtil.getInstance().show("请同意协议！");
            return;
        }
        //下一步
        Intent intent = new Intent(FindPwdActivity.this, ResetPwdActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", identifyCode);
        startActivity(intent);
        finish();

    }

}
