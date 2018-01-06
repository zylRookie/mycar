package com.example.zyl.dqcar.moudels.activity.mail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.adapter.MyGroupAdapter;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.MyGroupBean;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/9/13 7:56
 * Description: 交流群
 * PackageName: WeChatGroupActivity
 * Copyright: 端趣网络
 **/

public class WeChatGroupActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MyGroupAdapter myGroupAdapter;
    String type;

    @Override
    public int getLayout() {
        return R.layout.activity_wechatgroup;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        type = getIntent().getStringExtra("type");
        ((TextView) $(R.id.tvTitle)).setText("交流群");
        recyclerView = $(R.id.recyclerView_weGroup);
        $(R.id.ivBaseBack).setOnClickListener(this);

        getLinearLayoutManager();
        myGroupAdapter = new MyGroupAdapter();
        myGroupAdapter.setJoinGroup(new MyGroupAdapter.OnCallBack() {
            @Override
            public void setOnCallBack(String groupId) {
                getJoinGroup(groupId);
            }
        });
        recyclerView.setAdapter(myGroupAdapter);
        getData("noJoin");
    }

    private void getData(final String types) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_FINDGROUPLISTBYTYPE
                        + "?type=" + type
                , new HttpManager.ResponseCallback<MyGroupBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MyGroupBean o) {
                        if (o.errorCode.equals("0000")) {
                            myGroupAdapter.setData(o.items, types);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void getJoinGroup(String groupId) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_JOINGROUP
                        + "?groupId=" + groupId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("加入成功！");
                            getData("noJoin");
                            myGroupAdapter.notifyDataSetChanged();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WeChatGroupActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
