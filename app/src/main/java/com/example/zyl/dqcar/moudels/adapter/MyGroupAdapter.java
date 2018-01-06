package com.example.zyl.dqcar.moudels.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
import com.example.zyl.dqcar.moudels.bean.MyGroupBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/28 17:56
 * Description: 群组
 * PackageName: MyGroupAdapter
 * Copyright: 端趣网络
 **/

public class MyGroupAdapter extends RecyclerView.Adapter<VH> {

    List<MyGroupBean.ItemsBean> mDatas;
    String type;

    public void setData(List<MyGroupBean.ItemsBean> items, String types) {
        mDatas = items;
        type = types;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_mygroup, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class MyViewHolder extends VH {

        ImageView ivAvatar;
        TextView tvGroupContent;
        Button btnMyGroupJoin;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivAvatar = $(R.id.ivAvatar);
            tvGroupContent = $(R.id.tvGroupContent);
            btnMyGroupJoin = $(R.id.btnMyGroupJoin);
        }

        @Override
        public void bindData(Object o, int pos) {
            super.bindData(o, pos);
            final MyGroupBean.ItemsBean datas = (MyGroupBean.ItemsBean) o;
            if (type.equals("noJoin"))
                btnMyGroupJoin.setVisibility(View.VISIBLE);
            else if (type.equals("join")) {
                btnMyGroupJoin.setVisibility(View.GONE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(itemView.getContext(), DqChatActivity.class);
                        in.putExtra("userName", datas.nameStr);
                        in.putExtra("type", "groupChat");
                        in.putExtra("sourceUserId", datas.id);
                        itemView.getContext().startActivity(in);
                    }
                });
            }

            if (!CheckUtil.isNull(datas.imgKey))
                Picasso.with(itemView.getContext()).load(datas.imgKey).into(ivAvatar);
            tvGroupContent.setText(datas.nameStr + "(" + datas.groupNum + "" + ")");
            btnMyGroupJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCallBack.setOnCallBack(datas.id);
                }
            });
        }
    }

    public interface OnCallBack {
        void setOnCallBack(String groupId);
    }

    OnCallBack onCallBack;

    public void setJoinGroup(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

}
