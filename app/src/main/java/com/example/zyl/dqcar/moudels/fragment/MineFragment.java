package com.example.zyl.dqcar.moudels.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseFragment;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mine.AccountCenterActivity;
import com.example.zyl.dqcar.moudels.activity.mine.EditUserActivity;
import com.example.zyl.dqcar.moudels.activity.mine.GiftMallActivity;
import com.example.zyl.dqcar.moudels.activity.mine.MyCollectionActivity;
import com.example.zyl.dqcar.moudels.activity.mine.MyPhoneActivity;
import com.example.zyl.dqcar.moudels.activity.mine.MyReleaseActivity;
import com.example.zyl.dqcar.moudels.activity.mine.NewsContentActivity;
import com.example.zyl.dqcar.moudels.activity.mine.SetUpActivity;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.EventCenter;
import com.squareup.picasso.Picasso;


/**
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    TextView tvUserName;
    TextView tvPhoneNumber;
    TextView tvRoleType;
    ImageView myHeadO;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onVisibleChange(boolean isVisible) {
        super.onVisibleChange(isVisible);
        if (mView != null) {
            initData();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_USERDETAIL
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {
                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (!CheckUtil.isNull(o.smUserModel.name))
                                tvUserName.setText(o.smUserModel.name);
                            if (!CheckUtil.isNull(o.smUserModel.phone))
                                tvPhoneNumber.setText("账号：" + o.smUserModel.phone);
                            if (!CheckUtil.isNull(o.smUserModel.imgKey))
                                Picasso.with(getActivity()).load(o.smUserModel.imgKey).transform(new CircleTransform()).into(myHeadO);
                            if (!CheckUtil.isNull(o.smUserModel.roleStr))
                                tvRoleType.setText("会员级别：" + o.smUserModel.roleStr);
                            BaseSharedPreferences.setUserName(getActivity(), o.smUserModel.name);
                            BaseSharedPreferences.setUserHeadUrl(getActivity(), o.smUserModel.imgKey);
                        }
                    }
                });
    }

    private void initView() {
        tvUserName = $(R.id.tvUserName);
        tvPhoneNumber = $(R.id.tvPhoneNumber);
        myHeadO = $(R.id.myHeadO);
        tvRoleType = $(R.id.tvRoleType);
        $(R.id.llAccountCenter).setOnClickListener(this);
        $(R.id.llMyCollection).setOnClickListener(this);
        $(R.id.llMyRelease).setOnClickListener(this);
        $(R.id.llMyPhone).setOnClickListener(this);
        $(R.id.llGiftMall).setOnClickListener(this);
        $(R.id.llNewsContent).setOnClickListener(this);
        $(R.id.llSetUp).setOnClickListener(this);
        $(R.id.editUserMsg).setOnClickListener(this);
        $(R.id.myHeadO).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAccountCenter://账户中心
                startActivity(new Intent(getActivity(), AccountCenterActivity.class));
                break;
            case R.id.llMyRelease://我的发布
                startActivity(new Intent(getActivity(), MyReleaseActivity.class));
                break;
            case R.id.llMyPhone://我的相册
                startActivity(new Intent(getActivity(), MyPhoneActivity.class));
                break;
            case R.id.llMyCollection://我的收藏
                startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                break;
            case R.id.llGiftMall://礼品商店
                Intent intent = new Intent(getActivity(), GiftMallActivity.class);
                intent.putExtra("type", "mineGift");
                startActivity(intent);
//                startActivity(new Intent(getActivity(), GiftMallActivity.class));
                break;
            case R.id.llNewsContent://新闻中心
                startActivity(new Intent(getActivity(), NewsContentActivity.class));
                break;
            case R.id.llSetUp://设置
                startActivity(new Intent(getActivity(), SetUpActivity.class));
                break;
            case R.id.editUserMsg://修改个人信息
                startActivity(new Intent(getActivity(), EditUserActivity.class));
                break;
            case R.id.myHeadO://修改个人信息
                startActivity(new Intent(getActivity(), EditUserActivity.class));
                break;

        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusConst.LOGIN_SUCCESS)
            initData();
        if (eventCenter.getEventCode() == EventBusConst.UPDATE_USER) {
            initData();
        }

    }
}