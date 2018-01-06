package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.riders.VideoPlayerActivity;
import com.example.zyl.dqcar.moudels.bean.RidersBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.GetBitmap;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/30 9:56
 * Description: 我的相册
 * PackageName: MyPhotoAdapter
 * Copyright: 端趣网络
 **/

public class MyPhotoAdapter extends RecyclerView.Adapter<VH> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<RidersBean.CircleForListModelListBean> mDatas;
    GetBitmap getBitmap;

    public MyPhotoAdapter(Context context, List<RidersBean.CircleForListModelListBean> circleForListModelList) {
        this.mContext = context;
        this.mDatas = circleForListModelList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<RidersBean.CircleForListModelListBean> circleForListModelList) {
        mDatas = circleForListModelList;
        notifyDataSetChanged();
    }

    public void addData(List<RidersBean.CircleForListModelListBean> circleForListModelList) {
        mDatas.addAll(circleForListModelList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        getBitmap = new GetBitmap();
        VH holder = null;
        switch (viewType) {
            case 0:
                holder = new HeadHolder(layoutInflater.inflate(R.layout.layout_photo_header, parent, false));
                break;
            case 1:
                holder = new ContentHolder(layoutInflater.inflate(R.layout.layout_photo_content, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position > 0)
            holder.bindData(mDatas.get(position - 1), position);
        else
            holder.bindData(mDatas, position);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() + 1 : 0;
    }

    public class HeadHolder extends VH {

        TextView tvMyName;
        ImageView ivMyHead;

        public HeadHolder(View itemView) {
            super(itemView);
            tvMyName = $(R.id.tvMyName);
            ivMyHead = $(R.id.ivMyHead);
        }

        @Override
        public void bindData(Object o, int pos) {
            super.bindData(o, pos);
            List<RidersBean.CircleForListModelListBean> data = (List<RidersBean.CircleForListModelListBean>) o;
            if (data != null && data.size() > 0) {
                tvMyName.setText(data.get(0).user.name);
                Picasso.with(itemView.getContext()).load(data.get(0).user.imgKey).into(ivMyHead);
            }
        }
    }


    public class ContentHolder extends VH {
        TextView tvDay;
        TextView tvMonth;
        TextView tvRidersContent;
        RecyclerView layout_photo;
        FrameLayout flPhotoMovie;
        FrameLayout flVideo;
        ImageView ivVideo;

        public ContentHolder(View itemView) {
            super(itemView);
            tvDay = $(R.id.tvDay);
            tvMonth = $(R.id.tvMonth);
            tvRidersContent = $(R.id.tvRidersContent);
            layout_photo = $(R.id.layout_photo);
            flVideo = $(R.id.flVideo);
            ivVideo = $(R.id.ivVideo);
            flPhotoMovie = $(R.id.flPhotoMovie);
        }

        @Override
        public void bindData(Object o, int pos) {
            super.bindData(o, pos);
            final RidersBean.CircleForListModelListBean data = (RidersBean.CircleForListModelListBean) o;
            if (!CheckUtil.isNull(data.publishTimeString)) {
                String times = data.publishTimeString;
                String[] time = times.split("-");
                tvMonth.setText(Integer.parseInt(time[1]) + "" + "月");
                tvDay.setText(time[2]);
            }
            if (!CheckUtil.isNull(data.content)) {
                tvRidersContent.setVisibility(View.VISIBLE);
                tvRidersContent.setText(data.content);
            } else
                tvRidersContent.setVisibility(View.GONE);
            if (data.files != null && data.files.size() > 0) {
                if (data.fileType == 2) {
                    Picasso.with(mContext).load(data.viedoImg).into(ivVideo);
                    flVideo.setVisibility(View.VISIBLE);
                    layout_photo.setVisibility(View.GONE);
                } else {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    layout_photo.setLayoutManager(new GridLayoutManager(mContext, 3));
                    NewGridViewAdapter GriViewAdapter = new NewGridViewAdapter(mContext, data.files);
                    layout_photo.setAdapter(GriViewAdapter);
                    flVideo.setVisibility(View.GONE);
                    layout_photo.setVisibility(View.VISIBLE);
                }
                flPhotoMovie.setVisibility(View.VISIBLE);
            } else
                flPhotoMovie.setVisibility(View.GONE);
            flVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, VideoPlayerActivity.class);
                    in.putExtra("videoPath", data.files.get(0));
                    in.putExtra("videoImage", data.viedoImg);
                    mContext.startActivity(in);
                }
            });
        }
    }

}
