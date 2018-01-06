package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseLoadMoreAdapter;
import com.example.zyl.dqcar.common.VH;

/**
 * Author: Zhaoyl
 * Date: 2017/7/3 10:15
 * Description: 闲置商品
 * PackageName: FindGoodsAdapter
 * Copyright: 端趣网络
 **/

public class FindGoodsAdapter extends BaseLoadMoreAdapter {

    Context mContext;
    LayoutInflater layoutInflater;

    public FindGoodsAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getItemViewTypeImpl(int position) {
        return 0;
    }

    @Override
    protected int getItemCountImpl() {
        return 15;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View mView = layoutInflater.inflate(R.layout.layout_item_gift, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    protected void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CarDetailActivity.class);
//                intent.putExtra("type", "findGoods");
////                intent.putExtra("productId",)
//                mContext.startActivity(intent);
//            }
//        });
    }

    public class ViewHolder extends VH {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
