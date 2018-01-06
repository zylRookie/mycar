package com.example.zyl.dqcar.moudels.activity.riders;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.chat.CameraActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.KeyBoardUtil;
import com.example.zyl.dqcar.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
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
 * Date: 2017/7/7 15:35
 * Description: 写说说
 * PackageName: EditRidersActivity
 * Copyright: 端趣网络
 **/

public class EditRidersActivity extends BaseActivity implements View.OnClickListener {

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    RecyclerView recyclerView;
    VideoView videoView;
    EditText etnContent;
    PhotoAdapter photoAdapter;
    MediaController mediaController;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    List<String> photos = new ArrayList<>();
    List<String> ossPhotos = new ArrayList<>();
    Map<String, Object> maps;
    int positions;
    int type = 1;
    int num = 0;
    int num1 = 0;
    String path;
    String bitmap;

    @Override
    public int getLayout() {
        return R.layout.activity_editriders;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvTitle)).setText("车友圈发布");
        recyclerView = $(R.id.rvImage);
        videoView = $(R.id.videoView);
        etnContent = $(R.id.etnContent);
        $(R.id.btnNext).setOnClickListener(this);
        $(R.id.ivBaseBack).setOnClickListener(this);

        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                KeyBoardUtil.hideSoftKeyboard(EditRidersActivity.this);
                positions = position;
                new AlertView(null, null, "取消", null, new String[]{"照片", "拍摄"}, EditRidersActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0://照片
                                type = 2;
                                if (photoAdapter.getItemViewType(positions) == PhotoAdapter.TYPE_ADD) {
                                    PhotoPicker.builder()
                                            .setPhotoCount(PhotoAdapter.MAX)
                                            .setShowCamera(true)
                                            .setPreviewEnabled(false)
                                            .setSelected(selectedPhotos)
                                            .start(EditRidersActivity.this);
                                } else {
                                    PhotoPreview.builder()
                                            .setPhotos(selectedPhotos)
                                            .setCurrentItem(positions)
                                            .start(EditRidersActivity.this);
                                }
                                break;
                            case 1://拍摄
                                type = 3;
                                getPermissions();
                                break;
                        }
                    }
                }).show();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.btnNext:
                if (CheckUtil.isNull(etnContent.getText().toString()) && selectedPhotos.size() == 0 && CheckUtil.isNull(path)) {
                    ToastUtil.getInstance().show("内容不能为空");
                    return;
                }

                switch (type) {
                    case 2://上传图片
                        photos.clear();
                        if (selectedPhotos != null) {
                            for (String photo : selectedPhotos) {
                                compressWithLs(new File(photo), "photo");
                            }
                        }
                        break;
                    case 3://上传视频
                        if (!CheckUtil.isNull(bitmap)) {
                            compressWithLs(new File(bitmap), "videoI");
                        }
                        if (!CheckUtil.isNull(path)) {
                            initOss("release/riderRelease_" + System.currentTimeMillis() + ".mp4", path, "video");
                        }
                        break;
                    case 1://只有文字
                        commit(null, 0, null, 0);
                        break;
                }
                break;
        }
    }


    private void commit(List<String> files, int fileType, String viedoImg, int length) {
        maps = new HashMap<>();
        maps.put("content", etnContent.getText().toString());
        maps.put("files", files);
        maps.put("fileType", fileType);
        maps.put("viedoImg", viedoImg);
        maps.put("length", length);
        JSONObject json = new JSONObject(maps);
        String jsons = json.toJSONString();
        HttpManager.getInstance().postJson(WebAPI.FriendsApi.FRIENDSAPI_ADDCIRCLE, jsons
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("发布成功");
                            EventBus.getDefault().post(new EventCenter(EventBusConst.ADD_RIDER));
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    private void nextTry() {
        for (String photo : photos) {
            initOss("release/riderRelease_" + System.currentTimeMillis() + ".jpg", photo, "photo");
        }
    }

    private void compressWithLs(File file, final String type) {
        Luban.with(this)
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        if (type.equals("videoI")) {
                            initOss("msg/msg_video_" + System.currentTimeMillis() + ".jpg", file.getAbsolutePath(), "videoImg");
                        } else {
                            num++;
                            photos.add(file.getAbsolutePath());
                            Log.e("AAA", "onSuccess: " + num);
                            if (selectedPhotos.size() == num) {
                                nextTry();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    String videoPath = null;
    String videoImg = null;

    private void initOss(String hou, String uploadFilePath, final String type) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("dphuibaocar", hou, uploadFilePath);
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                switch (type) {
                    case "photo":
                        num1++;
                        String imgkey = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        ossPhotos.add(imgkey);
                        if (selectedPhotos.size() == num1) {
                            commit(ossPhotos, 0, null, 0);
                        }
                        break;
                    case "video":
                        ossPhotos.clear();
                        videoPath = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        ossPhotos.add(videoPath);
//                        commit(ossPhotos);
                        break;
                    case "videoImg":
                        videoImg = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        break;
                }

                if (type.equals("video") || type.equals("videoImg")) {
                    Log.e("AAA", "onSuccess: --videoImg->" + videoImg);
                    Log.e("AAA", "onSuccess: --videoPath->" + videoPath);
                    if (videoImg != null && videoPath != null) {
                        commit(ossPhotos, 2, videoImg, 0);
                    }
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


    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(EditRidersActivity.this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(EditRidersActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(EditRidersActivity.this, CameraActivity.class), 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }

        if (resultCode == 101) {
            String path = data.getStringExtra("path");
            Log.e("CJT", "picture======>" + path);
            ToastUtil.getInstance().show("请从照片中进行拍摄");
            if (checkSdState()) {
//                Log.e("AAA", "onActivityResult:------>");
                deleteAllFiles(new File(path));
            }
        }
        if (resultCode == 102) {
            path = data.getStringExtra("path");
            bitmap = data.getStringExtra("bitmap");
            Log.e("CJT", "video=======>" + path);
            recyclerView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            play(path);
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
    }

    private void play(final String path) {
        mediaController = new MediaController(this);
        videoView.setVideoPath(path);
        // 设置VideView与MediaController建立关联
        videoView.setMediaController(mediaController);
//        // 设置MediaController与VideView建立关联
        mediaController.setMediaPlayer(videoView);
        mediaController.setVisibility(View.INVISIBLE);
        // 让VideoView获取焦点
//        videoView.requestFocus();
        // 开始播放
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    /**
     * 判断是否有sd卡
     */
    public boolean checkSdState() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(EditRidersActivity.this, CameraActivity.class), 100);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
