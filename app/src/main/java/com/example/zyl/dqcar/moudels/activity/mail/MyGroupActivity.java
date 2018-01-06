package com.example.zyl.dqcar.moudels.activity.mail;

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
import com.example.zyl.dqcar.moudels.adapter.MyGroupAdapter;
import com.example.zyl.dqcar.moudels.bean.MyGroupBean;
import com.example.zyl.dqcar.utils.ToastUtil;

/**
 * Author: Zhaoyl
 * Date: 2017/8/28 16:56
 * Description: 群组
 * PackageName: MyGroupActivity
 * Copyright: 端趣网络
 **/

public class MyGroupActivity extends BaseActivity implements View.OnClickListener {

    TextView viewBlue, viewBlueTwo;
    RecyclerView recyclerView;
    RecyclerView recyclerViewTwo;
    MyGroupAdapter myGroupAdapter;
    int groupImage[] = {
            R.mipmap.nojoin_news,
            R.mipmap.nojoin_public,
            R.mipmap.nojoin_one,
            R.mipmap.nojoin_two,
            R.mipmap.nojoin_three,
            R.mipmap.nojoin_four,
            R.mipmap.nojoin_five,
            R.mipmap.nojoin_six,
            R.mipmap.nojoin_end};

    String groupName[] = {
            "新人报道大厅",
            "官方活动大厅",
            "华东区社群",
            "华南区社群",
            "华中区社群",
            "华北区社群",
            "西北区社群",
            "西南区社群",
            "东北区社群"
    };

    @Override
    public int getLayout() {
        return R.layout.activiy_mygroup;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvTitle)).setText("群聊");
        viewBlue = $(R.id.viewBlue);
        viewBlueTwo = $(R.id.viewBlueTwo);
        recyclerView = $(R.id.recyclerView_myGroup);
        recyclerViewTwo = $(R.id.recyclerView_myGroupTwo);
        $(R.id.flTwoCar).setOnClickListener(this);
        $(R.id.flOldGoods).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        viewBlue.setVisibility(View.VISIBLE);


        getLinearLayoutManager();
        myGroupAdapter = new MyGroupAdapter();
        recyclerView.setAdapter(myGroupAdapter);
//        myGroupAdapter.setJoinGroup(new MyGroupAdapter.OnCallBack() {
//            @Override
//            public void setOnCallBack(String groupId) {
//                getJoinGroup(groupId);
//            }
//        });
        recyclerView.setVisibility(View.VISIBLE);
        getData(1, "join");
    }


    private void getData(int isIn, final String type) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_FINDGROUPLISTBYUSERID
                        + "?isIn=" + isIn
                , new HttpManager.ResponseCallback<MyGroupBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MyGroupBean o) {
                        if (o.errorCode.equals("0000")) {
                            myGroupAdapter.setData(o.items, type);
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
            case R.id.flTwoCar://已加入
                viewBlue.setVisibility(View.VISIBLE);
                viewBlueTwo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewTwo.setVisibility(View.GONE);
                getData(1, "join");
                break;
            case R.id.flOldGoods://未加入
                viewBlue.setVisibility(View.GONE);
                viewBlueTwo.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerViewTwo.setVisibility(View.VISIBLE);
                getLinearLayoutManagerTwo();
                MyAdapter myAdapter = new MyAdapter();
                recyclerViewTwo.setAdapter(myAdapter);
//                getData(0, "noJoin");
                break;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_mygroup_def, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.ivAvatar.setImageResource(groupImage[position]);
            viewHolder.tvGroupContent.setText(groupName[position]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(MyGroupActivity.this, WeChatGroupActivity.class);
                    in.putExtra("type", position + 1 + "");
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount() {
            return groupImage.length;
        }

        public class ViewHolder extends VH {

            ImageView ivAvatar;
            TextView tvGroupContent;

            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = $(R.id.ivAvatar);
                tvGroupContent = $(R.id.tvGroupContent);
            }

        }


    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyGroupActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //设置线性布局的管理者
    private void getLinearLayoutManagerTwo() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyGroupActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTwo.setLayoutManager(linearLayoutManager);
    }


}
