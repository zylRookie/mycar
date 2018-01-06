package com.example.zyl.dqcar.moudels.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseFragment;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.riders.EditRidersActivity;
import com.example.zyl.dqcar.moudels.adapter.RidersAdapter;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.RidersBean;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.KeyBoardUtil;
import com.example.zyl.dqcar.utils.SoftKeyBoardListener;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 车友圈
 */
public class RidersFragment extends BaseFragment implements View.OnClickListener {


    RecyclerView recyclerView;
    TwinklingRefreshLayout refreshLayout;
    RidersAdapter ridersAdapter;
    EditText myEditText;
    RelativeLayout rlSendComment;
    Button btnSendComment;
    List<RidersBean.CircleForListModelListBean> circleForListModelList = new ArrayList<>();
    int page = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_riders;
    }

    public static RidersFragment newInstance() {
        Bundle args = new Bundle();
        RidersFragment fragment = new RidersFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void onVisibleChange(boolean isVisible) {
//        super.onVisibleChange(isVisible);
//        if (mView != null && circleForListModelList != null) {
//            if (circleForListModelList.size() <= 0) {
//                initData(1);
//            }
//        }
//    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        init();
    }

    private void init() {
        getLinearLayoutManager();
        ridersAdapter = new RidersAdapter(getActivity(), circleForListModelList);
        recyclerView.setAdapter(ridersAdapter);
        initRefreshLayout();
        initData(1);
    }

    private void initData(final int index) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_GETALLCANVISUALCIRCLE
                + "?pageId=" + index, new HttpManager.ResponseCallback<RidersBean>() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(RidersBean o) {
                if (o.errorCode.equals("0000")) {
                    if (o.circleForListModelList != null && o.circleForListModelList.size() > 0) {
                        ridersAdapter.addData(o.circleForListModelList);
                    } else {
                        if (index == 1) {
                            ToastUtil.getInstance().show("暂无数据");
                        } else {
                            ToastUtil.getInstance().show("已经加载完所有数据");
                        }
                    }
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                } else {
                    ToastUtil.getInstance().show(o.errorMsg);
                }

            }
        });
    }

    int myP;
    String toUserId = "";

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                circleForListModelList.clear();
                page = 1;
                initData(page);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                page++;
                initData(page);
            }
        });
        ridersAdapter.setOnclick(new RidersAdapter.OnCallbackListener() {
            @Override
            public void getChecked(View v, int p) {
                Log.e("AAA", "getChecked: =====>pos=" + p);
                likeCircle(p);
            }

            @Override
            public void getCheck(View v, int p) {
                Log.e("AAA", "getChecked: =====>pos=" + p);
                unLikeCircle(p);
            }

            @Override
            public void getDeleteAllOne(View v, int p) {
                deleteCircle(p);
            }

            @Override
            public void getComment(View v, int p) {
//                ToastUtil.getInstance().show("点击了！！！");
                rlSendComment.setVisibility(View.VISIBLE);
                rlSendComment.requestFocus();
                KeyBoardUtil.openKeyboard(getActivity(), myEditText);
                myP = p;
                toUserId = "";
            }

            @Override
            public void getDelete(View v, final int p, final int pos) {//删除
                new AlertView(null, null, "取消", new String[]{"删除"}, null, getActivity(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0)
                            deleteComment(p, pos);
                    }
                }).show();
            }

            @Override
            public void getBackSend(View v, int p, int pos) {//回复
                rlSendComment.setVisibility(View.VISIBLE);
                rlSendComment.requestFocus();
                KeyBoardUtil.openKeyboard(getActivity(), myEditText);
                myP = pos;
                toUserId = circleForListModelList.get(pos - 1).commentList.get(p).user.id;
            }


        });
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.e("AAA", "keyBoardShow: " + "键盘显示 高度" + height);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.e("AAA", "keyBoardShow: " + "键盘隐藏 高度" + height);
                rlSendComment.setVisibility(View.GONE);
            }
        });
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = myEditText.getText().toString();
                sendComment(content);
                KeyBoardUtil.hideSoftKeyboard(getActivity());
                myEditText.setText("");
            }
        });
    }

    //删除车友圈
    private void deleteCircle(final int p) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_DELETECIRCLE
                        + "?deleteCircleId=" + circleForListModelList.get(p - 1).circleId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("删除成功");
                            circleForListModelList.remove(p - 1);
                            ridersAdapter.notifyItemChanged(p);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    /*
    删除评论
     */
    private void deleteComment(final int p, final int pos) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_DELETECOMMENT
                        + "?deleteCommentId=" + circleForListModelList.get(pos - 1).commentList.get(p).commentId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("删除成功");
                            initDataNotifyItem(pos, "deleteComment");
