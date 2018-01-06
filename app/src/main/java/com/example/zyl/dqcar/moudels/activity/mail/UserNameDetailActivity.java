package com.example.zyl.dqcar.moudels.activity.mail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
import com.example.zyl.dqcar.moudels.activity.mine.MyPhoneActivity;
import com.example.zyl.dqcar.moudels.bean.MailDetailBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/5 13:56
 * Description: 个人详细信息
 * PackageName: UserNameDetailActivity
 * Copyright: 端趣网络
 **/

public class UserNameDetailActivity extends BaseActivity implements View.OnClickListener {


    RecyclerView recyclerView;
    ImageView ivMailDetailHead;
    TextView tvMailDetailName, tvMailDetailPhone, tvMailDetailBak;
    String userId, userName, sourceUserId;

    @Override
    public int getLayout() {
        return R.layout.activity_username_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userId = getIntent().getStringExtra("userId");
        ((TextView) $(R.id.tvBaseLeft)).setText("详细资料");
        recyclerView = $(R.id.recyclerView_H);
        ivMailDetailHead = $(R.id.ivMailDetailHead);
        tvMailDetailName = $(R.id.tvMailDetailName);
        tvMailDetailPhone = $(R.id.tvMailDetailPhone);
        tvMailDetailBak = $(R.id.tvMailDetailBak);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.btnMailDetailSendMsg).setOnClickListener(this);

        getLinearLayoutManager();
        getData();

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
                            if (!CheckUtil.isNull(o.smUserModel.imgKey))
                                Picasso.with(UserNameDetailActivity.this).load(o.smUserModel.imgKey).into(ivMailDetailHead);
                            if (!CheckUtil.isNull(o.smUserModel.name))
                                tvMailDetailName.setText(o.smUserModel.name);
                            if (!CheckUtil.isNull(o.smUserModel.phone))
                                tvMailDetailPhone.setText("车汇宝号：" + o.smUserModel.phone);
                            if (!CheckUtil.isNull(o.smUserModel.bak))
                                tvMailDetailBak.setText(o.smUserModel.bak);
                            if (o.cirleImages != null && o.cirleImages.size() > 0) {
                                MyAdapter myAdapter = new MyAdapter();
                                recyclerView.setAdapter(myAdapter);
                                myAdapter.setData(o.cirleImages);
                            }
                            userName = o.smUserModel.name;
                            sourceUserId = o.smUserModel.id;
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
            case R.id.btnMailDetailSendMsg://发消息
                if (!CheckUtil.isNull(sourceUserId)) {
                    Intent in = new Intent(UserNameDetailActivity.this, DqChatActivity.class);
                    in.putExtra("userName", userName);
                    in.putExtra("type", "oneChat");
                    in.putExtra("sourceUserId", sourceUserId);
                    startActivity(in);
                } else
                    ToastUtil.getInstance().show("暂时不能发送");
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {

        List<String> mDatas;

        public void setData(List<String> cirleImages) {
            mDatas = cirleImages;
            notifyDataSetChanged();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(UserNameDetailActivity.this).inflate(R.layout.layout_item_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.bindData(mDatas.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size() > 3 ? 3 : mDatas.size();
        }

        public class ViewHolder extends VH {

            ImageView ivMailDetailPhoto;

            public ViewHolder(View itemView) {
                super(itemView);
                ivMailDetailPhoto = $(R.id.ivMailDetailPhoto);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(UserNameDetailActivity.this, MyPhoneActivity.class);
                        in.putExtra("userId", sourceUserId);
                        startActivity(in);
                    }
                });
            }

            @Override
            public void bindData(Object o, int pos) {
                super.bindData(o, pos);
                String datas = (String) o;
                if (!CheckUtil.isNull(datas))
                    Picasso.with(itemView.getContext()).load(datas).into(ivMailDetailPhoto);
            }
        }

    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserNameDetailActivity.this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
