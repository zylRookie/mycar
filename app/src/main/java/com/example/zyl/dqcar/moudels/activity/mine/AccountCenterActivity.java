package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.squareup.picasso.Picasso;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 10:00
 * Description: 账户中心
 * PackageName: AccountCenterActivity
 * Copyright: 端趣网络
 **/

public class AccountCenterActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout llStart;
    TextView tvUserMoney;
    ImageView ivUserHead;

    @Override
    public int getLayout() {
        return R.layout.activity_account_center;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("账户中心");
        llStart = $(R.id.llStart);
        tvUserMoney = $(R.id.tvUserMoney);
        ivUserHead = $(R.id.ivUserHead);

        $(R.id.btnRecharge).setOnClickListener(this);
        $(R.id.btnGive).setOnClickListener(this);
        $(R.id.btnRenew).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

        getData();

    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_USERDETAIL
                        + "?useId=" + BaseSharedPreferences.getId(AccountCenterActivity.this)
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            tvUserMoney.setText("驹币  " + o.smUserModel.money + "");
                            if (!CheckUtil.isNull(o.smUserModel.imgKey))
                                Picasso.with(AccountCenterActivity.this).load(o.smUserModel.imgKey).transform(new CircleTransform()).into(ivUserHead);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.btnRecharge://充值
                intent = new Intent(AccountCenterActivity.this, BusinessActivity.class);
                intent.putExtra("type", "recharge");
                startActivity(intent);
                break;
            case R.id.btnGive://赠给
                intent = new Intent(AccountCenterActivity.this, BusinessActivity.class);
                intent.putExtra("type", "give");
                startActivity(intent);
                break;
            case R.id.btnRenew://续费
                intent = new Intent(AccountCenterActivity.this, BusinessActivity.class);
                intent.putExtra("type", "renew");
                startActivity(intent);
                break;
        }
    }
}