//                            initDataType(1);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    /*
    评论
     */
    private void sendComment(final String content) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_ADDCOMMENT
                        + "?circleId=" + circleForListModelList.get(myP - 1).circleId
                        + "&content=" + content + "&commentToUserId=" + toUserId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("评论成功");
                            initDataNotifyItem(0, "comment");
//                            initDataType(1);
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }

    /*
    取消点赞
     */
    private void unLikeCircle(final int p) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_UNLIKECIRCLE
                        + "?unLikeCircleId=" + circleForListModelList.get(p - 1).circleId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("取消成功");
                            initDataNotifyItem(p, "unLiked");
//                            ridersAdapter.notifyItemChanged(p, false);
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }

    /*
    点赞请求
     */
    private void likeCircle(final int p) {
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_LIKECIRCLE
                        + "?likeCircleId=" + circleForListModelList.get(p - 1).circleId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("点赞成功");
                            initDataNotifyItem(p, "liked");
//                            ridersAdapter.notifyItemChanged(p, true);
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }


    private void initView() {
        ((ImageView) $(R.id.ivRight)).setImageResource(R.mipmap.writea_about);
        ((TextView) $(R.id.tvTitle)).setText("车友圈");
        $(R.id.ivBaseBack).setVisibility(View.GONE);
        recyclerView = $(R.id.recyclerView_riders);
        rlSendComment = $(R.id.rlSendComment);
        refreshLayout = $(R.id.refreshLayout);
        myEditText = $(R.id.myEditText);
        btnSendComment = $(R.id.btnSendComment);
        $(R.id.rlRight).setOnClickListener(this);
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRight:
                startActivity(new Intent(getActivity(), EditRidersActivity.class));
                break;
        }
    }

    private void initDataNotifyItem(final int pos, final String type) {
        int position = type.equals("comment") ? myP : pos;
        HttpManager.getInstance().get(WebAPI.FriendsApi.FRIENDSAPI_GETCIRCLEINFO
                + "?circleId=" + circleForListModelList.get(position - 1).circleId, new HttpManager.ResponseCallback<RidersBean.CircleForListModelListBean>() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(RidersBean.CircleForListModelListBean o) {
                switch (type) {
                    case "liked"://点赞
                        circleForListModelList.get(pos - 1).hasLiked = o.hasLiked;
                        circleForListModelList.get(pos - 1).likedUserList = o.likedUserList;
                        ridersAdapter.notifyItemChanged(pos, circleForListModelList);
                        break;
                    case "unLiked"://取消点赞
                        circleForListModelList.get(pos - 1).hasLiked = o.hasLiked;
                        circleForListModelList.get(pos - 1).likedUserList = o.likedUserList;
                        ridersAdapter.notifyItemChanged(pos, circleForListModelList);
                        break;
                    case "comment"://评论，回复
                        circleForListModelList.get(myP - 1).commentList = o.commentList;
                        ridersAdapter.notifyItemChanged(myP, circleForListModelList);
                        break;
                    case "deleteComment"://删除评论
                        circleForListModelList.get(pos - 1).commentList = o.commentList;
                        ridersAdapter.notifyItemChanged(pos, circleForListModelList);
                        break;
                }

            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusConst.ADD_RIDER) {
            Log.e("AAA", "onEventComing: ------->刷新！！");
            circleForListModelList.clear();
            page = 1;
            initData(page);
        } else if (eventCenter.getEventCode() == EventBusConst.UPDATA_RIDERS) {
            circleForListModelList.clear();
            page = 1;
            initData(page);
        } else if (eventCenter.getEventCode() == EventBusConst.UPDATE_USER) {
            circleForListModelList.clear();
            page = 1;
            initData(page);
        }

    }

}