package com.example.zyl.dqcar.moudels.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mail.UserNameDetailActivity;
import com.example.zyl.dqcar.moudels.activity.msg.ImagePagerActivity;
import com.example.zyl.dqcar.moudels.activity.riders.VideoPlayerActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.CustomShapeTransformation;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.BubbleImageView;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.hadcn.keyboard.utils.EmoticonLoader;

/**
 * Author: Zhaoyl
 * Date: 2017/9/06 20:30
 * Description: 聊天
 * PackageName: DqChatAdapter
 * Copyright: 端趣网络
 **/

public class DqChatAdapter extends RecyclerView.Adapter<VH> {

    List<AnimationDrawable> mAnimationDrawables;
    List<DqChatBean.ItemsBean> mDatas = new ArrayList<>();
    Context mContext;
    View pre;
    String types;

    public DqChatAdapter() {
        mAnimationDrawables = new ArrayList<>();
    }

    public void setData(Context context, List<DqChatBean.ItemsBean> items, String type) {
        mContext = context;
        mDatas = items;
        types = type;
        notifyDataSetChanged();
    }

    public void addData(Context context, List<DqChatBean.ItemsBean> items, String type) {
        mContext = context;
        types = type;
        mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public void addOneData(Context context, List<DqChatBean.ItemsBean> items, String type) {
        mContext = context;
        mDatas = items;
        types = type;
        notifyDataSetChanged();
//        notifyItemInserted(0);

    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).User.id.equals(BaseSharedPreferences.getId(mContext))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
        switch (viewType) {
            case 0://右边，自己
                holder = new RightViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_list_right, parent, false));
                break;
            case 1://左边
                holder = new LeftViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_list_left, parent, false));
                break;
            default:
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.setIsRecyclable(false);
        holder.bindData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class LeftViewHolder extends VH {

        ImageView ivChatAvatar, ivChatVideoType;
        TextView tvChatContent, tvChatTime, tvChatName, tvChatRecordLength, tvOnLineStore;
        //FrameLayout flChatVideo;
        FrameLayout flChatPictureLayout;
        LinearLayout llChatRecord;
        RelativeLayout chat_item_layout_content;
        BubbleImageView bivChatPicture;

        public LeftViewHolder(View itemView) {
            super(itemView);
            ivChatAvatar = $(R.id.ivChatAvatar);
            tvChatTime = $(R.id.tvChatTime);
            tvChatContent = $(R.id.tvChatContent);
            llChatRecord = $(R.id.llChatRecord);
            chat_item_layout_content = $(R.id.chat_item_layout_content);
            flChatPictureLayout = $(R.id.flChatPictureLayout);
            bivChatPicture = $(R.id.bivChatPicture);
            ivChatVideoType = $(R.id.ivChatVideoType);
            tvChatName = $(R.id.tvChatName);
            tvChatRecordLength = $(R.id.tvChatRecordLength);
            tvOnLineStore = $(R.id.tvOnLineStore);
        }

        @Override
        public void bindData(Object o, final int pos) {
            super.bindData(o, pos);
            final DqChatBean.ItemsBean datas = (DqChatBean.ItemsBean) o;
            if (CheckUtil.isNull(datas.CreateTimeStr))
                tvChatTime.setVisibility(View.GONE);
            else {
                tvChatTime.setVisibility(View.VISIBLE);
                tvChatTime.setText(datas.CreateTimeStr);
            }
            if (types.equals("groupChat")) {//群聊
                tvChatName.setText(datas.User.name);
                tvChatName.setVisibility(View.VISIBLE);
            }
            flChatPictureLayout.setTag(datas.viedoImg);
            llChatRecord.setTag(datas.ImFileModel);
            ivChatAvatar.setTag(datas.User.imgKey);
            if (!CheckUtil.isNull(datas.User.imgKey)) {
                if (TextUtils.equals((String) ivChatAvatar.getTag(), datas.User.imgKey))
                    Picasso.with(mContext).load(datas.User.imgKey).transform(new CircleTransform()).into(ivChatAvatar);
            }
            ivChatAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, UserNameDetailActivity.class);
                    in.putExtra("userId", datas.User.id);
                    mContext.startActivity(in);
                }
            });
            switch (datas.States) {
                case 0://文字
                    chat_item_layout_content.setVisibility(View.VISIBLE);
                    flChatPictureLayout.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatRecordLength.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.VISIBLE);
                    tvChatContent.setText(datas.Content);
                    chat_item_layout_content.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            chat_item_layout_content.getLocationOnScreen(location);
                            int x = location[0];
                            int y = location[1];
                            Log.e("AAA", "onLongClick: ----->x=" + x);
                            Log.e("AAA", "onLongClick: ----->y=" + y);
                            showPopTop(chat_item_layout_content, y, x, datas.Content, pos, datas.MsgId, "left");
                            return true;
                        }
                    });
                    break;
                case 1://图片
                    chat_item_layout_content.setVisibility(View.GONE);
                    flChatPictureLayout.setVisibility(View.VISIBLE);
                    ivChatVideoType.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    int res = R.mipmap.chat_from_bg_normal;
