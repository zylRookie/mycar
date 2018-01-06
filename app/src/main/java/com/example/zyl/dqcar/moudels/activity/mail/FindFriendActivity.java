package com.example.zyl.dqcar.moudels.activity.mail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.FindBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/10 11:16
 * Description: 查找好友
 * PackageName: FindFriendActivity
 * Copyright: 端趣网络
 **/

public class FindFriendActivity extends BaseActivity implements View.OnClickListener {

    EditText etnSearchFriends;
    RecyclerView recyclerView;

    @Override
    public int getLayout() {
        return R.layout.activity_findfriend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("添加好友");
        etnSearchFriends = $(R.id.etnSearchFriends);
        recyclerView = $(R.id.recyclerView_findCarMan);
        $(R.id.etnSearchFriendsNext).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

        getLinearLayoutManager();

        etnSearchFriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        next();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.etnSearchFriendsNext:
                next();
                break;
        }
    }

    public void next() {
        if (CheckUtil.isNull(etnSearchFriends.getText().toString())) {
            ToastUtil.getInstance().show("号码不能为空");
            return;
        }
        getFindCarMan(etnSearchFriends.getText().toString());
    }

    private void getFindCarMan(String name) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_FINDBYNAMEORPHONE
                        + "?page=1"
                        + "&name=" + name
                , new HttpManager.ResponseCallback<FindBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(FindBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (o.items != null && o.items.size() > 0) {
                                MyAdapter myAdapter = new MyAdapter();
                                recyclerView.setAdapter(myAdapter);
                                myAdapter.setData(o.items);
                            } else {
                                ToastUtil.getInstance().show("该用户不存在！");
                            }
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindFriendActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {
        List<FindBean.ItemsBean> mDatas;

        public void setData(List<FindBean.ItemsBean> items) {
            mDatas = items;
            notifyDataSetChanged();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_findfriend, parent, false));
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

            ImageView ivAvatar;
            TextView tvFindUserName;
            TextView tvFindAddd;

            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = $(R.id.ivAvatar);
                tvFindUserName = $(R.id.tvFindUserName);
                tvFindAddd = $(R.id.tvFindAddd);
            }

            @Override
            public void bindData(Object o, int pos) {
                super.bindData(o, pos);
                final FindBean.ItemsBean datas = (FindBean.ItemsBean) o;
                if (!CheckUtil.isNull(datas.imgKey))
                    Picasso.with(itemView.getContext()).load(datas.imgKey).into(ivAvatar);
                if (!CheckUtil.isNull(datas.name))
                    tvFindUserName.setText(datas.name);
                tvFindAddd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAdd(datas.id);
                    }
                });
            }
        }
    }

    private void getAdd(String userId) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_ADDFRIEND
                        + "?userId=" + userId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("添加好友成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.ADD_FRIENDS));
                            EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_RIDERS));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }
}
