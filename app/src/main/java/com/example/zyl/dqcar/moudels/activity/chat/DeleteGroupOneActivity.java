package com.example.zyl.dqcar.moudels.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.AddGroupBean;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.UserNameBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.DividerItemDecoration;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Zhaoyl
 * Date: 2017/9/20 19:56
 * Description: 删除群成员
 * PackageName: DeleteGroupOneActivity
 * Copyright: 端趣网络
 **/

public class DeleteGroupOneActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRv;
    private SwipeDelMenuAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<UserNameBean> mDatas = new ArrayList<>();
    private Map<Integer, String> map = new HashMap<>();
    private List<String> entUsers = new ArrayList<>();

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private String groupId;

    @Override
    public int getLayout() {
        return R.layout.activity_deletegroupone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        groupId = getIntent().getStringExtra("groupId");
        initView();
    }


    private void initView() {

        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.rlRight).setOnClickListener(this);
        mRv = $(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(DeleteGroupOneActivity.this));
        mAdapter = new SwipeDelMenuAdapter(DeleteGroupOneActivity.this, mDatas);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(DeleteGroupOneActivity.this, mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(DeleteGroupOneActivity.this, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        mTvSideBarHint = $(R.id.tvSideBarHint);//HintTextView
        mIndexBar = $(R.id.indexBar);//IndexBar
        //加载数据
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
                                initDatas(o.items);
                            }
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    /**
     * 组织数据源
     *
     * @param
     * @return
     */
    private void initDatas(final List<AddGroupBean.ItemsBean> items) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            UserNameBean cityBean = new UserNameBean();
            if (!items.get(i).userId.equals(BaseSharedPreferences.getId(DeleteGroupOneActivity.this))) {
                cityBean.setCity(items.get(i).name);//设置名称
                cityBean.setImgKey(items.get(i).perImgKey);//设置头像
                cityBean.setUserId(items.get(i).userId);//设置id
                mDatas.add(cityBean);
            }
        }
        mAdapter.setDatas(mDatas);
        mAdapter.notifyDataSetChanged();

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mDatas)
                .setTitleFontSize((int) getResources().getDimension(R.dimen.px24dp))
                .setColorTitleBg(getResources().getColor(R.color.AF6F6F6))
                .setColorTitleFont(getResources().getColor(R.color.A999999))
                .setmTitleHeight((int) getResources().getDimension(R.dimen.px42dp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.rlRight:
                if (map.size() == 0) {
                    ToastUtil.getInstance().show("请选择删除成员");
                    return;
                }
                for (String ent : map.values()) {
                    entUsers.add(ent);
                }
                commit();
                break;
        }
    }

    private void commit() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("groupId", groupId);
        maps.put("entUsers", entUsers);
        JSONObject json = new JSONObject(maps);
        String jsons = json.toJSONString();
        HttpManager.getInstance().postJson(WebAPI.ChatApi.CHATAPI_DELETEBYGROUPANDUSER, jsons
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("删除成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.DELETE_GROUPS));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    /**
     * 和CityAdapter 一模一样，只是修改了 Item的布局
     * Created by zhangxutong .
     * Date: 16/08/28
     */

    private class SwipeDelMenuAdapter extends RecyclerView.Adapter<VH> {

        Context mContext;
        List<UserNameBean> mDatas;

        public SwipeDelMenuAdapter(Context mContext, List<UserNameBean> mData) {
            this.mContext = mContext;
            this.mDatas = mData;
        }

        public void setDatas(List<UserNameBean> data) {
            this.mDatas = data;
        }


        @Override
        public int getItemCount() {
            Log.e("AAA", "getItemCount: " + mDatas.size());
            return mDatas != null && mDatas.size() > 0 ? mDatas.size() : 0;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_newmenu, parent, false));
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            holder.bindData(mDatas.get(position), position);
        }

        public class ContentViewHolder extends VH {
            LinearLayout content;
            ImageView ivAvatar;
            TextView tvCity;
            CheckBox cbAddFriends;
            boolean isOk;

            public ContentViewHolder(View itemView) {
                super(itemView);
                content = $(R.id.content);
                ivAvatar = $(R.id.ivAvatar);
                tvCity = $(R.id.tvCity);
                cbAddFriends = $(R.id.cbAddFriends);
            }

            @Override
            public void bindData(final Object o, final int pos) {
                final UserNameBean mData = (UserNameBean) o;
                if (!CheckUtil.isNull(mData.getImgKey()))
                    Picasso.with(itemView.getContext()).load(mData.getImgKey()).into(ivAvatar);
                else
                    ivAvatar.setImageResource(R.mipmap.newdefhead);
                if (!CheckUtil.isNull(mData.getCity()))
                    tvCity.setText(mData.getCity());
                cbAddFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        isOk = b;
                        if (b) {
                            map.put(pos, mData.getUserId());
                        } else {
                            map.remove(pos);
                        }
                    }
                });
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbAddFriends.setChecked(isOk ? false : true);
                    }
                });
            }
        }

    }
}
