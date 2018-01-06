package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Date: 2017/6/29 14:25
 * Description: 我的发布
 * PackageName: ReleaseAdapter
 * Copyright: 端趣网络
 **/

public class ReleaseAdapter extends BaseLoadMoreAdapter<ReleaseAdapter.ViewHolder> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<ReleaseListBean.PtPtoductModelLstBean> mDatas;
    String flag;

    public ReleaseAdapter(Context context, List<ReleaseListBean.PtPtoductModelLstBean> list) {
        this.mContext = context;
        this.mDatas = list;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ReleaseListBean.PtPtoductModelLstBean> list, String flag) {
        this.flag = flag;
        mDatas = list;
        notifyDataSetChanged();
    }

    public void addData(List<ReleaseListBean.PtPtoductModelLstBean> list, String flag) {
        this.flag = flag;
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
        View mView = layoutInflater.inflate(R.layout.layout_item_release_new, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    protected void onBindViewHolderImpl(final ViewHolder holder, final int position) {
        if (mDatas.get(position).imgLst != null && mDatas.get(position).imgLst.size() > 0)
            Picasso.with(mContext).load(mDatas.get(position).imgLst.get(0)).into(holder.ivCar);
        holder.tvOldGoodsTitle.setText(mDatas.get(position).title);
        holder.tvTime.setText(mDatas.get(position).createdTime);
        holder.tvMoney.setText("￥" + mDatas.get(position).priceStr);
        if (mDatas.get(position).state == 2) {
            holder.btnState.setText("上架");
        } else if (mDatas.get(position).state == 1) {
            holder.btnState.setText("下架");
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackOnClick.callBack(position, mDatas.get(position).saleType, mDatas.get(position).id);
            }
        });

        holder.btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackOnClick.callBackState(position, mDatas.get(position).state, mDatas.get(position).id, holder.btnState);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("twoCar")) {
                    Intent intent = new Intent(mContext, CarDetailActivity.class);
                    intent.putExtra("type", "oldCar");
                    intent.putExtra("productId", mDatas.get(position).id);
                    mContext.startActivity(intent);
                } else if (flag.equals("oldGoods")) {
                    Intent intent = new Intent(mContext, CarDetailActivity.class);
                    intent.putExtra("type", "findGoods");
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
        Button btnState;
        Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCar = $(R.id.ivCar);
            tvOldGoodsTitle = $(R.id.tvOldGoodsTitle);
            tvTime = $(R.id.tvTime);
            tvMoney = $(R.id.tvMoney);
            btnState = $(R.id.btnState);
            btnEdit = $(R.id.btnEdit);
        }
    }

    public interface CallBackOnClick {
        void callBack(int position, int saleType, String id);

        void callBackState(int position, int state, String id, Button btnState);
    }

    CallBackOnClick callBackOnClick;

    public void setOnClick(CallBackOnClick callBackOnClick) {
        this.callBackOnClick = callBackOnClick;
    }
}
