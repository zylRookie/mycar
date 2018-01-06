package com.example.zyl.dqcar.moudels.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.moudels.activity.mine.MyPhoneActivity;
import com.example.zyl.dqcar.moudels.activity.riders.VideoPlayerActivity;
import com.example.zyl.dqcar.moudels.bean.RidersBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/29 15:15
 * Description: 通讯录适配器
 * PackageName: RidersAdapter
 * Copyright: 端趣网络
 **/

public class RidersAdapter extends RecyclerView.Adapter<VH> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<RidersBean.CircleForListModelListBean> mDatas;
//    GetBitmap getBitmap;

    public RidersAdapter(Context context, List<RidersBean.CircleForListModelListBean> circleForListModelList) {
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
        VH holder = null;
        switch (viewType) {
            case 0:
                holder = new HeadHolder(layoutInflater.inflate(R.layout.layout_photo_header, parent, false));
                break;
            case 1:
                holder = new ContentViewHolder(layoutInflater.inflate(R.layout.layout_item_riders, parent, false));
                break;
        }
        return holder;
    }

//    @Override
//    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
////            List<Object> list = (List<Object>) payloads.get(0);
//            Log.e("AAA", "onBindViewHolder: " + payloads.get(0));
//            ContentViewHolder c = (ContentViewHolder) holder;
//            if (payloads.get(0).equals(true) || payloads.get(0).equals(false)) {
//                c.ivSbButton.setChecked((Boolean) payloads.get(0) ? true : false);
//                switch (mDatas.get(position - 1).likedUserList.size()) {
//                    case 0:
//                        if ((Boolean) payloads.get(0)) {
//                            c.tvZanName.setText(BaseSharedPreferences.getUserName(mContext));
//                            if (mDatas.get(position - 1).commentList.size() > 0)
//                                c.viewLine.setVisibility(View.VISIBLE);
//                            else
//                                c.viewLine.setVisibility(View.GONE);
//                            c.llComment.setVisibility(View.VISIBLE);
//                        } else {
//                            c.llComment.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 1:
//                        if (mDatas.get(position - 1).likedUserList.get(0).name.equals(BaseSharedPreferences.getUserName(mContext))) {
//                            if (!(Boolean) payloads.get(0)) {
//                                c.llComment.setVisibility(View.GONE);
//                                c.viewLine.setVisibility(View.GONE);
//                            } else {//代议
//                                c.llComment.setVisibility(View.VISIBLE);
//                                if (mDatas.get(position - 1).commentList.size() > 0)
//                                    c.viewLine.setVisibility(View.VISIBLE);
//                                else
//                                    c.viewLine.setVisibility(View.GONE);
//                            }
//                        } else {
//                            StringBuilder sb = new StringBuilder();
//                            for (RidersBean.CircleForListModelListBean.LikedUserListBean listBean : mDatas.get(position - 1).likedUserList) {
//                                sb.append(listBean.name + ",");
//                            }
//                            Log.e("AAA", "bindData: ----ee-->" + sb.toString());
//                            if ((Boolean) payloads.get(0)) {
//                                c.tvZanName.setText(BaseSharedPreferences.getUserName(mContext) + "," + sb.toString().substring(0, sb.toString().length() - 1));
//                            } else {
//                                c.tvZanName.setText(sb.toString().substring(0, sb.toString().length() - 1));
//                            }
//                        }
//                        break;
//                    default:
//                        Log.e("AAA", "onBindViewHolder:--=-=----->报错 ");
//                        StringBuilder sb = new StringBuilder();
//                        for (RidersBean.CircleForListModelListBean.LikedUserListBean listBean : mDatas.get(position - 1).likedUserList) {
//                            sb.append(listBean.name + ",");
//                        }
//                        if (mDatas.get(position - 1).likedUserList.get(0).name.equals(BaseSharedPreferences.getUserName(mContext))) {
//                            if (!(Boolean) payloads.get(0)) {
//                                StringBuilder sbr = new StringBuilder();
//                                for (int i = 0; i < mDatas.get(position - 1).likedUserList.size(); i++) {
//                                    if (i != 0)
//                                        sbr.append(mDatas.get(i).likedUserList.get(i).name + ",");
//                                }
//                                c.tvZanName.setText(sbr.toString().substring(0, sbr.toString().length() - 1));
//                            } else
//                                c.tvZanName.setText(sb.toString().substring(0, sb.toString().length() - 1));
//
//                        } else {
//                            Log.e("AAA", "bindData: ----ee-->" + sb.toString());
//                            if ((Boolean) payloads.get(0)) {
//                                c.tvZanName.setText(BaseSharedPreferences.getUserName(mContext) + "," + sb.toString().substring(0, sb.toString().length() - 1));
//                            } else {
//                                c.tvZanName.setText(sb.toString().substring(0, sb.toString().length() - 1));
//                            }
//                        }
//                        break;
//                }
//            } else {
////                mDatas.get(position-1).commentList.add();
////                MyAdapter adapter = new MyAdapter(mDatas.get(position-1).commentList);
//
//            }
//
//        }
//    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position > 0)
            holder.bindData(mDatas.get(position - 1), position);
        if (position == 0) {
            holder.bindData(mDatas, position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() + 1 : 0;
    }

    public class HeadHolder extends VH {

        TextView tvMyName;
        RoundedImageView ivMyHead;

        public HeadHolder(View itemView) {
            super(itemView);
            tvMyName = $(R.id.tvMyName);
            ivMyHead = $(R.id.ivMyHead);
        }

        @Override
        public void bindData(Object o, int pos) {
            super.bindData(o, pos);
            tvMyName.setText(BaseSharedPreferences.getUserName(mContext));
            if (BaseSharedPreferences.getUserHeadUrl(mContext) != null && BaseSharedPreferences.getUserHeadUrl(mContext) != "") {
                Picasso.with(mContext).load(BaseSharedPreferences.getUserHeadUrl(mContext)).into(ivMyHead);
            }
        }
    }


    public class ContentViewHolder extends VH {

        RoundedImageView ivRidersHead;
        TextView tvRidersName;
        TextView tvRidersContent;
        RecyclerView layout_photo;
        TextView tvRidersTime;
        FrameLayout flPhotoMovie;
        FrameLayout flVideo;
        ImageView ivVideo;
        ImageView ivComment;
        ShineButton ivSbButton;
        TextView tvZanName;
        TextView tvRidersDelete;
        LinearLayout llComment;
        RecyclerView recyclerView_comment;
        View viewLine;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ivRidersHead = $(R.id.ivRidersHead);
            tvRidersName = $(R.id.tvRidersName);
            tvRidersContent = $(R.id.tvRidersContent);
            layout_photo = $(R.id.layout_photo);
            flVideo = $(R.id.flVideo);
            ivVideo = $(R.id.ivVideo);
            ivComment = $(R.id.ivComment);
            ivSbButton = $(R.id.ivSbButton);
            tvRidersTime = $(R.id.tvRidersTime);
            flPhotoMovie = $(R.id.flPhotoMovie);
            tvZanName = $(R.id.tvZanName);
            llComment = $(R.id.llComment);
            viewLine = $(R.id.viewLine);
            tvRidersDelete = $(R.id.tvRidersDelete);
            recyclerView_comment = $(R.id.recyclerView_comment);
        }

        @Override
        public void bindData(Object o, final int pos) {
            super.bindData(o, pos);
            final RidersBean.CircleForListModelListBean data = (RidersBean.CircleForListModelListBean) o;
            if (!CheckUtil.isNull(data.user.imgKey))
                Picasso.with(mContext).load(data.user.imgKey).into(ivRidersHead);
            if (!CheckUtil.isNull(data.user.name))
                tvRidersName.setText(data.user.name);
            if (!CheckUtil.isNull(data.content)) {
                tvRidersContent.setVisibility(View.VISIBLE);
                tvRidersContent.setText(data.content);
            } else
                tvRidersContent.setVisibility(View.GONE);
            tvRidersDelete.setVisibility(data.user.id.equals(BaseSharedPreferences.getId(mContext)) ? View.VISIBLE : View.GONE);
            tvRidersDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCallbackListener.getDeleteAllOne(view, pos);
                }
            });
            if (data.files != null && data.files.size() > 0) {
                if (data.fileType == 2) {
                    Picasso.with(mContext).load(data.viedoImg + "?x-oss-process=image/resize,h_200").into(ivVideo);
                    flVideo.setVisibility(View.VISIBLE);
                    layout_photo.setVisibility(View.GONE);
                } else {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    layout_photo.setLayoutManager(new GridLayoutManager(mContext, 3));
                    NewGridViewAdapter GriViewAdapter = new NewGridViewAdapter(mContext, data.files);
//                    layout_photo.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                    layout_photo.setAdapter(GriViewAdapter);
                    flVideo.setVisibility(View.GONE);
                    layout_photo.setVisibility(View.VISIBLE);
                }
                flPhotoMovie.setVisibility(View.VISIBLE);
            } else
                flPhotoMovie.setVisibility(View.GONE);
            if (!CheckUtil.isNull(data.publishTimeString)) {
                tvRidersTime.setText(data.publishTimeString);
            }
            flVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, VideoPlayerActivity.class);
                    in.putExtra("videoPath", data.files.get(0));
                    in.putExtra("videoImage", data.viedoImg);
                    mContext.startActivity(in);
                }
            });
            Log.e("AAA", "bindData: ----gg-->" + data.hasLiked);
            ivSbButton.setChecked(data.hasLiked ? true : false);
            ivSbButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    if (checked) {
                        onCallbackListener.getChecked(view, pos);
                    } else {
                        onCallbackListener.getCheck(view, pos);
                    }
                }
            });
            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCallbackListener.getComment(view, pos);
                }
            });
            if (data.likedUserList != null && data.likedUserList.size() > 0) {
                llComment.setVisibility(View.VISIBLE);
                if (data.commentList.size() > 0)
                    viewLine.setVisibility(View.VISIBLE);
                else
                    viewLine.setVisibility(View.GONE);
                StringBuilder sb = new StringBuilder();
                for (RidersBean.CircleForListModelListBean.LikedUserListBean listBean : data.likedUserList) {
                    sb.append(listBean.name + "，");
                }
                tvZanName.setText(sb.toString().substring(0, sb.toString().length() - 1));
            } else {
                llComment.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
            if (data.commentList != null && data.commentList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView_comment.setLayoutManager(linearLayoutManager);
                recyclerView_comment.setAdapter(new MyAdapter(data.commentList, pos));
                recyclerView_comment.setVisibility(View.VISIBLE);
            } else {
                recyclerView_comment.setVisibility(View.GONE);
            }

            ivRidersHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, MyPhoneActivity.class);
                    in.putExtra("userId", data.user.id);
                    mContext.startActivity(in);
                }
            });

        }
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {
        List<RidersBean.CircleForListModelListBean.CommentListBean> commentList;
        int position;

        public MyAdapter(List<RidersBean.CircleForListModelListBean.CommentListBean> commentList, int pos) {
            this.commentList = commentList;
            this.position = pos;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_mycomment, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.bindData(commentList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return commentList != null && commentList.size() > 0 ? commentList.size() : 0;
        }

        public class MyViewHolder extends VH {
            TextView tvCommentContent;
            TextView tvCommentName;
            TextView tvCommentToName;
            TextView tvContentHuiFu;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvCommentName = $(R.id.tvCommentName);
                tvCommentToName = $(R.id.tvCommentToName);
                tvContentHuiFu = $(R.id.tvContentHuiFu);
                tvCommentContent = $(R.id.tvCommentContent);
            }

            @Override
            public void bindData(Object o, final int pos) {
                super.bindData(o, pos);
                final RidersBean.CircleForListModelListBean.CommentListBean mData = (RidersBean.CircleForListModelListBean.CommentListBean) o;
                if (mData.user != null)
                    tvCommentName.setText(mData.user.name);
                if (mData.toUser != null) {
                    tvCommentToName.setText(mData.toUser.name);
                    tvCommentToName.setVisibility(View.VISIBLE);
                    tvContentHuiFu.setVisibility(View.VISIBLE);
                }
                tvCommentContent.setText("：" + mData.content);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mData.user != null) {
                            if (mData.user.name.equals(BaseSharedPreferences.getUserName(mContext)))
                                //和自己名称一样，点击删除
                                onCallbackListener.getDelete(view, pos, position);
                            else //和自己名称不一样，点击回复
                                onCallbackListener.getBackSend(view, pos, position);
                        }
                    }
                });

            }
        }
    }

    public interface OnCallbackListener {
        void getChecked(View v, int p);

        void getCheck(View v, int p);

        void getComment(View v, int p);

        void getDelete(View v, int p, int pos);

        void getBackSend(View v, int p, int pos);

        void getDeleteAllOne(View v, int p);
    }

    private OnCallbackListener onCallbackListener;

    public void setOnclick(OnCallbackListener onCallbackListener) {
        this.onCallbackListener = onCallbackListener;
    }
}
