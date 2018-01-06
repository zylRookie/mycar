package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.login.LoginActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.DeviceUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 15:06
 * Description: 设置
 * PackageName: SetUpActivity
 * Copyright: 端趣网络
 **/

public class SetUpActivity extends BaseActivity implements View.OnClickListener {


    TextView tvBindPhone, tvCode;


    @Override
    public int getLayout() {
        return R.layout.activity_setup;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvBaseLeft)).setText("设置");
        tvCode = $(R.id.tvCode);
        tvBindPhone = $(R.id.tvBindPhone);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.rlBack).setOnClickListener(this);
        $(R.id.rlEditPwd).setOnClickListener(this);
        tvBindPhone.setOnClickListener(this);
        initData();
    }

    private void initData() {
        tvCode.setText(DeviceUtil.getVersionName(SetUpActivity.this));
        tvBindPhone.setText(CheckUtil.isNull(BaseSharedPreferences.getPhoneNumber(SetUpActivity.this)) ? "绑定手机" : BaseSharedPreferences.getPhoneNumber(SetUpActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.rlEditPwd:
                startActivity(new Intent(SetUpActivity.this, EditPwdActivity.class));
                break;
            case R.id.tvBindPhone:
                if (CheckUtil.isNull(BaseSharedPreferences.getPhoneNumber(SetUpActivity.this))) {
                    startActivity(new Intent(SetUpActivity.this, BindPhoneActivity.class));
                }
                break;
            case R.id.rlBack:
                new AlertView("退出登录", "你确定要退出该程序吗？", null, new String[]{"取消", "确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 1) {
                            outLogin();
                        }
                    }
                }).show();
                break;
        }
    }

    //退出登录
    private void outLogin() {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_OUT
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            startActivity(new Intent(SetUpActivity.this, LoginActivity.class));
                            BaseSharedPreferences.setId(SetUpActivity.this, "");
                            BaseSharedPreferences.setSessionId(SetUpActivity.this, "");
                            BaseSharedPreferences.setUserName(SetUpActivity.this, "");
                            BaseSharedPreferences.setNumber(SetUpActivity.this, "");
                            BaseSharedPreferences.setUserName(SetUpActivity.this, "");
                            BaseSharedPreferences.setUserHeadUrl(SetUpActivity.this, "");
                            BaseSharedPreferences.setRoleType(SetUpActivity.this, "");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.BACK_SUCCESS));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.UPDATA_BINDPHONE) {
            tvBindPhone.setText(CheckUtil.isNull(BaseSharedPreferences.getPhoneNumber(SetUpActivity.this)) ? "绑定手机" : BaseSharedPreferences.getPhoneNumber(SetUpActivity.this));
        } else if (eventCenter.getEventCode() == EventBusConst.BACK_SUCCESS) {
            finish();
        }
    }


    //    WindowManager wm = (WindowManager) getSystemService(SetUpActivity.this.WINDOW_SERVICE);
//    int h = wm.getDefaultDisplay().getHeight();
//    int[] location = new int[2];
//                btnTry.getLocationOnScreen(location);
//    int x = location[0];
//    int y = location[1];
//                Log.e("AAA", "x:"+x+"y:"+y);
//                Log.e("AAA", "图片各个角Left："+btnTry.getLeft()+"Right："+btnTry.getRight()+"Top："+btnTry.getTop()+"Bottom："+btnTry.getBottom());
//                Log.e("AAA", "init: ---->h==" + h);

}
