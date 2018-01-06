package com.example.zyl.dqcar.moudels.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseFragment;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
import com.example.zyl.dqcar.moudels.activity.mail.FindFriendActivity;
import com.example.zyl.dqcar.moudels.activity.mine.GiftMallActivity;
import com.example.zyl.dqcar.moudels.activity.msg.FindOldCarActivity;
import com.example.zyl.dqcar.moudels.activity.msg.OnCreateGroupActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.MsgAllBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息
 */
public class MyMsgFragment extends BaseFragment implements View.OnClickListener {

    RecyclerView recyclerView;
    CustomPopWindow popWindow;
    ImageView ivRight;
    RelativeLayout rlTitleLayout;
    MyAdapter myAdapter;
    List<MsgAllBean.Items1Bean> items1 = new ArrayList<>();
    List<MsgAllBean.ItemsBean> items = new ArrayList<>();


    @Override
    protected int getLayout() {
        return R.layout.fragment_msg;
    }

    public static MyMsgFragment newInstance() {
        Bundle args = new Bundle();
        MyMsgFragment fragment = new MyMsgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        ((TextView) $(R.id.tvTitle)).setText("车汇宝");
        ivRight = $(R.id.ivRight);
        rlTitleLayout = $(R.id.rlTitleLayout);
        ivRight.setImageResource(R.mipmap.add_friends);
        recyclerView = $(R.id.recyclerView_msg);
        $(R.id.ivBaseBack).setVisibility(View.GONE);
        $(R.id.rlRight).setOnClickListener(this);


        getLinearLayoutManager();
        myAdapter = new MyAdapter();
        getData();
    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.MsgApi.MINE_FINDALLMYSESSION
                , new HttpManager.ResponseCallback<MsgAllBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(MsgAllBean o) {
                        if (o.errorCode.equals("0000")) {
                            myAdapter.setData(o.items1, o.items);
                            recyclerView.setAdapter(myAdapter);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRight:
                useInAndOutAnim();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void useInAndOutAnim() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.choose_popwindow, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.AnimTop)
                .create()
                .showAsDropDown(rlTitleLayout, 0, 0, Gravity.RIGHT);
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.llFindFriends:
                        startActivity(new Intent(getActivity(), FindFriendActivity.class));
                        break;
                    case R.id.llFindOldCar:
                        getActivity().startActivity(new Intent(getActivity(), FindOldCarActivity.class));
                        break;
                    case R.id.llFindOldGoods:
                        Intent intent = new Intent(getActivity(), GiftMallActivity.class);
                        intent.putExtra("type", "goods");
                        getActivity().startActivity(intent);
                        break;
                    case R.id.llOnCreateGroup:
                        if (!CheckUtil.isNull(BaseSharedPreferences.getRoleType(getActivity()))) {
                            if (Integer.parseInt(BaseSharedPreferences.getRoleType(getActivity())) >= 5)
                                getActivity().startActivity(new Intent(getActivity(), OnCreateGroupActivity.class));
                            else
                                ToastUtil.getInstance().show("您不能创建群");
                        }
                        break;
                }
            }
        };
        contentView.findViewById(R.id.llFindFriends).setOnClickListener(listener);
        contentView.findViewById(R.id.llFindOldCar).setOnClickListener(listener);
        contentView.findViewById(R.id.llFindOldGoods).setOnClickListener(listener);
        contentView.findViewById(R.id.llOnCreateGroup).setOnClickListener(listener);
    }


    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {

        List<MsgAllBean.Items1Bean> groupItems;
        List<MsgAllBean.ItemsBean> oneItems;

        public void setData(List<MsgAllBean.Items1Bean> items1, List<MsgAllBean.ItemsBean> items) {
            this.groupItems = items1;
            this.oneItems = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == groupItems.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.try_demohead, parent, false);
                    break;
                case 1:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_msg_content, parent, false);
                    break;
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (position == 0) {
                viewHolder.ivGroupImage.setImageResource(R.mipmap.newgroup);
                viewHolder.tvGroupName.setText("群聊");
            } else if (position == groupItems.size() + 1) {
                viewHolder.ivGroupImage.setImageResource(R.mipmap.oneman);
                viewHolder.tvGroupName.setText("车友");
            } else if (position > 0 && position < groupItems.size() + 1) {
                final MsgAllBean.Items1Bean myDatas = groupItems.get(position - 1);
                viewHolder.tvMessageNum.setVisibility(myDatas.unReadCount == 0 ? View.GONE : View.VISIBLE);
                if (myDatas.unReadCount > 99)
                    viewHolder.tvMessageNum.setText("99+");
                else
                    viewHolder.tvMessageNum.setText(myDatas.unReadCount + "");
                if (!CheckUtil.isNull(myDatas.title))
                    viewHolder.tvMsgTitle.setText(myDatas.title);
                if (!CheckUtil.isNull(myDatas.content))
                    viewHolder.tvMsgContent.setText(myDatas.content);
                if (!CheckUtil.isNull(myDatas.updatedTime))
                    viewHolder.tvMsgTime.setText(myDatas.updatedTime);
                if (!CheckUtil.isNull(myDatas.logoUrl))
                    Picasso.with(getActivity()).load(myDatas.logoUrl).into(viewHolder.ivMsgHead);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getActivity(), DqChatActivity.class);
                        in.putExtra("userName", myDatas.title);
                        in.putExtra("type", "groupChat");
                        in.putExtra("sourceUserId", myDatas.sourceId);
                        startActivity(in);
                    }
                });
