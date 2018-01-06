package com.example.zyl.dqcar.moudels.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseLoadMoreAdapter;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.mine.CarDetailActivity;
import com.example.zyl.dqcar.moudels.bean.ReleaseListBean;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.example.zyl.dqcar.R.id.tvE;

/**
 * Author: Zhaoyl
 * Date: 2017/6/30 15:56
 * Description: 查找二手车
 * PackageName: FindOldCarActivity
 * Copyright: 端趣网络
 **/

public class FindOldCarActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    CustomPopWindow mCustomPopWindow;
    CustomPopWindow popWindow;
    CustomPopWindow mPopWindow;
    PtrFrameLayout ptrFrameLayout;
    MyAdapter myAdapter;
    EditText etnSearchKey;
    TextView tvNull, tvLast, tvFirst, tvEnd, tvAddress;
    View viewD;
    int cur = 1;
    int orderId = 4;
    int sPrice = 0;
    int ePrice = 0;
    int sYear = 0;
    int eYear = 0;
    List<ReleaseListBean.PtPtoductModelLstBean> list = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_findriders;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvBaseLeft)).setText("查找二手车");
        recyclerView = $(R.id.recyclerView_findriders);
        ptrFrameLayout = $(R.id.ptrFrameLayout);
        etnSearchKey = $(R.id.etnSearchKey);
        tvNull = $(R.id.tvNull);
        tvFirst = $(R.id.tvFirst);
        tvLast = $(R.id.tvLast);
        tvEnd = $(R.id.tvEnd);
        viewD = $(R.id.viewD);
        $(R.id.llSort).setOnClickListener(this);
        $(R.id.llPrice).setOnClickListener(this);
        $(R.id.llCarAge).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.llSearchKey).setOnClickListener(this);

        getLinearLayoutManager();
        myAdapter = new MyAdapter(FindOldCarActivity.this, list);
        recyclerView.setAdapter(myAdapter);
        upData();
    }

    private void initRefresh(final int orderId, final int sPrice, final int ePrice, final int sYear, final int eYear) {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(FindOldCarActivity.this);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                list.clear();
                initData(cur = 1, etnSearchKey.getText().toString(), orderId, sPrice, ePrice, sYear, eYear);
            }
        });

        myAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                list.clear();
                initData(cur + 1, etnSearchKey.getText().toString(), orderId, sPrice, ePrice, sYear, eYear);
            }
        });
    }


    private void initData(final int index, String key, int orderId, int sPrice, int ePrice, int sYear, int eYear) {
        HttpManager.getInstance().get(WebAPI.MsgApi.MINE_FINDBYCAR
                        + "?pageId=" + index
                        + "&searchKey=" + key
                        + "&orderId=" + orderId
                        + "&sPrice=" + sPrice
                        + "&ePrice=" + ePrice
                        + "&sYear=" + sYear
                        + "&eYear=" + eYear
                , new HttpManager.ResponseCallback<ReleaseListBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseListBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (index == 1) {
                                if (o.ptPtoductModelLst != null && o.ptPtoductModelLst.size() > 0) {
                                    myAdapter.setData(o.ptPtoductModelLst);
                                    tvNull.setVisibility(View.GONE);
                                    ptrFrameLayout.setVisibility(View.VISIBLE);
                                    ptrFrameLayout.refreshComplete();
                                } else {
                                    tvNull.setVisibility(View.VISIBLE);
                                    ptrFrameLayout.setVisibility(View.GONE);
                                }
                            } else {
                                Log.e("AAA", "onSuccess: " + o.ptPtoductModelLst.size());
                                if (o.ptPtoductModelLst == null || o.ptPtoductModelLst.size() == 0) {
                                    ToastUtil.getInstance().show("已加载所有数据!");
                                    myAdapter.notifyLoadMoreCompleted();
                                } else {
                                    myAdapter.addData(o.ptPtoductModelLst);
                                    cur++;
                                }

                            }
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindOldCarActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.llSort://排序
                showPopMenu();
                break;
            case R.id.llPrice://价格
                showPopMenuP();
                break;
            case R.id.llCarAge://车龄
                showPopMenuA();
                break;
            case R.id.llSearchKey://搜索
                upData();
                break;

        }
    }

    private void showPopMenuA() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_a, null);
        //处理popWindow 显示内容
        handleLogicA(contentView);
        //创建并显示popWindow
        mPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create()
                .showAsDropDown(viewD, 0, 0);
    }

    private void handleLogicA(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopWindow != null) {
                    mPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case tvE:
                        tvEnd.setText("0-3年");
                        sYear = 0;
                        eYear = 3;
                        upData();
                        break;
                    case R.id.tvF:
                        tvEnd.setText("3-5年");
                        sYear = 3;
                        eYear = 5;
                        upData();
                        break;
                    case R.id.tvG:
                        tvEnd.setText("5-10年");
                        sYear = 5;
                        eYear = 10;
                        upData();
                        break;

                }
            }
        };
        contentView.findViewById(tvE).setOnClickListener(listener);
        contentView.findViewById(R.id.tvF).setOnClickListener(listener);
        contentView.findViewById(R.id.tvG).setOnClickListener(listener);
    }

    private void showPopMenuP() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_p_findcar, null);
        //处理popWindow 显示内容
        handleLogicP(contentView);
        //创建并显示popWindow
        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create()
                .showAsDropDown(viewD, 0, 0);
    }

    private void handleLogicP(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.dissmiss();
                }
                switch (v.getId()) {
                    //点击确认
                    case R.id.tvA:
                        tvLast.setText("0-5万");
                        sPrice = 0;
                        ePrice = 50000;
                        upData();
                        break;
                    case R.id.tvB:
                        tvLast.setText("5-8万");
                        sPrice = 50000;
                        ePrice = 80000;
                        upData();
                        break;
                    case R.id.tvC:
                        tvLast.setText("8-15万");
                        sPrice = 80000;
                        ePrice = 150000;
                        upData();
                        break;
                    case R.id.tvD:
                        tvLast.setText("15++");
                        sPrice = 0;
                        ePrice = 0;
                        upData();
                        break;

                }
            }
        };
        contentView.findViewById(R.id.tvA).setOnClickListener(listener);
        contentView.findViewById(R.id.tvB).setOnClickListener(listener);
        contentView.findViewById(R.id.tvC).setOnClickListener(listener);
        contentView.findViewById(R.id.tvD).setOnClickListener(listener);
    }

    private void showPopMenu() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_findcar, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
                    case R.id.tvPriceOn:
                        tvFirst.setText("价格升");
                        orderId = 1;
                        upData();
                        break;
                    case R.id.tvPriceUp:
                        tvFirst.setText("价格降");
                        orderId = 2;
                        upData();
                        break;
                    case R.id.tvTimeOn:
                        tvFirst.setText("时间升");
                        orderId = 3;
                        upData();
                        break;
                    case R.id.tvTimeUp:
                        tvFirst.setText("时间降");
                        orderId = 4;
                        upData();
                        break;
                    case R.id.tvCarYearon:
                        tvFirst.setText("车龄升");
                        orderId = 5;
                        upData();
                        break;
                    case R.id.tvCarYearUp:
                        tvFirst.setText("车龄降");
                        orderId = 6;
                        upData();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tvPriceOn).setOnClickListener(listener);
        contentView.findViewById(R.id.tvPriceUp).setOnClickListener(listener);
        contentView.findViewById(R.id.tvTimeOn).setOnClickListener(listener);
        contentView.findViewById(R.id.tvTimeUp).setOnClickListener(listener);
        contentView.findViewById(R.id.tvCarYearon).setOnClickListener(listener);
        contentView.findViewById(R.id.tvCarYearUp).setOnClickListener(listener);
    }

    private void upData() {
        initRefresh(orderId, sPrice, ePrice, sYear, eYear);
        initData(cur = 1, etnSearchKey.getText().toString(), orderId, sPrice, ePrice, sYear, eYear);
    }

    public class MyAdapter extends BaseLoadMoreAdapter<VH> {

        Context mContext;
        LayoutInflater layoutInflater;
        List<ReleaseListBean.PtPtoductModelLstBean> mDatas;

        public MyAdapter(Context context, List<ReleaseListBean.PtPtoductModelLstBean> list) {
            this.mContext = context;
            this.mDatas = list;
            layoutInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<ReleaseListBean.PtPtoductModelLstBean> list) {
            mDatas = list;
            notifyDataSetChanged();
        }

        public void addData(List<ReleaseListBean.PtPtoductModelLstBean> list) {
            int count = mDatas.size();
            mDatas.addAll(list);
            notifyItemRangeInserted(count + 1, list.size());
        }

        @Override
        protected int getItemViewTypeImpl(int position) {
            return 0;
        }

        @Override
        protected int getItemCountImpl() {
            return mDatas.size() > 0 ? mDatas.size() : 0;
        }

        //暂时先分开，数据源来了之后删掉一个
        @Override
        protected ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
            View mView = layoutInflater.inflate(R.layout.layout_item_release_findcar, parent, false);
            return new ViewHolder(mView);
        }

        @Override
        protected void onBindViewHolderImpl(final VH holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (mDatas.get(position).imgLst != null && mDatas.get(position).imgLst.size() > 0)
                Picasso.with(mContext).load(mDatas.get(position).imgLst.get(0)).into(viewHolder.ivCar);
            viewHolder.tvOldGoodsTitle.setText(mDatas.get(position).title);
            viewHolder.tvTime.setText(mDatas.get(position).createdTime);
            viewHolder.tvMoney.setText("￥" + mDatas.get(position).priceStr);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CarDetailActivity.class);
                    intent.putExtra("type", "oldCarOnLine");
                    intent.putExtra("productId", mDatas.get(position).id);
                    mContext.startActivity(intent);

                }
            });
        }

        public class ViewHolder extends VH {

            ImageView ivCar;
            TextView tvOldGoodsTitle;
            TextView tvTime;
            TextView tvMoney;

            public ViewHolder(View itemView) {
                super(itemView);
                ivCar = $(R.id.ivCar);
                tvOldGoodsTitle = $(R.id.tvOldGoodsTitle);
                tvTime = $(R.id.tvTime);
                tvMoney = $(R.id.tvMoney);
            }
        }
    }
}
