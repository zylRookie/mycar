package com.example.zyl.dqcar.moudels.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;

/**
 * Author: Zhaoyl
 * Date: 2017/6/29 10:56
 * Description: 新闻详情页
 * PackageName: NewsContentDetailActivity
 * Copyright: 端趣网络
 **/

public class NewsContentDetailActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public int getLayout() {
        return R.layout.activity_newscontent_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("新闻");
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
