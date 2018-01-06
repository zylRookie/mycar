package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
import com.example.zyl.dqcar.moudels.activity.msg.ImagePagerActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.ReleaseBean;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/6/30 11:16
 * Description: 车详情
 * PackageName: CarDetailActivity
 * Copyright: 端趣网络
 **/

public class CarDetailActivity extends BaseActivity implements View.OnClickListener {


    Banner banner;
    RelativeLayout rlGiftMallDetailButton;
    Button btnDetailName, btnDetailCollectionTwo, btnDetailCollection, btnDetailBuy;
    LinearLayout llGiftMallDetail, llOldCarDetail, llFindGoodsDetail, llOldCarDetailButton, llDetailTitle;
    TextView tvDetailTitle, tvDetailCarNorm, tvDetailCarYear, tvDetailCarDisplacement, tvDetailCarPrice, tvDetailGoodsName, tvDetailGoodsPrice, tvDetailGiftName, tvDetailGiftPrice, tvDetailDel;
    String type, productId, userName, sourceUserId;
    List<String> lists = new ArrayList<>();

    boolean isOk;

    @Override
    public int getLayout() {
        return R.layout.activity_cardetail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        llGiftMallDetail = $(R.id.llGiftMallDetail);
        llOldCarDetail = $(R.id.llOldCarDetail);
        llFindGoodsDetail = $(R.id.llFindGoodsDetail);
        llOldCarDetailButton = $(R.id.llOldCarDetailButton);
        rlGiftMallDetailButton = $(R.id.rlGiftMallDetailButton);
        llDetailTitle = $(R.id.llDetailTitle);
        tvDetailTitle = $(R.id.tvDetailTitle);
        tvDetailDel = $(R.id.tvDetailDel);
        tvDetailCarNorm = $(R.id.tvDetailCarNorm);
        tvDetailCarYear = $(R.id.tvDetailCarYear);
        tvDetailCarDisplacement = $(R.id.tvDetailCarDisplacement);
        tvDetailCarPrice = $(R.id.tvDetailCarPrice);
        tvDetailGoodsName = $(R.id.tvDetailGoodsName);
        tvDetailGoodsPrice = $(R.id.tvDetailGoodsPrice);
        tvDetailGiftName = $(R.id.tvDetailGiftName);
        tvDetailGiftPrice = $(R.id.tvDetailGiftPrice);
        btnDetailName = $(R.id.btnDetailName);
        btnDetailCollectionTwo = $(R.id.btnDetailCollectionTwo);
        btnDetailCollection = $(R.id.btnDetailCollection);
        btnDetailBuy = $(R.id.btnDetailBuy);

        type = getIntent().getStringExtra("type");
        productId = getIntent().getStringExtra("productId");
        if (type.equals("gift")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("礼品详情");
            llGiftMallDetail.setVisibility(View.VISIBLE);
            llOldCarDetailButton.setVisibility(View.VISIBLE);
            btnDetailBuy.setText("购买");
        } else if (type.equals("oldCar")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("车辆详情");
            llOldCarDetail.setVisibility(View.VISIBLE);
            rlGiftMallDetailButton.setVisibility(View.VISIBLE);
        } else if (type.equals("findGoods")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("闲置商品详情");
            llFindGoodsDetail.setVisibility(View.VISIBLE);
            rlGiftMallDetailButton.setVisibility(View.VISIBLE);
        } else if (type.equals("oldCarOnLine")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("车辆详情");
            llOldCarDetail.setVisibility(View.VISIBLE);
            llOldCarDetailButton.setVisibility(View.VISIBLE);
            btnDetailBuy.setText("私聊");
        } else if (type.equals("findGoodsOnLine")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("闲置商品详情");
            llFindGoodsDetail.setVisibility(View.VISIBLE);
            llOldCarDetailButton.setVisibility(View.VISIBLE);
            btnDetailBuy.setText("私聊");
        } else if (type.equals("oldCarMyCollection")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("车辆详情");
            llOldCarDetail.setVisibility(View.VISIBLE);
        } else if (type.equals("findGoodsMyCollection")) {
            ((TextView) $(R.id.tvBaseLeft)).setText("闲置商品详情");
            llFindGoodsDetail.setVisibility(View.VISIBLE);
        }
        banner = $(R.id.banner);
        $(R.id.ivBaseBack).setOnClickListener(this);
        btnDetailCollection.setOnClickListener(this);
        btnDetailCollectionTwo.setOnClickListener(this);
        btnDetailBuy.setOnClickListener(this);

        initData();
        initIsCollection();
    }

