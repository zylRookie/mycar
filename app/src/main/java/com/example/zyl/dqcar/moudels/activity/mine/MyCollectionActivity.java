package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.MyCollectionBean;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 14:04
 * Description:  我的收藏
 * PackageName: MyCollectionActivity
 * Copyright: 端趣网络
 **/

public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    TwinklingRefreshLayout refreshLayout;
    List<MyCollectionBean.ItemsBean> items = new ArrayList<>();
    MyAdapter myAdapter;
    TextView tvNull;
    int page = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_mycollection;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        getLinearLayoutManager();
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        initRefreshLayout();
        getData(1);
    }

    private void getData(final int i) {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_FINDCOLLECTLIST
                        + "?sort=1"
                        + "&page=" + i,
                new HttpManager.ResponseCallback<MyCollectionBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MyCollectionBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (o.items != null && o.items.size() > 0) {
                                if (i == 1)
                                    myAdapter.setData(o.items);
                                else
                                    myAdapter.addData(o.items);
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
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                items.clear();
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

    private void initView() {
        ((TextView) $(R.id.tvBaseLeft)).setText("我的收藏");
        recyclerView = $(R.id.recyclerView_collection);
        refreshLayout = $(R.id.refreshLayout);
        tvNull = $(R.id.tvNull);
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

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCollectionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {

        List<MyCollectionBean.ItemsBean> mDatas;

        public void setData(List<MyCollectionBean.ItemsBean> items) {
            this.mDatas = items;
            notifyDataSetChanged();
        }

        public void addData(List<MyCollectionBean.ItemsBean> items) {
            mDatas.addAll(mDatas);
            notifyDataSetChanged();
        }


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.layout_item_mycollection, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.bindData(mDatas.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mDatas != null && mDatas.size() > 0 ? mDatas.size() : 0;
        }

        public class ViewHolder extends VH {

            ImageView ivMyCollectionHead, ivMyCollectionImage;
            TextView ivMyCollectionName, ivMyCollectionTime, ivMyCollectionTitle;
            Intent intent;

            public ViewHolder(View itemView) {
                super(itemView);
                ivMyCollectionHead = $(R.id.ivMyCollectionHead);
                ivMyCollectionImage = $(R.id.ivMyCollectionImage);
                ivMyCollectionName = $(R.id.ivMyCollectionName);
                ivMyCollectionTime = $(R.id.ivMyCollectionTime);
                ivMyCollectionTitle = $(R.id.ivMyCollectionTitle);
            }

            @Override
            public void bindData(Object o, int pos) {
                super.bindData(o, pos);
                final MyCollectionBean.ItemsBean datas = (MyCollectionBean.ItemsBean) o;
                Picasso.with(MyCollectionActivity.this).load(datas.imgKey).transform(new CircleTransform()).into(ivMyCollectionHead);
                ivMyCollectionName.setText(datas.name);
                ivMyCollectionTime.setText(datas.createdTime);
                Picasso.with(MyCollectionActivity.this).load(datas.imgLst.get(0)).into(ivMyCollectionImage);
                ivMyCollectionTitle.setText(datas.title);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (datas.saleType) {
                            case 1:
                                intent = new Intent(MyCollectionActivity.this, CarDetailActivity.class);
                                intent.putExtra("type", "oldCarMyCollection");
                                intent.putExtra("productId", datas.productId);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MyCollectionActivity.this, CarDetailActivity.class);
                                intent.putExtra("type", "findGoodsMyCollection");
                                intent.putExtra("productId", datas.productId);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(MyCollectionActivity.this, CarDetailActivity.class);
                                intent.putExtra("type", "gift");
                                intent.putExtra("productId", datas.productId);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }
        }
    }
}
