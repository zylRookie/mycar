package com.example.zyl.dqcar.moudels.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.moudels.bean.SendPhoneCodeBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/7/3 15:47
 * Description:  注册
 * PackageName: RegisterActivity
 * Copyright: 端趣网络
 **/

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    EditText etMyPhone;
    EditText etMyCode;
    EditText etFirstPwd;
    EditText etOkPwd;
    TextView tvGetIdentifyCode;
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
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        etMyPhone = $(R.id.etMyPhone);
        etMyCode = $(R.id.etMyCode);
        etFirstPwd = $(R.id.etFirstPwd);
        etOkPwd = $(R.id.etOkPwd);
        tvGetIdentifyCode = $(R.id.tvGetIdentifyCode);
        tvGetIdentifyCode.setOnClickListener(this);
        $(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                String phone = etMyPhone.getText().toString();
                String code = etMyCode.getText().toString();
                String firstPwd = etFirstPwd.getText().toString();
                String okPwd = etOkPwd.getText().toString();

                if (CheckUtil.isNull(phone)) {
                    ToastUtil.getInstance().show("手机号码不能为空！");
                    return;
                }
                if (CheckUtil.isNull(code)) {
                    ToastUtil.getInstance().show("验证码不能为空！");
                    return;
                }
                if (CheckUtil.isNull(firstPwd)) {
                    ToastUtil.getInstance().show("输入密码不能为空！");
                    return;
                }
                if (CheckUtil.isNull(okPwd)) {
                    ToastUtil.getInstance().show("确认密码不能为空！");
                    return;
                }

                if (!okPwd.equals(firstPwd)) {
                    ToastUtil.getInstance().show("确定密码和密码不一致！");
                    return;
                }
                register(phone, code, okPwd);
                break;
            case R.id.tvGetIdentifyCode:
                String edtphone = etMyPhone.getText().toString();
                if (TextUtils.isEmpty(edtphone) || !CheckUtil.checkPhone(edtphone)) {
                    ToastUtil.getInstance().show("请输入正确的手机号码");
                    return;
                }
                sendMsg(edtphone);
                break;
        }
    }

    private void sendMsg(String edtphone) {
        //【msgType】短信验证类型：1.注册短信 2.修改密码短信
        totalTimes = 60;
        tvGetIdentifyCode.setText(totalTimes + "秒");
        tvGetIdentifyCode.setEnabled(false);
        handler.postDelayed(runnable, during);
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_SENCODE
                        + "?cellNumber=" + edtphone
                , new HttpManager.ResponseCallback<SendPhoneCodeBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(SendPhoneCodeBean o) {
                        if (o.response.respCode.equals("000000")) {
                            ToastUtil.getInstance().show("发送成功");
                        }
                    }
                });
    }


    private void register(String phone, String code, String okPwd) {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_REGISTER
                        + "?phone=" + phone
                        + "&code=" + code
                        + "&pwd=" + okPwd
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("注册成功！");
                            BaseSharedPreferences.setSessionId(null, o.smUserModel.sessionId);
                            BaseSharedPreferences.setId(RegisterActivity.this, o.smUserModel.id);
                            BaseSharedPreferences.setPhoneNumber(RegisterActivity.this, o.smUserModel.phone);
                            BaseSharedPreferences.setNumber(RegisterActivity.this, o.smUserModel.number);
                            BaseSharedPreferences.setUserName(RegisterActivity.this, o.smUserModel.name);
                            BaseSharedPreferences.setUserHeadUrl(RegisterActivity.this, o.smUserModel.imgKey);
                            BaseSharedPreferences.setRoleType(RegisterActivity.this, o.smUserModel.roleType+"");
                            startActivity(new Intent(RegisterActivity.this, UserDetailActivity.class));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }
}