    private void initIsCollection() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_ISCOLLECT
                        + "?productId=" + productId
                        + "&userId=" + BaseSharedPreferences.getId(CarDetailActivity.this)
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            btnDetailCollection.setText(o.returnValue.equals("1") ? "取消收藏" : "收藏");
                            btnDetailCollectionTwo.setText(o.returnValue.equals("1") ? "取消收藏" : "收藏");
                            isOk = o.returnValue.equals("1") ? true : false;
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void initData() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_FINDBYPRODUCTID
                        + "?productId=" + productId
                , new HttpManager.ResponseCallback<ReleaseBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (o.ptPtoductModel.imgLst != null && o.ptPtoductModel.imgLst.size() > 0) {
                                banner.setImages(o.ptPtoductModel.imgLst)
                                        .setImageLoader(new MyImageLoader())
                                        .start();
                            }
                            if (type.equals("gift")) {
                                llDetailTitle.setVisibility(View.GONE);
                                btnDetailName.setText("商品描述");
                                tvDetailGiftName.setText("商品名称：" + o.ptPtoductModel.carNorm);
                                tvDetailGiftPrice.setText("销售价：" + o.ptPtoductModel.price + "" + "驹币");
                            } else if (type.equals("oldCar") || type.equals("oldCarOnLine") || type.equals("oldCarMyCollection")) {
                                llDetailTitle.setVisibility(View.VISIBLE);
                                btnDetailName.setText("车辆描述");
                                tvDetailTitle.setText(o.ptPtoductModel.title);
                                tvDetailCarNorm.setText("车    型：" + o.ptPtoductModel.carNorm);
                                tvDetailCarYear.setText("年    份：" + o.ptPtoductModel.carYear);
                                tvDetailCarDisplacement.setText("排    量：" + o.ptPtoductModel.carDisplacement);
                                tvDetailCarPrice.setText("销售价：" + o.ptPtoductModel.priceStr);
                            } else if (type.equals("findGoods") || type.equals("findGoodsOnLine") || type.equals("findGoodsMyCollection")) {
                                llDetailTitle.setVisibility(View.VISIBLE);
                                btnDetailName.setText("商品描述");
                                tvDetailTitle.setText(o.ptPtoductModel.title);
                                tvDetailGoodsName.setText("商品名称：" + o.ptPtoductModel.carNorm);
                                tvDetailGoodsPrice.setText("销售价：" + o.ptPtoductModel.priceStr);
                            }
                            if (type.equals("oldCarMyCollection") || type.equals("findGoodsMyCollection")) {
                                if (o.ptPtoductModel.userId.equals(BaseSharedPreferences.getId(CarDetailActivity.this))) {
                                    rlGiftMallDetailButton.setVisibility(View.VISIBLE);
                                } else {
                                    llOldCarDetailButton.setVisibility(View.VISIBLE);
                                    btnDetailBuy.setText("私聊");
                                }
                            }
                            tvDetailDel.setText(o.ptPtoductModel.detial);
                            lists = o.ptPtoductModel.imgLst;
                            userName = o.ptPtoductModel.userName;
                            sourceUserId = o.ptPtoductModel.userId;
                            initBanner();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void initBanner() {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent in = new Intent(CarDetailActivity.this, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) lists);
                in.putExtra("position", position + "");
                in.putExtras(bundle);
                startActivity(in);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.btnDetailCollection:
                if (isOk) {//按下==收藏（显示）请求借口==取消收藏
                    btnDetailCollection.setText("收藏");
                    deleteCollection();
                } else {// 显示  == 取消收藏  请求借口==收藏
                    btnDetailCollection.setText("取消收藏");
                    addCollection();
                }
                break;
            case R.id.btnDetailCollectionTwo:
                if (isOk) {//按下==收藏（显示）请求借口==取消收藏
                    btnDetailCollectionTwo.setText("收藏");
                    deleteCollection();
                } else {// 显示  == 取消收藏  请求借口==收藏
                    btnDetailCollectionTwo.setText("取消收藏");
                    addCollection();
                }
                break;
            case R.id.btnDetailBuy:
                if (type.equals("findGoodsOnLine") || type.equals("oldCarOnLine") || type.equals("oldCarMyCollection") || type.equals("findGoodsMyCollection")) {
                    Intent in = new Intent(CarDetailActivity.this, DqChatActivity.class);
                    in.putExtra("userName", userName);
                    in.putExtra("type", "oneChat");
                    in.putExtra("sourceUserId", sourceUserId);
                    startActivity(in);
                } else {
                    ToastUtil.getInstance().show("购买");
                }
                break;
        }
    }

    private void deleteCollection() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_DELETECOLLECT
                        + "?productId=" + productId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            isOk = false;
                            ToastUtil.getInstance().show("取消成功");
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void addCollection() {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_ADDCOLLECT
                        + "?productId=" + productId
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            isOk = true;
                            ToastUtil.getInstance().show("收藏成功");
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Picasso.with(CarDetailActivity.this).load((String) path).into(imageView);
        }
    }


}
