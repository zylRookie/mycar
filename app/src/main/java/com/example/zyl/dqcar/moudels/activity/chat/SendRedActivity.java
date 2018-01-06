package com.example.zyl.dqcar.moudels.activity.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;

/**
 * Author: Zhaoyl
 * Date: 2017/7/17 9:56
 * Description: 发红包
 * PackageName: SendRedActivity
 * Copyright: 端趣网络
 **/

public class SendRedActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getLayout() {
        return R.layout.activity_sendred;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("发红包");
        $(R.id.ivBaseBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }
}
