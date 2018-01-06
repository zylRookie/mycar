package com.example.zyl.dqcar.moudels.activity.mine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.riders.MyPhotoAdapter;
import com.example.zyl.dqcar.moudels.activity.riders.RecyclerItemClickListener;
import com.example.zyl.dqcar.moudels.bean.ReleaseBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.zyl.dqcar.common.DqCarApplication.oss;

/**
 * Author: Zhaoyl
 * Date: 2017/6/27 10:56
 * Description: 发布二手车
 * PackageName: ReleaseOldCarActivity
 * Copyright: 端趣网络
 **/

public class ReleaseOldCarActivity extends BaseActivity implements View.OnClickListener {

    EditText etContent;
    EditText etnCarType;
    TextView etnCarTime;
    TextView tvCarStar;
    EditText etnCarL;
    EditText etnCarMoney;
    EditText etnBakDetail;
    LinearLayout llOldCarType;
    RecyclerView rv_image;
    MyPhotoAdapter myPhotoAdapter;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    List<String> photos = new ArrayList<>();

    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    int num = 0;
    int num1 = 0;
    String type;
    String id;

    @Override
    public int getLayout() {
        return R.layout.activity_release_oldcar;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        ((TextView) $(R.id.tvBaseLeft)).setText("发布二手车");
        etContent = $(R.id.etContent);
        etnCarType = $(R.id.etnCarType);
        etnCarTime = $(R.id.etnCarTime);
        etnCarL = $(R.id.etnCarL);
        etnCarMoney = $(R.id.etnCarMoney);
        etnBakDetail = $(R.id.etnBakDetail);
        rv_image = $(R.id.rv_image);
        llOldCarType = $(R.id.llOldCarType);
        tvCarStar = $(R.id.tvCarStar);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        if (type != null && type.equals("2")) {
            llOldCarType.setVisibility(View.GONE);
        } else {
            llOldCarType.setVisibility(View.VISIBLE);
        }
        $(R.id.btnRelease).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);
        etnCarTime.setOnClickListener(this);
        tvCarStar.setOnClickListener(this);


        if (!CheckUtil.isNull(id)) {
            initData(id);
        }

