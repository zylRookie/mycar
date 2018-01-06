package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.zyl.dqcar.moudels.activity.login.LoginActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: Zhaoyl
 * Date: 2017/9/20 11:46
 * Description: 修改密码
 * PackageName: EditPwdActivity
 * Copyright: 端趣网络
 **/

public class EditPwdActivity extends BaseActivity implements View.OnClickListener {


    EditText etPhone;
    EditText etIdentifyCode;
    EditText etnEditOkPwd;


    @Override
    public int getLayout() {
        return R.layout.activity_editpwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvTitle)).setText("修改密码");

        etPhone = $(R.id.etPhone);
        etIdentifyCode = $(R.id.etIdentifyCode);
        etnEditOkPwd = $(R.id.etnEditOkPwd);

        $(R.id.btnNext).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                next();
                break;
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }


    private void next() {
        //手机号，验证码
        final String oldPwd = etPhone.getText() + "";
        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtil.getInstance().show("请输入旧密码");
            return;
        }
        final String newPwd = etIdentifyCode.getText() + "";
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtil.getInstance().show("请输入新密码");
            return;
        }
        final String okPwd = etnEditOkPwd.getText() + "";
        if (TextUtils.isEmpty(okPwd)) {
            ToastUtil.getInstance().show("请再次输入新密码");
            return;
        }
        if (!newPwd.equals(okPwd)) {
            ToastUtil.getInstance().show("输入新密码和确认密码要一致");
            return;
        }

        HttpManager.getInstance().get(WebAPI.Login.LOGIN_MODIFYPWD
                        + "?oldPwd=" + oldPwd
                        + "&newPwd=" + okPwd
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("修改成功，请重新登录！");
                            startActivity(new Intent(EditPwdActivity.this, LoginActivity.class));
                            BaseSharedPreferences.setId(EditPwdActivity.this, "");
                            BaseSharedPreferences.setSessionId(EditPwdActivity.this, "");
                            BaseSharedPreferences.setUserName(EditPwdActivity.this, "");
                            BaseSharedPreferences.setNumber(EditPwdActivity.this, "");
                            BaseSharedPreferences.setUserName(EditPwdActivity.this, "");
                            BaseSharedPreferences.setUserHeadUrl(EditPwdActivity.this, "");
                            BaseSharedPreferences.setRoleType(EditPwdActivity.this, "");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.BACK_SUCCESS));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });


    }
}
