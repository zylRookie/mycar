package com.example.zyl.dqcar.moudels.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.moudels.MainActivity;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/9/15 10:56
 * Description: 启动页
 * PackageName: OnStartActivity
 * Copyright: 端趣网络
 **/

public class OnStartActivity extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToastUtil.getInstance();
        //enter();
        next();
    }

    private void next() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    enter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void enter() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BaseSharedPreferences.getIsFirst(OnStartActivity.this)) {
                    startActivity(new Intent(OnStartActivity.this, GuideActivity.class));
                    finish();
                } else {
                    if (CheckUtil.isNull(BaseSharedPreferences.getSessionId(OnStartActivity.this))) {
                        startActivity(new Intent(OnStartActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        if (!CheckUtil.isNull(BaseSharedPreferences.getNumber(OnStartActivity.this))) {
                            startActivity(new Intent(OnStartActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(OnStartActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
            }
        }, 2 * 1000);

    }

}
