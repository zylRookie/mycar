package com.example.zyl.dqcar.moudels.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyl.dqcar.common.VH;

/**
 * Author: Zhaoyl
 * Date: 2017/7/10 10:56
 * Description: 横向的Adapter
 * PackageName: MyHAdapter
 * Copyright: 端趣网络
 **/

public class MyHAdapter extends RecyclerView.Adapter<MyHAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends VH {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