//                    if (TextUtils.equals((String) ivChatPicture.getTag(), datas.ImFileModel))
//                        Picasso.with(mContext).load(datas.ImFileModel).into(ivChatPicture);
                    Glide.with(mContext).load(datas.ImFileModel + "?x-oss-process=image/resize,h_200").transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
                    flChatPictureLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent in = new Intent(mContext, ImagePagerActivity.class);
                            in.putExtra("pvUrl", datas.ImFileModel);
                            in.putExtra("type", "pv");
                            mContext.startActivity(in);
                        }
                    });
                    break;
                case 2://语音
                    chat_item_layout_content.setVisibility(View.VISIBLE);
                    flChatPictureLayout.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.VISIBLE);
                    tvChatRecordLength.setVisibility(View.VISIBLE);
                    tvChatContent.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvChatRecordLength.getLayoutParams();
                    params.width = 80 + datas.length * 5;
                    tvChatRecordLength.setLayoutParams(params);
                    tvChatRecordLength.setText(datas.length + "" + "''");
                    chat_item_layout_content.setOnClickListener(new View.OnClickListener() {
                        boolean play;

                        @Override
                        public void onClick(View view) {
                            final AnimationDrawable animationDrawable = (AnimationDrawable) llChatRecord.getBackground();
                            play = !play;
                            if (play) {
                                //重置动画
                                resetAnim(animationDrawable);
                                animationDrawable.start();
                                MediaPlayerHelper.getInstance().start(datas.ImFileModel, true, new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        //重置动画
                                        animationDrawable.selectDrawable(0);//显示动画第一帧
                                        animationDrawable.stop();
                                        play = false;
                                    }
                                });
                                if (pre != null) {
                                    resetAnim(animationDrawable);
                                    animationDrawable.start();
                                }
                                pre = view;
                            } else {
                                MediaPlayerHelper.getInstance().stop();
                                animationDrawable.selectDrawable(0);//显示动画第一帧
                                animationDrawable.stop();
                                pre = null;
                            }
                        }
                    });

                    break;
                case 3://视频
                    chat_item_layout_content.setVisibility(View.GONE);
                    flChatPictureLayout.setVisibility(View.VISIBLE);
                    ivChatVideoType.setVisibility(View.VISIBLE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    res = R.mipmap.chat_from_bg_normal;
                    if (TextUtils.equals((String) flChatPictureLayout.getTag(), datas.viedoImg)) {
                        Glide.with(mContext).load(datas.viedoImg + "?x-oss-process=image/resize,h_200").transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bitmap = getBitmap.createVideoThumbnail(datas.ImFileModel, 100, 100);
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                                final byte[] bytes = baos.toByteArray();
//                                bivChatPicture.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        int res = R.mipmap.chat_to_bg_normal;
//                                        Glide.with(mContext).load(bytes).transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
//                                    }
//                                });
//                            }
//                        }).start();
                    }
                    flChatPictureLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent in = new Intent(mContext, VideoPlayerActivity.class);
                            in.putExtra("videoPath", datas.ImFileModel);
                            in.putExtra("videoImage", datas.viedoImg);
                            mContext.startActivity(in);
                        }
                    });
                    break;
                case 6://红包
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    break;
            }
            tvOnLineStore.setText("网上门店(" + datas.User.carNum + ")");
            switch (datas.User.roleType) {
                case 1:
                    tvOnLineStore.setBackgroundResource(R.color.Aa9a9a9);
                    break;
                case 2:
                    tvOnLineStore.setBackgroundResource(R.color.myBlue);
                    break;
                default:
                    tvOnLineStore.setBackgroundResource(R.color.onLine);
                    break;
            }
            tvOnLineStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, OnlineStoreActivity.class);
                    in.putExtra("userId", datas.User.id);
                    mContext.startActivity(in);
                }
            });
        }
    }


    public class RightViewHolder extends VH {

        ImageView ivChatAvatar, ivChatVideoType, ivChatEmoticons;
        TextView tvChatContent, tvChatTime, tvChatRecordLength;
        FrameLayout flChatPictureLayout;
        LinearLayout llChatRecord;
        RelativeLayout chat_item_layout_content;
        BubbleImageView bivChatPicture;
//        ivChatPicture,flChatVideo, ivChatVideo

        public RightViewHolder(View itemView) {
            super(itemView);
            ivChatAvatar = $(R.id.ivChatAvatar);
            tvChatContent = $(R.id.tvChatContent);
            tvChatTime = $(R.id.tvChatTime);
            llChatRecord = $(R.id.llChatRecord);
            chat_item_layout_content = $(R.id.chat_item_layout_content);
            flChatPictureLayout = $(R.id.flChatPictureLayout);
            bivChatPicture = $(R.id.bivChatPicture);
            ivChatVideoType = $(R.id.ivChatVideoType);
            tvChatRecordLength = $(R.id.tvChatRecordLength);
            ivChatEmoticons = $(R.id.ivChatEmoticons);
        }

        @Override
        public void bindData(Object o, final int pos) {
            super.bindData(o, pos);
            final DqChatBean.ItemsBean datas = (DqChatBean.ItemsBean) o;
            if (CheckUtil.isNull(datas.CreateTimeStr))
                tvChatTime.setVisibility(View.GONE);
            else {
                tvChatTime.setVisibility(View.VISIBLE);
                tvChatTime.setText(datas.CreateTimeStr);
            }
//            ivChatPicture.setTag(datas.ImFileModel);
            flChatPictureLayout.setTag(datas.viedoImg);
            llChatRecord.setTag(datas.ImFileModel);
            ivChatAvatar.setTag(datas.User.imgKey);
            if (!CheckUtil.isNull(datas.User.imgKey)) {
                if (TextUtils.equals((String) ivChatAvatar.getTag(), datas.User.imgKey))
                    Picasso.with(mContext).load(datas.User.imgKey).transform(new CircleTransform()).into(ivChatAvatar);
            }
            ivChatAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, UserNameDetailActivity.class);
                    in.putExtra("userId", datas.User.id);
                    mContext.startActivity(in);
                }
            });
            Log.e("AAA", "bindData: ---->states=" + datas.States + "");
            switch (datas.States) {
                case 0://文字
                    ivChatEmoticons.setVisibility(View.GONE);
                    chat_item_layout_content.setVisibility(View.VISIBLE);
                    flChatPictureLayout.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.VISIBLE);
                    tvChatRecordLength.setVisibility(View.GONE);
                    tvChatContent.setText(datas.Content);
                    chat_item_layout_content.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            chat_item_layout_content.getLocationOnScreen(location);
                            int x = location[0];
                            int y = location[1];
                            Log.e("AAA", "onLongClick: ----->x=" + x);
                            Log.e("AAA", "onLongClick: ----->y=" + y);
                            showPopTop(chat_item_layout_content, y, x, datas.Content, pos, datas.MsgId, "right");
                            return true;
                        }
                    });
                    break;
                case 1://图片
                    ivChatEmoticons.setVisibility(View.GONE);
                    chat_item_layout_content.setVisibility(View.GONE);
                    flChatPictureLayout.setVisibility(View.VISIBLE);
                    ivChatVideoType.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    int res = R.mipmap.chat_to_bg_normal;
