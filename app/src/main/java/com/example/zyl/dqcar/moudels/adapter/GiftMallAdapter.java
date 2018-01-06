package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseLoadMoreAdapter;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.mine.CarDetailActivity;
import com.example.zyl.dqcar.moudels.bean.ReleaseListBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/29 17:15
 * Description: 礼品商城
 * PackageName: GiftMallAdapter
 * Copyright: 端趣网络
 **/

public class GiftMallAdapter extends BaseLoadMoreAdapter<GiftMallAdapter.ViewHolder> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<ReleaseListBean.PtPtoductModelLstBean> mDatas;

    public GiftMallAdapter(Context context, List<ReleaseListBean.PtPtoductModelLstBean> list) {
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

    @Override
    protected ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View mView = layoutInflater.inflate(R.layout.layout_item_gift, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    protected void onBindViewHolderImpl(ViewHolder holder, final int position) {

        if (mDatas.get(position).imgLst != null && mDatas.get(position).imgLst.size() > 0)
            Picasso.with(mContext).load(mDatas.get(position).imgLst.get(0)).into(holder.ivGift);
        holder.tvGiftTitle.setText(mDatas.get(position).title);
        holder.tvGiftTime.setText(mDatas.get(position).createdTime);
        holder.tvGiftMoney.setText(mDatas.get(position).price + " 驹币");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CarDetailActivity.class);
                intent.putExtra("type", "gift");
                intent.putExtra("productId",mDatas.get(position).id);
                mContext.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends VH {
        ImageView ivGift;
        TextView tvGiftTitle;
        TextView tvGiftTime;
        TextView tvGiftMoney;


        public ViewHolder(View itemView) {
            super(itemView);
            ivGift = $(R.id.ivGift);
            tvGiftTitle = $(R.id.tvGiftTitle);
            tvGiftTime = $(R.id.tvGiftTime);
            tvGiftMoney = $(R.id.tvGiftMoney);
        }
    }
}