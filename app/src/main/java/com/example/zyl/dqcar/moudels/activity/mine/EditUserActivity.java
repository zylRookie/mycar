package com.example.zyl.dqcar.moudels.activity.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.CarAdressChooseBean;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.utils.permission.PermissionReq;
import com.example.zyl.dqcar.utils.permission.PermissionResult;
import com.example.zyl.dqcar.utils.permission.Permissions;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.example.zyl.dqcar.view.SexDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.zyl.dqcar.R.id.edit_userName;
import static com.example.zyl.dqcar.common.DqCarApplication.oss;

/**
 * Author: Zhaoyl
 * Date: 2017/6/28 15:21
 * Description: 修改个人信息
 * PackageName: EditUserActivity
 * Copyright: 端趣网络
 **/

public class EditUserActivity extends BaseActivity implements View.OnClickListener {


    ImageView ivHead;
    TextView tvSex;
    TextView tvPath;
    EditText editUserName;
    EditText etnBak;
    SexDialog sexDialog;
    SexDialog.Sex curSex;
    CustomPopWindow mCustomPopWindow;
    String imgkey = "";
    String areas, areasNumber;


    @Override
    public int getLayout() {
        return R.layout.activity_edituser;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ivHead = $(R.id.ivHead);
        tvSex = $(R.id.tvSex);
        tvPath = $(R.id.tvPath);
        editUserName = $(edit_userName);
        etnBak = $(R.id.etnBak);
        ivHead.setOnClickListener(this);
        ((TextView) $(R.id.tvTitle)).setText("个人信息");
        tvSex.setOnClickListener(this);
        tvPath.setOnClickListener(this);
        $(R.id.btnMotifyPwd).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

        onClickListener();

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
                                editUserName.setText(o.smUserModel.name);
                            if (!CheckUtil.isNull(o.smUserModel.areaS)) {
                                tvPath.setText(o.smUserModel.areaS + "-" + o.smUserModel.number);
                                areas = o.smUserModel.areaS;
                                areasNumber = o.smUserModel.number;
                            }
                            if (!CheckUtil.isNull(o.smUserModel.sex)) {
                                if (o.smUserModel.sex.equals("男")) {
                                    tvSex.setHint("");
                                    tvSex.setText("男");
                                    tvSex.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.img_boy), null);
                                } else {
                                    tvSex.setHint("");
                                    tvSex.setText("女");
                                    tvSex.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.img_girl), null);
                                }
                            }
                            if (!CheckUtil.isNull(o.smUserModel.bak))
                                etnBak.setText(o.smUserModel.bak);
                            if (!CheckUtil.isNull(o.smUserModel.imgKey)) {
                                Picasso.with(EditUserActivity.this).load(o.smUserModel.imgKey).transform(new CircleTransform()).into(ivHead);
                                Log.e("AAA", "onSuccess: ----->imgKey=" + o.smUserModel.imgKey);

                            }
                        }
                    }
                });
    }

    private void onClickListener() {
        tvSex.setHint("");
        tvSex.setText("女");
        tvSex.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.img_girl), null);

        //性别对话框
        sexDialog = new SexDialog(this);
        sexDialog.setOnSexCallBack(new SexDialog.OnSexCallBack() {
            @Override
            public void onSexCallBack(SexDialog.Sex sex) {
                curSex = sex;
                tvSex.setHint("");
                tvSex.setText(curSex.getStr());
                tvSex.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(curSex.getImageSourceID()), null);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHead:
                PermissionReq.with(this)
                        .permissions(Permissions.CAMERA)
                        .result(new PermissionResult() {
                            @Override
                            public void onGranted() {
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setPreviewEnabled(false)
                                        .start(EditUserActivity.this);
                            }

                            @Override
                            public void onDenied() {
                                ToastUtil.getInstance().show("没有权限，无法打开摄像头！");
                            }
                        })
                        .request();
                break;
            case R.id.tvSex:
                sexDialog.show();
                break;
            case R.id.tvPath:
//                showPopMenuM();
                getAdress();
                break;
            case R.id.btnMotifyPwd:
                next();
                break;
            case R.id.ivBaseBack:
                finish();
                break;
        }
    }

    private void next() {
        Log.e("AAA", "next: --->xingm==" + editUserName.getText().toString());
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_EDITUSER
                        + "?name=" + editUserName.getText().toString()
                        + "&areas=" + areas
                        + "&number=" + areasNumber
                        + "&sex=" + tvSex.getText().toString()
                        + "&bak=" + etnBak.getText().toString()
                        + "&imgkey=" + imgkey
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATE_USER));
                            finish();
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            ArrayList<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            File imageFile = new File(photos.get(0));
            Picasso.with(EditUserActivity.this).load(imageFile).transform(new CircleTransform()).into(ivHead);
            compressWithLs(imageFile);

        }

    }

    private void compressWithLs(File file) {
        Luban.with(this)
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.e("AAA", "onActivityResult:---hou---> " + file.getAbsolutePath());
                        initOss("head/header_" + System.currentTimeMillis() + ".jpg", file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    private void initOss(String hou, String uploadFilePath) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("dphuibaocar", hou, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                imgkey = URLEncoder.encode("http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
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
                tvPath.setText(first + "-" + second);
                areas = first;
                areasNumber = second;
            }
        });
        picker.show();
    }

}
