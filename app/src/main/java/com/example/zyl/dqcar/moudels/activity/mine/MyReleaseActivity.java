package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseLoadMoreAdapter;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.adapter.ReleaseAdapter;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.ReleaseListBean;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 11:35
 * Description: 我的发布
 * PackageName: MyReleaseActivity
 * Copyright: 端趣网络
 **/

public class MyReleaseActivity extends BaseActivity implements View.OnClickListener {

    TextView viewBlue, viewBlueTwo, tvNull;
    RecyclerView recyclerView;
    boolean aBoolean = false;
    ReleaseAdapter releaseAdapter;
    PtrFrameLayout ptrFrameLayout;
    private int cur = 1;
    private String url;
    List<ReleaseListBean.PtPtoductModelLstBean> list = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_myrelease;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvBaseLeft)).setText("我的发布");
        ((ImageView) $(R.id.ivRight)).setImageResource(R.mipmap.writea_about);
        tvNull = $(R.id.tvNull);
        recyclerView = $(R.id.recyclerView_myRelease);
        viewBlue = $(R.id.viewBlue);
        viewBlueTwo = $(R.id.viewBlueTwo);
        ptrFrameLayout = $(R.id.ptrFrameLayout);
        $(R.id.flTwoCar).setOnClickListener(this);
        $(R.id.flOldGoods).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.rlRight).setOnClickListener(this);

        viewBlue.setVisibility(View.VISIBLE);

        getLinearLayoutManager();
        releaseAdapter = new ReleaseAdapter(MyReleaseActivity.this, list);
        recyclerView.setAdapter(releaseAdapter);
        url = WebAPI.MineApi.MINE_MYRELEASELIST;
        initRefresh(url, "twoCar");
        initData(cur = 1, url, "twoCar");
    }

    private void initRefresh(final String url, final String flag) {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(MyReleaseActivity.this);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                list.clear();
                initData(cur = 1, url, flag);
            }
        });

        releaseAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                list.clear();
                initData(cur + 1, url, flag);
            }
        });

        releaseAdapter.setOnClick(new ReleaseAdapter.CallBackOnClick() {
            @Override
            public void callBack(int position, int saleType, String id) {
                Intent in = new Intent(MyReleaseActivity.this, ReleaseOldCarActivity.class);
                in.putExtra("type", saleType + "");
                in.putExtra("id", id);
                startActivity(in);
            }

            @Override
            public void callBackState(int position, int state, String id, Button btnState) {
                modifyProductState(id, state, btnState);
            }
        });
    }

    private void modifyProductState(String id, final int state, final Button btnState) {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_MODIFYPRODUCTSTATE
                        + "?productId=" + id
                        + "&sale=" + state
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {


                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (state == 1)
                                btnState.setText("上架");
                            else
                                btnState.setText("下架");
                            ToastUtil.getInstance().show("成功！");
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }


    private void initData(final int index, String url, final String flag) {

        HttpManager.getInstance().get(url + "?pageId=" + index + ""
                , new HttpManager.ResponseCallback<ReleaseListBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseListBean o) {
                        if (o.errorCode.equals("0000")) {

                            if (index == 1) {
                                if (o.ptPtoductModelLst != null && o.ptPtoductModelLst.size() > 0) {
                                    releaseAdapter.setData(o.ptPtoductModelLst, flag);
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
                                    releaseAdapter.notifyLoadMoreCompleted();
                                } else {
                                    releaseAdapter.addData(o.ptPtoductModelLst, flag);
                                    cur++;
                                }
                            }
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
            case R.id.flTwoCar:
                viewBlue.setVisibility(View.VISIBLE);
                viewBlueTwo.setVisibility(View.GONE);
                aBoolean = false;
                url = WebAPI.MineApi.MINE_MYRELEASELIST;
                initData(cur = 1, url, "twoCar");
                initRefresh(url, "twoCar");
                break;
            case R.id.flOldGoods:
                viewBlue.setVisibility(View.GONE);
                viewBlueTwo.setVisibility(View.VISIBLE);
                aBoolean = true;
                url = WebAPI.MineApi.MINE_MYRELEASELISTGOODS;
                initData(cur = 1, url, "oldGoods");
                initRefresh(url, "oldGoods");
                break;
            case R.id.rlRight:
                Intent in = new Intent(MyReleaseActivity.this, ReleaseOldCarActivity.class);
                if (!aBoolean) {
                    in.putExtra("type", "1");
                    in.putExtra("id", "");
                    startActivity(in);
                } else {
                    in.putExtra("type", "2");
                    in.putExtra("id", "");
                    startActivity(in);
                }
                break;
        }
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyReleaseActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.UPDATA_OLDCAR) {
            String type = (String) eventCenter.getData();
            if (type.equals("1")) {
                viewBlue.setVisibility(View.VISIBLE);
                viewBlueTwo.setVisibility(View.GONE);
                aBoolean = false;
                url = WebAPI.MineApi.MINE_MYRELEASELIST;
                initData(cur = 1, url, "twoCar");
                initRefresh(url, "twoCar");
            } else {
                viewBlue.setVisibility(View.GONE);
                viewBlueTwo.setVisibility(View.VISIBLE);
                aBoolean = true;
                url = WebAPI.MineApi.MINE_MYRELEASELISTGOODS;
                initData(cur = 1, url, "oldGoods");
                initRefresh(url, "oldGoods");
            }
        }
    }
}
