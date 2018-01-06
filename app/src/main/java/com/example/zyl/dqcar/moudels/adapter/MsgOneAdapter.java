//package com.example.zyl.dqcar.moudels.adapter;
//
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.zyl.dqcar.R;
//import com.example.zyl.dqcar.common.VH;
//import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
//import com.example.zyl.dqcar.moudels.bean.MsgAllBean;
//import com.example.zyl.dqcar.utils.CheckUtil;
//import com.makeramen.roundedimageview.RoundedImageView;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
///**
// * Author: Zhaoyl
// * Date: 2017/7/5 15:20
// * Description: 消息适配器
// * PackageName: MsgAdapter
// * Copyright: 端趣网络
// **/
//
//public class MsgOneAdapter extends RecyclerView.Adapter<VH> {
//
//    List<MsgAllBean.ItemsBean> mDatas;
//
//    public void setData(List<MsgAllBean.ItemsBean> items) {
//        mDatas = items;
//        Log.e("AAA", "setData: " + items.size());
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msg_content, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(VH holder, int position) {
//        holder.bindData(mDatas.get(position), position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDatas != null ? mDatas.size() : 0;
//    }
//
//    public class ContentHolder extends VH {
//
//        RoundedImageView ivMsgHead;
//        TextView tvMsgTitle, tvMsgContent, tvMsgTime, tvMessageNum;
//
//        public ContentHolder(View itemView) {
//            super(itemView);
//            ivMsgHead = $(R.id.ivMsgHead);
//            tvMsgTitle = $(R.id.tvMsgTitle);
//            tvMsgContent = $(R.id.tvMsgContent);
//            tvMsgTime = $(R.id.tvMsgTime);
//            tvMessageNum = $(R.id.tvMessageNum);
//        }
//
//        @Override
//        public void bindData(Object o, int pos) {
//            super.bindData(o, pos);
//            final MsgAllBean.ItemsBean datas = (MsgAllBean.ItemsBean) o;
//            tvMessageNum.setVisibility(datas.unReadCount == 0 ? View.GONE : View.VISIBLE);
//            tvMessageNum.setText(datas.unReadCount + "");
//            if (!CheckUtil.isNull(datas.title))
//                tvMsgTitle.setText(datas.title);
//            if (!CheckUtil.isNull(datas.content))
//                tvMsgContent.setText(datas.content);
//            if (!CheckUtil.isNull(datas.updatedTime))
//                tvMsgTime.setText(datas.updatedTime);
//            if (!CheckUtil.isNull(datas.logoUrl))
//                Picasso.with(itemView.getContext()).load(datas.logoUrl).into(ivMsgHead);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent in = new Intent(itemView.getContext(), DqChatActivity.class);
//                    in.putExtra("userName", datas.title);
//                    in.putExtra("type", "oneChat");
//                    in.putExtra("sourceUserId", datas.sourceUserId);
//                    itemView.getContext().startActivity(in);
//                }
//            });
//        }
//    }
//
//}
