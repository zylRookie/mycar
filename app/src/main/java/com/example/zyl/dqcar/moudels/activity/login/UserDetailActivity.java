package com.example.zyl.dqcar.moudels.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.MainActivity;
import com.example.zyl.dqcar.moudels.bean.CarAdressChooseBean;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.SexDialog;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;

/**
 * Author: Zhaoyl
 * Date: 2017/7/4 10:07
 * Description: 个人基础信息
 * PackageName: UserDetailActivity
 * Copyright: 端趣网络
 **/
public class UserDetailActivity extends BaseActivity implements View.OnClickListener {

    EditText etnUserName;
    TextView tvAreas;
    TextView tvSex;
    SexDialog sexDialog;
    SexDialog.Sex curSex;
    String areas, areasNumber;

    @Override
    public int getLayout() {
        return R.layout.activity_userdetail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        etnUserName = $(R.id.etnUserName);
        tvAreas = $(R.id.tvAreas);
        tvSex = $(R.id.tvSex);
        $(R.id.btnNext).setOnClickListener(this);
        tvAreas.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        onClickListener();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                String name = etnUserName.getText().toString();
                String sex = tvSex.getText().toString();

                if (CheckUtil.isNull(name)) {
                    ToastUtil.getInstance().show("请填写名称！");
                    return;
                }
                if (CheckUtil.isNull(areas) || CheckUtil.isNull(areasNumber)) {
                    ToastUtil.getInstance().show("请选择区域！");
                    return;
                }
                if (CheckUtil.isNull(sex)) {
                    ToastUtil.getInstance().show("请选择性别！");
                    return;
                }
                next(name, areas, areasNumber, sex);
                break;
            case R.id.tvAreas:
                getAdress();
                break;
            case R.id.tvSex:
                sexDialog.show();
                break;
        }
    }

    private void onClickListener() {
        //性别对话框
        sexDialog = new SexDialog(this);
        sexDialog.setOnSexCallBack(new SexDialog.OnSexCallBack() {
            @Override
            public void onSexCallBack(SexDialog.Sex sex) {
                curSex = sex;
                tvSex.setHint("");
                tvSex.setText(curSex.getStr());
            }
        });
    }

    private void getAdress() {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_FINDCARAREALIST
                , new HttpManager.ResponseCallback<CarAdressChooseBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(CarAdressChooseBean o) {
                        if (o.errorCode.equals("0000")) {
                            if (o.items.size() > 0) {
                                onLinkagePicker(o.items);
                            }
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    public void onLinkagePicker(final List<CarAdressChooseBean.ItemsBeanX> items) {
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    firstList.add(items.get(i).name);
                }
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int p = 0; p < items.get(firstIndex).items.size(); p++) {
                    secondList.add(items.get(firstIndex).items.get(p).name);
                }
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(this, provider);
        picker.setCanLoop(true);
        picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {

            @Override
            public void onItemPicked(String first, String second, String third) {
                tvAreas.setText(first + "-" + second);
                areas = first;
                areasNumber = second;
            }
        });
        picker.show();
    }


    private void next(String name, String areas, String areasNumber, String sex) {
        Log.e("AAA", "next: " + name + "--" + areas + "--" + areasNumber + "--" + sex);
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_GOUSERDETAIL + "?name=" + name + "&areas=" + areas + "&number=" + areasNumber + "&sex=" + sex
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {
                        Log.e("AAA", "onFail: ---失败--->");
                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("填写信息成功！");
                            startActivity(new Intent(UserDetailActivity.this, MainActivity.class));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }
}
