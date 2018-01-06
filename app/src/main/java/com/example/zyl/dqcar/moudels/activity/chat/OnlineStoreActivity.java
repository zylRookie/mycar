package com.example.zyl.dqcar.moudels.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseLoadMoreAdapter;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mine.CarDetailActivity;
import com.example.zyl.dqcar.moudels.bean.MailDetailBean;
import com.example.zyl.dqcar.moudels.bean.ReleaseListBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author: Zhaoyl
 * Date: 2017/7/14 9:56
 * Description: 网上门店
 * PackageName: OnlineStoreActivity
 * Copyright: 端趣网络
 **/

public class OnlineStoreActivity extends BaseActivity implements View.OnClickListener {

    List<ReleaseListBean.PtPtoductModelLstBean> list = new ArrayList<>();
    TextView viewBlue, viewBlueTwo, tvUserTitle, tvNull;
    RecyclerView recyclerView;
    PtrFrameLayout ptrFrameLayout;
    MyAdapter myAdapter;
    boolean aBoolean = false;
    ImageView ivUserHead;
    String userId, url;
    int cur = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_onlinestore;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvBaseLeft)).setText("网上门店");
        recyclerView = $(R.id.recyclerView_online);
        viewBlue = $(R.id.viewBlue);
        viewBlueTwo = $(R.id.viewBlueTwo);
        tvUserTitle = $(R.id.tvUserTitle);
        ivUserHead = $(R.id.ivUserHead);
        ptrFrameLayout = $(R.id.ptrFrameLayout);
        tvNull = $(R.id.tvNull);
        $(R.id.flTwoCar).setOnClickListener(this);
        $(R.id.flOldGoods).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        viewBlue.setVisibility(View.VISIBLE);
        userId = getIntent().getStringExtra("userId");
        getData();
        getLinearLayoutManager();
        myAdapter = new MyAdapter(OnlineStoreActivity.this, list);
        recyclerView.setAdapter(myAdapter);
        url = WebAPI.MsgApi.MINE_FINDCARSBYUSERID;
        initRefresh(url);
        getOnData(cur = 1, url);
    }

    private void getOnData(final int index, String url) {
        HttpManager.getInstance().get(url
                        + "?userId=" + userId
                        + "&pageId=" + index
                , new HttpManager.ResponseCallback<ReleaseListBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseListBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (index == 1) {
                                if (o.ptPtoductModelLst != null && o.ptPtoductModelLst.size() > 0) {
                                    myAdapter.setData(o.ptPtoductModelLst);
                                    tvNull.setVisibility(View.GONE);
                                    ptrFrameLayout.setVisibility(View.VISIBLE);
                                    ptrFrameLayout.refreshComplete();
                                } else {
                                    tvNull.setVisibility(View.VISIBLE);
                                    ptrFrameLayout.setVisibility(View.GONE);
                                }
                            } else {
                                Log.e("AAA", "onSuccess: " + o.ptPtoductModelLst.size());
                                if (o.ptPtoductModelLst == null || o.ptPtoductModelLst.size() == 0) {
                                    ToastUtil.getInstance().show("已加载所有数据!");
                                    myAdapter.notifyLoadMoreCompleted();
                                } else {
                                    myAdapter.addData(o.ptPtoductModelLst);
                                    cur++;
                                }

                            }
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }

    private void initRefresh(final String url) {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(OnlineStoreActivity.this);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                list.clear();
                getOnData(cur = 1, url);
            }
        });

        myAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                list.clear();
                getOnData(cur + 1, url);
            }
        });
    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_FINDLISTUSER
                        + "?userId=" + userId
                , new HttpManager.ResponseCallback<MailDetailBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MailDetailBean o) {
                        if (o.errorCode.equals("0000")) {
                            tvUserTitle.setText(o.smUserModel.name);
                            if (!CheckUtil.isNull(o.smUserModel.imgKey))
                                Picasso.with(OnlineStoreActivity.this).load(o.smUserModel.imgKey).transform(new CircleTransform()).into(ivUserHead);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.flTwoCar:
                viewBlue.setVisibility(View.VISIBLE);
                viewBlueTwo.setVisibility(View.GONE);
                aBoolean = false;
                url = WebAPI.MsgApi.MINE_FINDCARSBYUSERID;
                getOnData(cur = 1, url);
                initRefresh(url);
                break;
            case R.id.flOldGoods:
                viewBlue.setVisibility(View.GONE);
                viewBlueTwo.setVisibility(View.VISIBLE);
                aBoolean = true;
                url = WebAPI.MsgApi.MINE_FINDGOODSBYUSERID;
                getOnData(cur = 1, url);
                initRefresh(url);
                break;
        }
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OnlineStoreActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class MyAdapter extends BaseLoadMoreAdapter<VH> {

        Context mContext;
        LayoutInflater layoutInflater;
        List<ReleaseListBean.PtPtoductModelLstBean> mDatas;

        public MyAdapter(Context context, List<ReleaseListBean.PtPtoductModelLstBean> list) {
            this.mContext = context;
            this.mDatas = list;
            layoutInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<ReleaseListBean.PtPtoductModelLstBean> list) {
            mDatas = list;
            notifyDataSetChanged();
        }

        public void addData(List<ReleaseListBean.PtPtoductModelLstBean> list) {
            int count = mDatas.size();
            mDatas.addAll(list);
            notifyItemRangeInserted(count + 1, list.size());
        }

        @Override
        protected int getItemViewTypeImpl(int position) {
            return 0;
        }

        @Override
        protected int getItemCountImpl() {
            return mDatas.size() > 0 ? mDatas.size() : 0;
        }

        //暂时先分开，数据源来了之后删掉一个
        @Override
        protected ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
            View mView = layoutInflater.inflate(R.layout.layout_item_release_findcar, parent, false);
            return new ViewHolder(mView);
        }

        @Override
        protected void onBindViewHolderImpl(final VH holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (mDatas.get(position).imgLst != null && mDatas.get(position).imgLst.size() > 0)
                Picasso.with(mContext).load(mDatas.get(position).imgLst.get(0)).into(viewHolder.ivCar);
            viewHolder.tvOldGoodsTitle.setText(mDatas.get(position).title);
            viewHolder.tvTime.setText(mDatas.get(position).createdTime);
            viewHolder.tvMoney.setText("￥" + mDatas.get(position).priceStr);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!aBoolean) {//二手车
                        Intent intent = new Intent(mContext, CarDetailActivity.class);
                        intent.putExtra("type", "oldCarOnLine");
                        intent.putExtra("productId", mDatas.get(position).id);
                        mContext.startActivity(intent);
                    } else {//商品
                        Intent intent = new Intent(mContext, CarDetailActivity.class);
                        intent.putExtra("type", "findGoodsOnLine");
                        intent.putExtra("productId", mDatas.get(position).id);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public class ViewHolder extends VH {

            ImageView ivCar;
            TextView tvOldGoodsTitle;
            TextView tvTime;
            TextView tvMoney;

            public ViewHolder(View itemView) {
                super(itemView);
                ivCar = $(R.id.ivCar);
                tvOldGoodsTitle = $(R.id.tvOldGoodsTitle);
                tvTime = $(R.id.tvTime);
                tvMoney = $(R.id.tvMoney);
            }
        }
    }
}
