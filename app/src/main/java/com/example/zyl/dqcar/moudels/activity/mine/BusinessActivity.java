package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.AliPayBean;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.pay.PayResult;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/10 10:56
 * Description: 业务（充值，赠送，续费）
 * PackageName: Activity
 * Copyright: 端趣网络
 **/

public class BusinessActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout llRecharge, llGive, llRenew;
    TextView tvPay, tvMonths, tvNumber, tvGiveUser, tvUserMoney;
    private static final int RQF_PAY = 1;
    RelativeLayout rlMonths, rlChoosePay;
    CustomPopWindow mCustomPopWindow;
    EditText editTextMoney;
    ImageView ivUserHead;
    int payType = 1;
    String url;
    View viewD;

    @Override
    public int getLayout() {
        return R.layout.activity_business;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        String type = getIntent().getStringExtra("type");
        viewD = $(R.id.viewD);
        tvPay = $(R.id.tvPay);
        tvMonths = $(R.id.tvMonths);
        tvNumber = $(R.id.tvNumber);
        llRecharge = $(R.id.llRecharge);
        llGive = $(R.id.llGive);
        tvGiveUser = $(R.id.tvGiveUser);
        llRenew = $(R.id.llRenew);
        rlMonths = $(R.id.rlMonths);
        rlChoosePay = $(R.id.rlChoosePay);
        tvUserMoney = $(R.id.tvUserMoney);
        ivUserHead = $(R.id.ivUserHead);
        editTextMoney = $(R.id.editTextMoney);

        if (type.equals("recharge")) {//充值
            ((TextView) $(R.id.tvBaseLeft)).setText("充值");
            llRecharge.setVisibility(View.VISIBLE);
            llGive.setVisibility(View.GONE);
            llRenew.setVisibility(View.GONE);
        } else if (type.equals("give")) {//赠给
            ((TextView) $(R.id.tvBaseLeft)).setText("赠送");
            llRecharge.setVisibility(View.GONE);
            llGive.setVisibility(View.VISIBLE);
            llRenew.setVisibility(View.GONE);
        } else if (type.equals("renew")) {//续费
            ((TextView) $(R.id.tvBaseLeft)).setText("续费");
            llRecharge.setVisibility(View.GONE);
            llGive.setVisibility(View.GONE);
            llRenew.setVisibility(View.VISIBLE);
        }

        rlMonths.setOnClickListener(this);
        rlChoosePay.setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.tvGiveUser).setOnClickListener(this);
        $(R.id.btnPay).setOnClickListener(this);

        getData();
    }

    private void getData() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_USERDETAIL
                        + "?useId=" + BaseSharedPreferences.getId(BusinessActivity.this)
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            tvUserMoney.setText("驹币  " + o.smUserModel.money + "");
                            if (!CheckUtil.isNull(o.smUserModel.imgKey))
                                Picasso.with(BusinessActivity.this).load(o.smUserModel.imgKey).transform(new CircleTransform()).into(ivUserHead);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.rlChoosePay:
                rlChoosePay.setBackgroundColor(getResources().getColor(R.color.Ae9f8ff));
                showPopMenu();
                break;
            case R.id.rlMonths:
                showPopMenuM();
                break;
            case R.id.tvGiveUser:
                startActivity(new Intent(BusinessActivity.this, mailActivity.class));
                break;
            case R.id.btnPay:
                if (CheckUtil.isNull(editTextMoney.getText().toString())) {
                    ToastUtil.getInstance().show("金额不能为空");
                    return;
                }
                if (payType == 1) {//支付宝
                    url = "https://pay.willowspace.cn/alipay/unifiedOrder";
                } else if (payType == 2) {//微信
                    ToastUtil.getInstance().show("维护中请耐心等待。。。");
                    return;
                }
                gotoPay();
                break;
        }
    }

    private void gotoPay() {
        HttpManager.getInstance().get(url
                        + "?totalAmount=" + editTextMoney.getText().toString()
                        + "&subject=" + "驹币充值"
                        + "&body=" + "驹币充值"
                , new HttpManager.ResponseCallback<AliPayBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(AliPayBean o) {
                        startPay(o.data.orderString);
                    }
                });
    }

    private void showPopMenuM() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_months, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .create()
                .showAsDropDown(rlMonths, 0, 0);
    }

    private void handleListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MyAdapter adapter = new MyAdapter();
        adapter.setData(mockData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private List<Integer> mockData() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            data.add(i + 1);
        }

        return data;
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Integer> mData;

        public void setData(List<Integer> data) {
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(mData.get(position) + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvMonths.setText(mData.get(position) + "");
                    mCustomPopWindow.onDismiss();
                    if (position + 1 < 3) {
                        tvNumber.setText(mData.get(position) * 30 + "");
                    } else if (position + 1 >= 3 && position + 1 < 6) {
                        tvNumber.setText(mData.get(position) * 30 * 0.95 + "");
                    } else if (position + 1 >= 6 && position + 1 < 12) {
                        tvNumber.setText(mData.get(position) * 30 * 0.85 + "");
                    } else if (position + 1 >= 12) {
                        tvNumber.setText(mData.get(position) * 30 * 0.65 + "");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends VH {

            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = $(R.id.text_content);
            }
        }
    }

    private void showPopMenu() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_pay, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rlChoosePay.setBackgroundColor(getResources().getColor(R.color.AF6F6F6));
                    }
                })
                .create()
                .showAsDropDown(viewD, 0, 0);
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.tvOn:
                        tvPay.setText("微信");
                        payType = 2;
                        break;
                    case R.id.tvUp:
                        tvPay.setText("支付宝");
                        payType = 1;
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tvOn).setOnClickListener(listener);
        contentView.findViewById(R.id.tvUp).setOnClickListener(listener);
    }

    public void startPay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(BusinessActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PayResult payResult = new PayResult((String) msg.obj);
            switch (msg.what) {
                case RQF_PAY:
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(BusinessActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }
                    // 支付结果resultStatus={4000};memo={系统繁忙，请稍后再试};result={}
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(BusinessActivity.this, resultStatus, Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        Toast.makeText(BusinessActivity.this, resultStatus, Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(BusinessActivity.this, "您已取消支付", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        Toast.makeText(BusinessActivity.this, resultStatus, Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusConst.BACK_USERNAME) {
            String name = (String) eventCenter.getData();
            tvGiveUser.setText(name);
        }
    }

}
