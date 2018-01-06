package com.example.zyl.dqcar.moudels.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mail.UserNameDetailActivity;
import com.example.zyl.dqcar.moudels.activity.riders.RecyclerItemClickListener;
import com.example.zyl.dqcar.moudels.bean.AddGroupBean;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/9/11 7:30
 * Description: 群组
 * PackageName: DqChatGroupActivity
 * Copyright: 端趣网络
 **/

public class DqChatGroupActivity extends BaseActivity implements View.OnClickListener {

    MyAdapter myAdapter;
    RecyclerView recyclerView;
    TextView tvTitle;
    String groupId;

    @Override
    public int getLayout() {
        return R.layout.activity_dqchatgroup;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    List<String> listData = new ArrayList<>();

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DqChatGroupActivity.this, 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (myAdapter.getItemViewType(position) == myAdapter.TYPE_PHOTO) {
                    Intent in = new Intent(DqChatGroupActivity.this, UserNameDetailActivity.class);
                    in.putExtra("userId", listData.get(position));
                    startActivity(in);
                } else if (myAdapter.getItemViewType(position) == myAdapter.TYPE_ADD) {
                    Intent in = new Intent(DqChatGroupActivity.this, MailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) listData);
                    in.putExtras(bundle);
                    in.putExtra("groupId", groupId);
                    startActivity(in);
                } else if (myAdapter.getItemViewType(position) == myAdapter.TYPE_DELETE) {
                    if (!CheckUtil.isNull(BaseSharedPreferences.getRoleType(DqChatGroupActivity.this))) {
                        if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatGroupActivity.this)) >= 5) {
                            Intent in = new Intent(DqChatGroupActivity.this, DeleteGroupOneActivity.class);
                            in.putExtra("groupId", groupId);
                            startActivity(in);
                            ToastUtil.getInstance().show("删除成员");
                        }
                    }

                }
            }
        }));
        getData();
    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.ChatApi.CHATAPI_FINDGROUPUSERLIST
                        + "?groupId=" + groupId
                , new HttpManager.ResponseCallback<AddGroupBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(AddGroupBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (o.items != null && o.items.size() > 0) {
                                tvTitle.setText("聊天信息(" + o.total + "" + ")");
                                myAdapter.setData(o.items);
                                recyclerView.setAdapter(myAdapter);
                                for (int i = 0; i < o.items.size(); i++) {
                                    listData.add(o.items.get(i).userId);
                                }
                            }
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void initView() {
        tvTitle = $(R.id.tvTitle);
        tvTitle.setText("聊天信息()");
        recyclerView = $(R.id.recyclerView_addGroup);
        $(R.id.btnOutGroup).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        groupId = getIntent().getStringExtra("groupId");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOutGroup:
                outGroup();
                break;
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }

    //个人退出
    private void outGroup() {
        HttpManager.getInstance().get(WebAPI.ChatApi.CHATAPI_DELETEBYGROUPANDUSERSINGLE
                        + "?groupId=" + groupId
                        + "&userId=" + BaseSharedPreferences.getId(DqChatGroupActivity.this)
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.DELETE_GROUP));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }
    //

    public class MyAdapter extends RecyclerView.Adapter<VH> {

        public final static int TYPE_ADD = 1;
        public final static int TYPE_PHOTO = 2;
        public final static int TYPE_DELETE = 3;
        List<AddGroupBean.ItemsBean> mDatas;

        public void setData(List<AddGroupBean.ItemsBean> items) {
            mDatas = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDatas.size() + 1) {
                return TYPE_DELETE;
            } else if (position == mDatas.size()) {
                return TYPE_ADD;
            } else {
                return TYPE_PHOTO;
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            switch (viewType) {
                case TYPE_ADD:
                    itemView = LayoutInflater.from(DqChatGroupActivity.this).inflate(R.layout.layout_item_add, parent, false);
                    break;
                case TYPE_DELETE:
                    itemView = LayoutInflater.from(DqChatGroupActivity.this).inflate(R.layout.layout_item_delete, parent, false);
                    break;
                case TYPE_PHOTO:
                    itemView = LayoutInflater.from(DqChatGroupActivity.this).inflate(R.layout.layout_item_grouphead, parent, false);
                    break;
            }
//            return new MyAddGroupViewHolder(LayoutInflater.from(DqChatGroupActivity.this).inflate(R.layout.layout_item_grouphead, parent, false));
            return new MyAddGroupViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (getItemViewType(position) == TYPE_PHOTO) {
                holder.bindData(mDatas.get(position), position);
            } else if (getItemViewType(position) == TYPE_DELETE) {
                MyAddGroupViewHolder myAddGroupViewHolder = (MyAddGroupViewHolder) holder;
                if (!CheckUtil.isNull(BaseSharedPreferences.getRoleType(DqChatGroupActivity.this))) {
                    if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatGroupActivity.this)) >= 5)
                        myAddGroupViewHolder.ivGroupDelete.setVisibility(View.VISIBLE);
                    else
                        myAddGroupViewHolder.ivGroupDelete.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mDatas != null && mDatas.size() > 0 ? mDatas.size() + 2 : 0;
        }

        public class MyAddGroupViewHolder extends VH {

            RoundedImageView ivGroupHead;
            ImageView ivGroupAdd;
            ImageView ivGroupDelete;
            LinearLayout rlBig, llDelete;
            TextView tvUserNameGroup;

            public MyAddGroupViewHolder(View itemView) {
                super(itemView);
                ivGroupHead = $(R.id.ivGroupHead);
                ivGroupAdd = $(R.id.ivGroupAdd);
                ivGroupDelete = $(R.id.ivGroupDelete);
                rlBig = $(R.id.rlBig);
                llDelete = $(R.id.llDelete);
                tvUserNameGroup = $(R.id.tvUserNameGroup);
            }

            @Override
            public void bindData(Object o, int pos) {
                super.bindData(o, pos);
                AddGroupBean.ItemsBean datas = (AddGroupBean.ItemsBean) o;
//                ivGroupHead.setLayoutParams(new LinearLayout.LayoutParams(w, w) );
//                rlBig.setLayoutParams(new LinearLayout.LayoutParams(w, w));
                WindowManager wm = (WindowManager) getSystemService(DqChatGroupActivity.this.WINDOW_SERVICE);
                int w = (wm.getDefaultDisplay().getWidth() - 300) / 5;
                if (!CheckUtil.isNull(datas.perImgKey))
                    Glide.with(DqChatGroupActivity.this).load(datas.perImgKey).override(w, w).into(ivGroupHead);
                else {
                    Glide.with(DqChatGroupActivity.this).load(R.mipmap.newdefhead).override(w, w).into(ivGroupHead);
                }
                if (!CheckUtil.isNull(datas.name))
                    tvUserNameGroup.setText(datas.name);

            }
        }

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.ADD_GROUP) {
            getData();
        } else if (eventCenter.getEventCode() == EventBusConst.DELETE_GROUPS) {
            getData();
        }
    }
}
