package com.example.zyl.dqcar.moudels.activity.msg;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.moudels.adapter.FindGoodsAdapter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.CustomPopWindow;

/**
 * Author: Zhaoyl
 * Date: 2017/7/3 10:10
 * Description: 查找闲置商品
 * PackageName: FindUserGoodsActivity
 * Copyright: 端趣网络
 **/

public class FindUserGoodsActivity extends BaseActivity implements View.OnClickListener {


    RecyclerView recyclerView;
    CustomPopWindow mCustomPopWindow;
    CustomPopWindow popWindow;
    View viewD;

    @Override
    public int getLayout() {
        return R.layout.activity_findusergoods;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("查找闲置商品");
        recyclerView = $(R.id.recyclerView_find_goods);
        viewD = $(R.id.viewD);
        $(R.id.llSort).setOnClickListener(this);
        $(R.id.llPrice).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

        getLinearLayoutManager();
        recyclerView.setAdapter(new FindGoodsAdapter(FindUserGoodsActivity.this));
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FindUserGoodsActivity.this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.llSort:
                showPopMenu();
                break;
            case R.id.llPrice:
                showPopMenuP();
                break;
        }
    }

    private void showPopMenuP() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_p, null);
        //处理popWindow 显示内容
        handleLogicP(contentView);
        //创建并显示popWindow
        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create()
                .showAsDropDown(viewD, 0, 0);
    }

    private void handleLogicP(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.tvA:
                        ToastUtil.getInstance().show("降！！！");
                        break;
                    case R.id.tvB:
                        ToastUtil.getInstance().show("降！！！");
                        break;
                    case R.id.tvC:
                        ToastUtil.getInstance().show("降！！！");
                        break;
                    case R.id.tvD:
                        ToastUtil.getInstance().show("降！！！");
                        break;

                }
            }
        };
        contentView.findViewById(R.id.tvA).setOnClickListener(listener);
        contentView.findViewById(R.id.tvB).setOnClickListener(listener);
        contentView.findViewById(R.id.tvC).setOnClickListener(listener);
        contentView.findViewById(R.id.tvD).setOnClickListener(listener);
    }

    private void showPopMenu() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create()
                .showAsDropDown(viewD, 0, 0);
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.tvOn:
                        ToastUtil.getInstance().show("升！！！");
                        break;
                    case R.id.tvUp:
                        ToastUtil.getInstance().show("降！！！");
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tvOn).setOnClickListener(listener);
        contentView.findViewById(R.id.tvUp).setOnClickListener(listener);
    }


}
