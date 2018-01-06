package com.example.zyl.dqcar.moudels.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.adapter.NewsAdapter;
import com.example.zyl.dqcar.moudels.bean.NewsListBean;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 14:25
 * Description: 新闻中心
 * PackageName: NewsContentActivity
 * Copyright: 端趣网络
 **/

public class NewsContentActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    TextView tvNull;
    TwinklingRefreshLayout refreshLayout;
    List<NewsListBean.ItemsBean> list = new ArrayList<>();
    int page = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_newscontent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();

    }

    private void initData() {
        getLinearLayoutManager();
        newsAdapter = new NewsAdapter(NewsContentActivity.this, list);
        recyclerView.setAdapter(newsAdapter);
        initRefreshLayout();
        getData(1);
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                list.clear();
                page = 1;
                getData(page);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                page++;
                getData(page);
            }
        });
    }

    private void getData(final int i) {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_FINDNEWSLIST
                + "?page=" + i
                + "&sort=1", new HttpManager.ResponseCallback<NewsListBean>() {
            @Override
            public void onFail() {
            }

            @Override
            public void onSuccess(NewsListBean o) {
                if (o.errorCode.equals("0000")) {
                    if (o.items != null && o.items.size() > 0) {
                        newsAdapter.addData(o.items);
                        tvNull.setVisibility(View.GONE);
                        refreshLayout.setVisibility(View.VISIBLE);
                    } else {
                        if (i == 1) {
                            tvNull.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        } else {
                            ToastUtil.getInstance().show("已经是最后一页了");
                            tvNull.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
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


    private void initView() {
        ((TextView) $(R.id.tvBaseLeft)).setText("新闻中心");
        recyclerView = $(R.id.recyclerView_news);
        refreshLayout = $(R.id.refreshLayout);
        tvNull = $(R.id.tvNull);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.rlRight).setOnClickListener(this);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsContentActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
