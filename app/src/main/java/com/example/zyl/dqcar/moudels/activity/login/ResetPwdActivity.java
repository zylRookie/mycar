package com.example.zyl.dqcar.moudels.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/7/26 10:56
 * Description: 重置密码
 * PackageName: ResetPwdActivity
 * Copyright: 端趣网络
 **/

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

    EditText etnFirstPwd;
    EditText etnOkPwd;
    String phone;
    String code;

    @Override
    public int getLayout() {
        return R.layout.activity_findmypwd_next;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        etnFirstPwd = $(R.id.etnFirstPwd);
        etnOkPwd = $(R.id.etnOkPwd);
        $(R.id.btnNext).setOnClickListener(this);

        if (!CheckUtil.isNull(getIntent().getStringExtra("phone")))
            phone = getIntent().getStringExtra("phone");
        if (!CheckUtil.isNull(getIntent().getStringExtra("code")))
            code = getIntent().getStringExtra("code");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (CheckUtil.isNull(etnFirstPwd.getText().toString())) {
                    ToastUtil.getInstance().show("输入密码不能为空！");
                    return;
                }
                if (CheckUtil.isNull(etnOkPwd.getText().toString())) {
                    ToastUtil.getInstance().show("输入确认密码不能为空！");
                    return;
                }
                if (!etnFirstPwd.getText().toString().equals(etnOkPwd.getText().toString())) {
                    ToastUtil.getInstance().show("输入密码不一致！");
                    return;
                }

                next();
                break;
        }
    }

    private void next() {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_RESETPWD + "?phone=" + phone + "&code=" + code + "&pwd=" + etnOkPwd.getText().toString()
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            finish();
                            ToastUtil.getInstance().show("修改密码成功！");
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }
}