//                    if (TextUtils.equals((String) ivChatPicture.getTag(), datas.ImFileModel))
//                        Picasso.with(mContext).load(datas.ImFileModel).into(ivChatPicture);
                    Glide.with(mContext).load(datas.ImFileModel + "?x-oss-process=image/resize,h_200").transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
                    flChatPictureLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent in = new Intent(mContext, ImagePagerActivity.class);
                            in.putExtra("pvUrl", datas.ImFileModel);
                            in.putExtra("type", "pv");
                            mContext.startActivity(in);
                        }
                    });
                    break;
                case 2://语音
                    ivChatEmoticons.setVisibility(View.GONE);
                    chat_item_layout_content.setVisibility(View.VISIBLE);
                    flChatPictureLayout.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.VISIBLE);
                    tvChatRecordLength.setVisibility(View.VISIBLE);
                    tvChatContent.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvChatRecordLength.getLayoutParams();
                    params.width = 80 + datas.length * 5;
                    tvChatRecordLength.setLayoutParams(params);
                    tvChatRecordLength.setText(datas.length + "" + "''");
                    chat_item_layout_content.setOnClickListener(new View.OnClickListener() {
                        boolean play;

                        @Override
                        public void onClick(View view) {
                            final AnimationDrawable animationDrawable = (AnimationDrawable) llChatRecord.getBackground();
                            play = !play;
                            if (play) {
                                //重置动画
                                resetAnim(animationDrawable);
                                animationDrawable.start();
                                MediaPlayerHelper.getInstance().start(datas.ImFileModel, true, new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        //重置动画
                                        animationDrawable.selectDrawable(0);//显示动画第一帧
                                        animationDrawable.stop();
                                        play = false;
                                    }
                                });
                                if (pre != null) {
                                    resetAnim(animationDrawable);
                                    animationDrawable.start();
                                }
                                pre = view;
                            } else {
                                MediaPlayerHelper.getInstance().stop();
                                animationDrawable.selectDrawable(0);//显示动画第一帧
                                animationDrawable.stop();
                                pre = null;
                            }
                        }
                    });
                    break;
                case 3://视频
                    ivChatEmoticons.setVisibility(View.GONE);
                    chat_item_layout_content.setVisibility(View.GONE);
                    flChatPictureLayout.setVisibility(View.VISIBLE);
                    ivChatVideoType.setVisibility(View.VISIBLE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    res = R.mipmap.chat_to_bg_normal;
                    if (TextUtils.equals((String) flChatPictureLayout.getTag(), datas.viedoImg)) {
                        Glide.with(mContext).load(datas.viedoImg + "?x-oss-process=image/resize,h_200").transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bitmap = getBitmap.createVideoThumbnail(datas.ImFileModel, 100, 100);
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                                final byte[] bytes = baos.toByteArray();
//                                bivChatPicture.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        int res = R.mipmap.chat_to_bg_normal;
//                                        Glide.with(mContext).load(bytes).transform(new CustomShapeTransformation(mContext, res)).into(bivChatPicture);
//                                    }
//                                });
//                            }
//                        }).start();
                    }
                    flChatPictureLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent in = new Intent(mContext, VideoPlayerActivity.class);
                            in.putExtra("videoPath", datas.ImFileModel);
                            in.putExtra("videoImage", datas.viedoImg);
                            mContext.startActivity(in);
                        }
                    });
                    break;
                case 6://红包
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    break;
                case 7://表情图片
                    chat_item_layout_content.setVisibility(View.GONE);
                    flChatPictureLayout.setVisibility(View.GONE);
                    ivChatVideoType.setVisibility(View.GONE);
                    llChatRecord.setVisibility(View.GONE);
                    tvChatContent.setVisibility(View.GONE);
                    ivChatEmoticons.setVisibility(View.VISIBLE);
                    ivChatEmoticons.setImageDrawable(EmoticonLoader.getInstance(mContext).getDrawableByTag(datas.Content));
                    break;
            }
        }
    }

    private void resetAnim(AnimationDrawable animationDrawable) {
        if (!mAnimationDrawables.contains(animationDrawable)) {
            mAnimationDrawables.add(animationDrawable);
        }
        for (AnimationDrawable ad : mAnimationDrawables) {
            ad.selectDrawable(0);
            ad.stop();
        }
    }

    CustomPopWindow popWindow;

    private void showPopTop(View mButton2, int y, int x, String content, int pos, String MsgId, String leftAndRight) {
        if (y > 370) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_layout2, null);
            handleLogic(contentView, content, pos, MsgId);
            popWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                    .setView(contentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .create();
            if (leftAndRight.equals("left")) {
//                if (x < 210)
//                    popWindow.showAsDropDown(mButton2, 90, -(mButton2.getHeight() + popWindow.getHeight()));
//                else
                popWindow.showAsDropDown(mButton2, 0, -(mButton2.getHeight() + popWindow.getHeight()));
            } else {
                if (x > 410)
                    popWindow.showAsDropDown(mButton2, -90, -(mButton2.getHeight() + popWindow.getHeight()));
                else
                    popWindow.showAsDropDown(mButton2, 0, -(mButton2.getHeight() + popWindow.getHeight()));
            }
        } else {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_layout1, null);
            handleLogic(contentView, content, pos, MsgId);
            popWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                    .setView(contentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .create();
            if (leftAndRight.equals("left")) {
//                if (x < 210)
//                    popWindow.showAsDropDown(mButton2, 90, 0);
//                else
                popWindow.showAsDropDown(mButton2, 0, 0);
            } else {
                if (x > 410)
                    popWindow.showAsDropDown(mButton2, -90, 0);
                else
                    popWindow.showAsDropDown(mButton2, 0, 0);
            }


        }
    }

    private void handleLogic(View contentView, final String content, final int pos, final String MsgId) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.tvCopy:
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(content);
                        ToastUtil.getInstance().show("复制成功");
                        break;
                    case R.id.tvDelete:
                        deleteOneMsg(MsgId, pos);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tvCopy).setOnClickListener(listener);
        contentView.findViewById(R.id.tvDelete).setOnClickListener(listener);
    }

    private void deleteOneMsg(String msgId, final int pos) {
        HttpManager.getInstance().get(WebAPI.ChatApi.CHATAPI_FECALLMSG
                        + "?msgId=" + msgId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            mDatas.remove(pos);
                            notifyDataSetChanged();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


}
