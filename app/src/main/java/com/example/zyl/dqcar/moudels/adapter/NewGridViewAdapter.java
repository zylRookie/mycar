package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.msg.ImagePagerActivity;
import com.example.zyl.dqcar.utils.CheckUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Author: Zhaoyl
 * Date: 2016/12/26 17:25
 * Description: 上传图片适配器
 * PackageName: NewGridViewAdapter
 * Copyright: 银点商城
 **/

public class NewGridViewAdapter extends RecyclerView.Adapter<NewGridViewAdapter.ViewHolder> {
    private List<String> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> list = new ArrayList<>();

    public NewGridViewAdapter(Context mcontext, List<String> mList) {
        this.mContext = mcontext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public NewGridViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.base_hrecycler_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(NewGridViewAdapter.ViewHolder holder, final int position) {
        WindowManager wm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        int w = (wm.getDefaultDisplay().getWidth() - 200) / 3;
        if (!CheckUtil.isNull(mList.get(position))) {
            Glide.with(mContext)
                    .load(mList.get(position) + "?x-oss-process=image/resize,h_200")
                    .override(w, w)
                    .into(holder.ivIcont);
        }
        list.add(mList.get(position));
        holder.ivIcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                in.putExtra("position", position + "");
                in.putExtra("type", "");
                in.putExtras(bundle);
                mContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() > 0 ? mList.size() : 0;
    }

    public class ViewHolder extends VH {
        ImageView ivIcont;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcont = $(R.id.iv_iconT);
        }
    }

}