//                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        new AlertView("删除会话", "你确定删除该条会话吗？", null, new String[]{"取消", "确定"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Object o, int position) {
//                                if (position == 1) {
//                                    deleteMsg(myDatas.id);
//                                }
//                            }
//                        }).show();
//                        return true;
//                    }
//                });
            } else if (position > groupItems.size() + 1) {
                final MsgAllBean.ItemsBean myOneDatas = oneItems.get(position - (groupItems.size() + 2));
                Log.e("AAA", "onBindViewHolder: ---->ppp=" + groupItems.size());
                viewHolder.tvMessageNum.setVisibility(myOneDatas.unReadCount == 0 ? View.GONE : View.VISIBLE);
                viewHolder.tvMessageNum.setText(myOneDatas.unReadCount + "");
                if (!CheckUtil.isNull(myOneDatas.title))
                    viewHolder.tvMsgTitle.setText(myOneDatas.title);
                if (!CheckUtil.isNull(myOneDatas.content))
                    viewHolder.tvMsgContent.setText(myOneDatas.content);
                if (!CheckUtil.isNull(myOneDatas.updatedTime))
                    viewHolder.tvMsgTime.setText(myOneDatas.updatedTime);
                if (!CheckUtil.isNull(myOneDatas.logoUrl))
                    Picasso.with(getActivity()).load(myOneDatas.logoUrl).into(viewHolder.ivMsgHead);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getActivity(), DqChatActivity.class);
                        in.putExtra("userName", myOneDatas.title);
                        in.putExtra("type", "oneChat");
                        in.putExtra("sourceUserId", myOneDatas.sourceUserId);
                        startActivity(in);
                    }
                });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertView("删除会话", "你确定删除该条会话吗？", null, new String[]{"取消", "确定"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 1) {
                                    deleteMsg(myOneDatas.id);
                                }
                            }
                        }).show();
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return groupItems.size() + oneItems.size() + 2;
        }

        public class ViewHolder extends VH {

            ImageView ivGroupImage;
            TextView tvGroupName;
            RoundedImageView ivMsgHead;
            TextView tvMsgTitle, tvMsgContent, tvMsgTime, tvMessageNum;

            public ViewHolder(View itemView) {
                super(itemView);
                ivGroupImage = $(R.id.ivGroupImage);
                tvGroupName = $(R.id.tvGroupName);
                ivMsgHead = $(R.id.ivMsgHead);
                tvMsgTitle = $(R.id.tvMsgTitle);
                tvMsgContent = $(R.id.tvMsgContent);
                tvMsgTime = $(R.id.tvMsgTime);
                tvMessageNum = $(R.id.tvMessageNum);
            }
        }
    }

    private void deleteMsg(String delId) {
        HttpManager.getInstance().get(WebAPI.MsgApi.MINE_DELCHATSESSION
                        + "?delId=" + delId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            getData();
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
        if (eventCenter.getEventCode() == EventBusConst.UPDATA_MSG) {
            Log.e("AAA", "onEventComing: ---进来了哦-刷新-外面的界面-个人--__>");
//            items.clear();
            getData();
        } else if (eventCenter.getEventCode() == EventBusConst.UPDATA_UNREADMSG) {
            Log.e("AAA", "onEventComing: ---进来了哦-刷新-未读消息--__>");
//            items1.clear();
            getData();
        } else if (eventCenter.getEventCode() == EventBusConst.UPDATA_GROUPMSG) {
            Log.e("AAA", "onEventComing: ---进来了哦-刷新-外面的界面-群--__>");
//            items1.clear();
            getData();
        }
    }
}