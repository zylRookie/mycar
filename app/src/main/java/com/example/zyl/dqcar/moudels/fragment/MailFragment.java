package com.example.zyl.dqcar.moudels.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseFragment;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mail.FindFriendActivity;
import com.example.zyl.dqcar.moudels.activity.mail.MyGroupActivity;
import com.example.zyl.dqcar.moudels.activity.mail.UserNameDetailActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.MailMyBean;
import com.example.zyl.dqcar.moudels.bean.UserNameBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.DividerItemDecoration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 通讯录
 */
public class MailFragment extends BaseFragment implements View.OnClickListener {


    private static final String TAG = "zxt";
    private static final String INDEX_STRING_TOP = "↑";
    private RecyclerView mRv;
    private SwipeDelMenuAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<UserNameBean> mDatas = new ArrayList<>();

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;


    @Override
    protected int getLayout() {
        return R.layout.fragment_mail;
    }

    public static MailFragment newInstance() {
        Bundle args = new Bundle();
        MailFragment fragment = new MailFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void onVisibleChange(boolean isVisible) {
//        super.onVisibleChange(isVisible);
//        if (mView != null) {
//            getData();
//        }
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        $(R.id.rlRight).setOnClickListener(this);
        ((TextView) $(R.id.tvTitle)).setText("通讯录");
        ((ImageView) $(R.id.ivRight)).setImageResource(R.mipmap.findfriends);
        mRv = $(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(getActivity()));
        mAdapter = new SwipeDelMenuAdapter(getActivity(), mDatas);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(getActivity(), mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        //使用indexBar
        mTvSideBarHint = $(R.id.tvSideBarHint);//HintTextView
        mIndexBar = $(R.id.indexBar);//IndexBar

        //加载数据
        getData();
    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_FINDMYFRIENDS
                , new HttpManager.ResponseCallback<MailMyBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MailMyBean o) {
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
    private void initDatas(final List<MailMyBean.ItemsBean> items) {
        mDatas = new ArrayList<>();
        //微信的头部 也是可以右侧IndexBar导航索引的，
        // 但是它不需要被ItemDecoration设一个标题titile
        mDatas.add((UserNameBean) new UserNameBean("交流大厅").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
        for (int i = 0; i < items.size(); i++) {
            UserNameBean cityBean = new UserNameBean();
            cityBean.setCity(items.get(i).userName);//设置名称
            cityBean.setImgKey(items.get(i).imgKey);//设置头像
            cityBean.setUserId(items.get(i).userId);//设置id
            cityBean.setIsInGroup(items.get(i).isInGroup);//设置群
            mDatas.add(cityBean);
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
            case R.id.rlRight:
                startActivity(new Intent(getActivity(), FindFriendActivity.class));
                break;
        }
    }


    /**
     * 和CityAdapter 一模一样，只是修改了 Item的布局
     * Created by zhangxutong .
     * Date: 16/08/28
     */

    private class SwipeDelMenuAdapter extends RecyclerView.Adapter<VH> {

        Context mContext;
        List<UserNameBean> mDatas;

        public SwipeDelMenuAdapter(Context mContext, List<UserNameBean> mDatas) {
            this.mContext = mContext;
            this.mDatas = mDatas;
        }

        public void setDatas(List<UserNameBean> datas) {
            this.mDatas = datas;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            Log.e("AAA", "getItemCount: " + mDatas.size());
            return mDatas != null && mDatas.size() > 0 ? mDatas.size() : 1;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH holder = null;
            switch (viewType) {
                case 0:
                    holder = new FirstHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_menu_head, parent, false));
                    break;
                case 1:
                    holder = new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_menu, parent, false));
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            if (position > 0)
                holder.bindData(mDatas.get(position), position);
        }

        public class ContentViewHolder extends VH {
            LinearLayout content;
            RoundedImageView ivAvatar;
            TextView tvCity;
            Button btnDel;

            public ContentViewHolder(View itemView) {
                super(itemView);
                content = $(R.id.content);
                ivAvatar = $(R.id.ivAvatar);
                tvCity = $(R.id.tvCity);
                btnDel = $(R.id.btnDel);
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
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(mContext, UserNameDetailActivity.class);
                        in.putExtra("userId", mData.getUserId());
                        startActivity(in);

                    }
                });
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((SwipeMenuLayout) itemView).quickClose();
                        mDatas.remove(pos);
                        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                                .setNeedRealIndex(false)//设置需要真实的索引
                                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                                .setmSourceDatas(mDatas)//设置数据
                                .invalidate();
                        notifyDataSetChanged();
                        getDeleteFriend(mData.getUserId());
                    }
                });

            }
        }

        public class FirstHolder extends VH {
            RoundedImageView ivAvatar;
            LinearLayout content;
            Button btnDel;

            public FirstHolder(View itemView) {
                super(itemView);
                ivAvatar = $(R.id.ivAvatar);
                content = $(R.id.content);
                btnDel = $(R.id.btnDel);

                ivAvatar.setImageResource(R.mipmap.allpeople);
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), MyGroupActivity.class));
                    }
                });
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.getInstance().show("该大厅不能删除！");
                    }
                });
            }
        }
    }

    private void getDeleteFriend(String userId) {
        HttpManager.getInstance().get(WebAPI.MailApi.MAILAPI_DELETEFRIEND
                        + "?userId=" + userId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("删除成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_RIDERS));
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.ADD_FRIENDS)
            getData();
    }
}