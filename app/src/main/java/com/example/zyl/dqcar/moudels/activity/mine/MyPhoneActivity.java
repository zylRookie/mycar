package com.example.zyl.dqcar.moudels.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.adapter.MyPhotoAdapter;
import com.example.zyl.dqcar.moudels.bean.RidersBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Zhaoyl
 * Date: 2017/6/28 13:53
 * Description: 我的相册
 * PackageName: MyPhoneActivity
 * Copyright: 端趣网络
 **/

public class MyPhoneActivity extends BaseActivity implements View.OnClickListener {


    RecyclerView recyclerView;
    TwinklingRefreshLayout refreshLayout;
    List<RidersBean.CircleForListModelListBean> circleForListModelList = new ArrayList<>();
    MyPhotoAdapter myPhotoAdapter;
    int page = 1;
    String userId;

    @Override
    public int getLayout() {
        return R.layout.activity_myphone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        ((TextView) $(R.id.tvBaseLeft)).setText("我的相册");
        recyclerView = $(R.id.recyclerView_phone);
        refreshLayout = $(R.id.refreshLayout);
        $(R.id.ivBaseBack).setOnClickListener(this);
    }

    private void initData() {
        userId = getIntent().getStringExtra("userId");
        if (CheckUtil.isNull(userId))
            userId = BaseSharedPreferences.getId(MyPhoneActivity.this);
        getLinearLayoutManager();
        myPhotoAdapter = new MyPhotoAdapter(MyPhoneActivity.this, circleForListModelList);
        recyclerView.setAdapter(myPhotoAdapter);
        initRefreshLayout();
        getData(1,userId);
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                circleForListModelList.clear();
                page = 1;
                getData(page,userId);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                page++;
                getData(page,userId);
            }
        });
    }

    private void getData(final int i, String userId) {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_GETUSERCIRCLE
                + "?userId=" + userId
                + "&pageId=" + i, new HttpManager.ResponseCallback<RidersBean>() {
            @Override
            public void onFail() {


            }

            @Override
            public void onSuccess(RidersBean o) {
                if (o.errorCode.equals("0000")) {
                    if (o.circleForListModelList != null && o.circleForListModelList.size() > 0) {
                        myPhotoAdapter.addData(o.circleForListModelList);
                    } else {
                        if (i == 1) {
                            ToastUtil.getInstance().show("暂无数据");
                        } else {
                            ToastUtil.getInstance().show("已经是最后一页了");
                        }
                    }
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                } else {
                    ToastUtil.getInstance().show(o.errorMsg);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyPhoneActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}
