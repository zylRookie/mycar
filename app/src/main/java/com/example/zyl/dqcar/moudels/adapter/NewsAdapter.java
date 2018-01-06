package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.mine.NewsContentDetailActivity;
import com.example.zyl.dqcar.moudels.bean.NewsListBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/29 17:25
 * Description: 新闻中心
 * PackageName: NewsAdapter
 * Copyright: 端趣网络
 **/

public class NewsAdapter extends RecyclerView.Adapter<VH> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<NewsListBean.ItemsBean> mDatas;

    public NewsAdapter(Context context, List<NewsListBean.ItemsBean> list) {
        this.mContext = context;
        this.mDatas = list;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<NewsListBean.ItemsBean> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    public void addData(List<NewsListBean.ItemsBean> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends VH {
        ImageView ivNewsUrl;
        TextView tvNewsTitle;
        TextView tvNewsDetail;
        TextView tvNewsTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ivNewsUrl = $(R.id.ivNewsUrl);
            tvNewsTitle = $(R.id.tvNewsTitle);
            tvNewsDetail = $(R.id.tvNewsDetail);
            tvNewsTime = $(R.id.tvNewsTime);
        }

        @Override
        public void bindData(Object o, int pos) {
            super.bindData(o, pos);
            NewsListBean.ItemsBean itemsBean = (NewsListBean.ItemsBean) o;
            if (!CheckUtil.isNull(itemsBean.title))
                tvNewsTitle.setText(itemsBean.title);
            if (!CheckUtil.isNull(itemsBean.detail))
                tvNewsDetail.setText(itemsBean.detail);
            if (!CheckUtil.isNull(itemsBean.createdTime))
                tvNewsTime.setText(itemsBean.createdTime);
            if (itemsBean.imgLst != null && itemsBean.imgLst.size() > 0)
                Picasso.with(mContext).load(itemsBean.imgLst.get(0)).into(ivNewsUrl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, NewsContentDetailActivity.class));
                }
            });
        }
    }
}