        myPhotoAdapter = new MyPhotoAdapter(this, selectedPhotos);
        rv_image.setLayoutManager(new LinearLayoutManager(ReleaseOldCarActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        rv_image.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.HORIZONTAL));
        rv_image.setAdapter(myPhotoAdapter);
        rv_image.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (myPhotoAdapter.getItemViewType(position) == MyPhotoAdapter.TYPE_ADD) {
                    PhotoPicker.builder()
                            .setPhotoCount(MyPhotoAdapter.MAX)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .setSelected(selectedPhotos)
                            .start(ReleaseOldCarActivity.this);
                } else {
                    PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(position)
                            .start(ReleaseOldCarActivity.this);
                }
            }
        }));

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    private void initData(String id) {
        HttpManager.getInstance().get(WebAPI.MineApi.MINE_FINDBYPRODUCTID + "?productId=" + id
                , new HttpManager.ResponseCallback<ReleaseBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseBean o) {
                        if (o.errorCode.equals("0000")) {//图片列表
                            etContent.setText(o.ptPtoductModel.title);
                            etnCarType.setText(o.ptPtoductModel.carNorm);
                            etnCarTime.setText(o.ptPtoductModel.carYear);
                            etnCarL.setText(o.ptPtoductModel.carDisplacement);
                            etnCarMoney.setText(o.ptPtoductModel.priceStr);
                            etnBakDetail.setText(o.ptPtoductModel.detial);
                            tvCarStar.setText(o.ptPtoductModel.carBrand);

                            selectedPhotos.clear();
//                            photos.add(o.ptPtoductModel.lstImage);
//                            photos.add(o.ptPtoductModel.contentImage1);
//                            photos.add(o.ptPtoductModel.contentImage2);
//                            photos.add(o.ptPtoductModel.contentImage3);
//                            photos.add(o.ptPtoductModel.contentImage4);
//                            photos.add(o.ptPtoductModel.contentImage5);

                            if (photos != null) {
                                selectedPhotos.addAll(photos);
                            }
                            myPhotoAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.tvCarStar:
                startActivity(new Intent(ReleaseOldCarActivity.this, StarCarActivity.class));
                break;
            case R.id.etnCarTime:
                showDialog(DATE_DIALOG);
                break;
            case R.id.btnRelease:
                if (type.equals("2")) {
                    if (CheckUtil.isNull(etContent.getText().toString())) {
                        ToastUtil.getInstance().show("标题不能为空！");
                        return;
                    }

                    if (CheckUtil.isNull(etnCarMoney.getText().toString())) {
                        ToastUtil.getInstance().show("价格不能为空！");
                        return;
                    }
                    if (photos.size() == 0) {
                        ToastUtil.getInstance().show("图片不能为空！");
                        return;
                    }
                } else {
                    if (CheckUtil.isNull(etContent.getText().toString())) {
                        ToastUtil.getInstance().show("标题不能为空！");
                        return;
                    }
                    if (CheckUtil.isNull(etnCarType.getText().toString())) {
                        ToastUtil.getInstance().show("车型不能为空！");
                        return;
                    }
                    if (CheckUtil.isNull(tvCarStar.getText().toString())) {
                        ToastUtil.getInstance().show("品牌不能为空！");
                        return;
                    }
                    if (CheckUtil.isNull(etnCarTime.getText().toString())) {
                        ToastUtil.getInstance().show("年份不能为空！");
                        return;
                    }
                    if (CheckUtil.isNull(etnCarL.getText().toString())) {
                        ToastUtil.getInstance().show("排量不能为空！");
                        return;
                    }
                    if (CheckUtil.isNull(etnCarMoney.getText().toString())) {
                        ToastUtil.getInstance().show("价格不能为空！");
                        return;
                    }
                    if (photos.size() == 0) {
                        ToastUtil.getInstance().show("图片不能为空！");
                        return;
                    }
                }
                selectedPhotos.clear();
                if (photos != null) {
                    for (String photo : photos) {
                        compressWithLs(new File(photo));
                    }
                }


//                next();

                break;
        }
    }

    private void nextTry() {
        photos.clear();
        for (String photo : selectedPhotos) {
            initOss("release/releaseOldCar_" + System.currentTimeMillis() + ".jpg", photo);
        }
    }

    private void next() {
        Map<String, Object> maps = new HashMap<>();
         /*{
                    "userId":"be867f25-3058-4d02-a65f-cfc2f6849258",
                    "userName":"啥子事",
                    "lstImage":"kk",
                    "contentImage1":"ll",
                    "contentImage2":"ll",
                    "contentImage3":"ll",
                    "contentImage4":"ll",
                    "contentImage5":"ll",
                    "title":"奔驰",
                    "carNorm":"s20",
                    "carYear":"2017-07-26T15:59:25.4221937+08:00",
                    "carDisplacement":"1.5l",
                    "state":1,
                    "priceStr":"20",
                    "price":2.2,
                    "num":1,
                    "aNum":1,
                    "detial":"哈哈哈哈",
                    "userNum":1
                }*/

//            json.putOpt("userId", "1");
//            json.putOpt("userName", "啥子事");
//            json.putOpt("lstImage", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("contentImage1", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("contentImage2", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("contentImage3", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("contentImage4", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("contentImage5", "https://img13.360buyimg.com/n5/jfs/t5743/306/2360895402/325421/31e7da3e/592f932cNc77c7831.jpg");
//            json.putOpt("num", 2);
//            json.putOpt("aNum", 0);
//            json.putOpt("state", 1);
//            json.putOpt("priceStr", 20);
        maps.put("id", id);
//            json.putOpt("lstImage", selectedPhotos);
        maps.put("imgLst", photos);
        maps.put("title", etContent.getText().toString());
        maps.put("carNorm", etnCarType.getText().toString());
        maps.put("carBrand", tvCarStar.getText().toString());
        maps.put("carYear", etnCarTime.getText().toString());
        maps.put("carDisplacement", etnCarL.getText().toString());
        maps.put("price", etnCarMoney.getText().toString());
        maps.put("detial", etnBakDetail.getText().toString());
        maps.put("saleType", type);
        JSONObject json = new JSONObject(maps);
        String jsons = json.toJSONString();
        Log.e("AAA", "next: =======>"+jsons);
        HttpManager.getInstance().postJson(WebAPI.MineApi.MINE_MYRELEASE, jsons
                , new HttpManager.ResponseCallback<ReleaseBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(ReleaseBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("发布成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_OLDCAR,type));
                            finish();
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            myPhotoAdapter.notifyDataSetChanged();
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
                        num++;
                        selectedPhotos.add(file.getAbsolutePath());
                        Log.e("AAA", "onSuccess: " + num);
                        if (photos.size() == num) {
                            nextTry();
                        }
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
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                num1++;
                Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                String imgkey = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                photos.add(imgkey);
                if (selectedPhotos.size() == num1) {
                    next();
                }
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        etnCarTime.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.CHOOSE_CARSTAR) {
            String carBrand = (String) eventCenter.getData();
            tvCarStar.setText(carBrand);
        }
    }
}
