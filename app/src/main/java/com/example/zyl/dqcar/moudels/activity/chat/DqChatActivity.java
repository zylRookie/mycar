package com.example.zyl.dqcar.moudels.activity.chat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.RolesBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.utils.permission.PermissionReq;
import com.example.zyl.dqcar.utils.permission.PermissionResult;
import com.example.zyl.dqcar.utils.permission.Permissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hadcn.keyboard.ChatKeyboardLayout;
import cn.hadcn.keyboard.RecordingLayout;
import cn.hadcn.keyboard.media.MediaBean;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.zyl.dqcar.common.DqCarApplication.oss;
import static com.example.zyl.dqcar.utils.SdCardUtil.checkSdState;


/**
 * Author: Zhaoyl
 * Date: 2017/8/30 10:56
 * Description: 聊天
 * PackageName: DqChatActivity
 * Copyright: 端趣网络
 **/

public class DqChatActivity extends AppCompatActivity implements View.OnClickListener, MediaBean
        .MediaListener {

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private static final int PERMISSION_REQUEST_CODE = 0;
    ChatKeyboardLayout keyboardLayout;
    RecyclerView recyclerView;
    DqChatAdapter dqChatAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    RecordingLayout rlRecordArea;
    RelativeLayout rlRight, rlRightImg;
    ImageView ivRight, ivRightImg;
    String userName, type, sourceUserId, url, urlSend, IdKey;
    int pageIndex = 1;
    int roles;
    boolean isOK;
    MediaPlayer mMediaPlayer;
    String mVoicePath;
    List<DqChatBean.ItemsBean> items = new ArrayList<>();
    ImageView ivBaseBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dqchat);
        EventBus.getDefault().register(this);
        userName = getIntent().getStringExtra("userName");
        type = getIntent().getStringExtra("type");
        sourceUserId = getIntent().getStringExtra("sourceUserId");
        initView();
        initData();
    }

    private void initData() {
        if (type.equals("oneChat")) {//个聊
            url = WebAPI.ChatApi.CHATAPI_FINDMSGLOG;
            urlSend = WebAPI.ChatApi.CHATAPI_SENDMSG;
            IdKey = "?userId=";

        } else if (type.equals("groupChat")) {//群聊
            ivRight.setImageResource(R.mipmap.group);
            rlRight.setOnClickListener(this);
            if (!CheckUtil.isNull(BaseSharedPreferences.getRoleType(DqChatActivity.this))) {
                if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatActivity.this)) >= 4) {
//                    ivRightImg.setImageResource(roles == 0 ? R.mipmap.group : R.mipmap.group);
                    rlRightImg.setOnClickListener(this);
                }
            }
            url = WebAPI.ChatApi.CHATAPI_GROUPFINDMSGLOG;
            urlSend = WebAPI.ChatApi.CHATAPI_GROUPSENDMSG;
            IdKey = "?groupId=";
        }
        getLinearLayoutManager();
        dqChatAdapter = new DqChatAdapter();
        recyclerView.setAdapter(dqChatAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex++;
                getData(pageIndex);
            }
        });
        getData(pageIndex);
    }

    private void initView() {
        rlRight = (RelativeLayout) findViewById(R.id.rlRight);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        rlRightImg = (RelativeLayout) findViewById(R.id.rlRightImg);
        ivRightImg = (ImageView) findViewById(R.id.ivRightImg);
        keyboardLayout = (ChatKeyboardLayout) findViewById(R.id.kv_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_chat);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ((TextView) findViewById(R.id.tvTitle)).setText(userName);
        rlRecordArea = (RecordingLayout) findViewById(R.id.recording_area);
        ivBaseBack = (ImageView) findViewById(R.id.ivBaseBack);
        ivBaseBack.setOnClickListener(this);
        initKeyBoard();
    }


    private void getData(final int pageIndex) {
        HttpManager.getInstance().get(url
                        + IdKey + sourceUserId
                        + "&pageIndex=" + pageIndex + ""
                        + "&pageSize=" + "10"
                , new HttpManager.ResponseCallback<DqChatBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(DqChatBean o) {
                        roles = o.roles;
                        if (type.equals("groupChat")) {
                            if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatActivity.this)) >= 4) {
                                ivRightImg.setImageResource(roles == 0 ? R.mipmap.unroles : R.mipmap.roles);
                                isOK = roles == 0 ? false : true;
                            }
                        }
                        if (o.errorCode.equals("0000")) {
                            if (o.items != null && o.items.size() > 0) {
                                if (pageIndex == 1) {
                                    dqChatAdapter.setData(DqChatActivity.this, o.items, type);
                                    scrollToBottom();
                                } else {
                                    dqChatAdapter.addData(DqChatActivity.this, o.items, type);
                                }
                            } else {
                                if (pageIndex == 1) {
                                    ToastUtil.getInstance().show("暂无对话");
                                } else {
                                    ToastUtil.getInstance().show("已没更多会话");
                                }
                            }
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBaseBack:
                keyboardLayout.hideKeyboard();
                finish();
                break;
            case R.id.rlRight:
                Intent in = new Intent(DqChatActivity.this, DqChatGroupActivity.class);
                in.putExtra("groupId", sourceUserId);
                startActivity(in);
                break;
            case R.id.rlRightImg://禁言
                if (isOK) {
                    roles = 0;
                    isOK = false;
                } else {
                    roles = 1;
                    isOK = true;
                }
                gag(roles);
                break;

        }
    }

    private void gag(int roles) {
        HttpManager.getInstance().get(WebAPI.ChatApi.CHATAPI_MODIFYROLES
                        + "?groupId=" + sourceUserId
                        + "&roles=" + roles
                , new HttpManager.ResponseCallback<RolesBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(RolesBean o) {
                        if (o.errorCode.equals("0000")) {
                            ivRightImg.setImageResource(o.imGroupModel.roles == 0 ? R.mipmap.unroles : R.mipmap.roles);
//                            ToastUtil.getInstance().show(o.imGroupModel.roles == 0 ? "解言成功" : "禁言成功");
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }


    private void initKeyBoard() {
        ArrayList<MediaBean> popupModels = new ArrayList<>();
        popupModels.add(new MediaBean(0, R.mipmap.icon_chat_image_start, "照片", this));
        popupModels.add(new MediaBean(1, R.mipmap.icon_chat_photo_new, "拍摄", this));
//        popupModels.add(new MediaBean(2, R.mipmap.icon_chat_photo_new, "视频", this));
        popupModels.add(new MediaBean(3, R.mipmap.icon_chat_money, "红包", this));
//        popupModels.add(new MediaBean(4, R.mipmap.icon_chat_path, "位置", this));
//        popupModels.add(new MediaBean(5, R.mipmap.icon_chat_user, "名片", this));
        keyboardLayout.initMediaContents(popupModels);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                keyboardLayout.hideKeyboard();
                return false;
            }
        });

        keyboardLayout.setOnChatKeyBoardListener(new ChatKeyboardLayout.OnChatKeyBoardListener() {
            @Override
            public void onSendButtonClicked(String s) {
                sendContent(s, null, 0, null, 0);
                keyboardLayout.clearInputContent();
            }

            @Override
            public void onInputTextChanged(String s) {

            }

            @Override
            public void onRecordingAction(ChatKeyboardLayout.RecordingAction recordingAction) {
                switch (recordingAction) {
                    case START:
                        mVoicePath = AudioLib.getInstance().generatePath();
                        AudioLib.getInstance().start(mVoicePath, new AudioListener());
                        rlRecordArea.show(1);
                        break;
                    case RESTORE:
                        rlRecordArea.show(1);
                        break;
                    case READY_CANCEL:
                        rlRecordArea.show(0);
                        break;
                    case CANCELED:
                        AudioLib.getInstance().cancel();
                        rlRecordArea.hide();
                        break;
                    case COMPLETE:
                        if (AudioLib.getInstance().complete() < 0) {
                            ToastUtil.getInstance().show("时间太短");
                            rlRecordArea.hide();
                            return;
                        } else {
                            rlRecordArea.hide();
                        }
                        if (!CheckUtil.isNull(mVoicePath)) {
                            initOss("msg/msg_record_" + System.currentTimeMillis() + ".mp3", mVoicePath, "record", AudioLib.getInstance().complete());
                        }
                        break;
                    case PERMISSION_NOT_GRANTED:
                        ActivityCompat.requestPermissions(DqChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
                        break;
                }
            }

            @Override
            public void onUserDefEmoticonClicked(String s, String s1) {
                Log.e("AAA", "onUserDefEmoticonClicked: s=" + s);
                Log.e("AAA", "onUserDefEmoticonClicked: s1=" + s1);
//                sendContent(s, "sssss.sss", 4, null, 0);
            }

            @Override
            public void onKeyboardHeightChanged(int i) {
                if (i >= 600)
                    scrollToBottom();
            }

            @Override
            public boolean onLeftIconClicked(View view) {
                return false;
            }

            @Override
            public boolean onRightIconClicked(View view) {
                return false;
            }
        });
    }

    private class AudioListener implements AudioLib.OnAudioListener {
        @Override
        public void onDbChange(double db) {
            int level = 0;
            Log.e("", "onDbChange db = " + db);
            if (db > 40) {
                level = ((int) db - 40) / 7;
            }
            Log.e("", "onDbChange level = " + level);
            rlRecordArea.setVoiceLevel(level);
        }
    }

    private void sendContent(String s, String fileModel, int fileType, String viedoImg, int length) {
        if (type.equals("groupChat")) {
            if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatActivity.this)) < 4) {
                if (roles == 1) {
                    ToastUtil.getInstance().show("群主已禁言");
                    return;
                }
            }
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("groupId", sourceUserId);
        maps.put("content", s);
        maps.put("fileModel", fileModel);
        maps.put("fileType", fileType);
        maps.put("viedoImg", viedoImg);
        maps.put("length", length);
        JSONObject json = new JSONObject(maps);
        String jsons = json.toJSONString();
        HttpManager.getInstance().postJson(urlSend, jsons
                , new HttpManager.ResponseCallback<BaseBackBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(BaseBackBean o) {
                        if (o.errorCode.equals("0000")) {
                            ToastUtil.getInstance().show("成功");
                            initDataType(1, false);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                });
    }

    @Override
    public void onMediaClick(int i) {
        switch (i) {
            case 0://照片
                PermissionReq.with(this)
                        .permissions(Permissions.CAMERA)
                        .result(new PermissionResult() {
                            @Override
                            public void onGranted() {
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(false)
                                        .setPreviewEnabled(false)
                                        .start(DqChatActivity.this);
                            }

                            @Override
                            public void onDenied() {
                                ToastUtil.getInstance().show("没有权限，无法打开摄像头！");
                            }
                        })
                        .request();
                break;
            case 1://拍照
                getPermissions();
                break;
            case 3://红包
                startActivity(new Intent(DqChatActivity.this, SendRedActivity.class));
                break;
            case 4://位置

                break;
            case 5://名片

                break;
        }
    }

    //设置线性布局的管理者
    private void getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DqChatActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void scrollToBottom() {
        recyclerView.requestLayout();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    private void initDataType(int pageIndex, final boolean isRoles) {
        HttpManager.getInstance().get(url
                        + IdKey + sourceUserId
                        + "&pageIndex=" + pageIndex + ""
                        + "&pageSize=" + "10"
                , new HttpManager.ResponseCallback<DqChatBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(DqChatBean o) {
                        Log.e("AAA", "onSuccess: ----->xiancheng" + Thread.currentThread().getName());
                        if (o.errorCode.equals("0000")) {
                            if (isRoles) {
                                roles = o.roles;
                                if (type.equals("groupChat")) {
                                    if (Integer.parseInt(BaseSharedPreferences.getRoleType(DqChatActivity.this)) >= 4) {
                                        ivRightImg.setImageResource(roles == 0 ? R.mipmap.unroles : R.mipmap.roles);
                                        isOK = roles == 0 ? false : true;
                                    }
                                    ToastUtil.getInstance().show(roles == 0 ? "群主解除禁言了哦" : "群主禁言了哦");
                                }
                            } else {
                                dqChatAdapter.addOneData(DqChatActivity.this, o.items, type);
                                scrollToBottom();
                            }
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
            compressWithLs(imageFile, "photo");
        }

        if (resultCode == 101) {
            String path = data.getStringExtra("path");
            Log.e("AAA", "picture======>" + path);
            compressWithLs(new File(path), "photo");
            if (checkSdState()) {
//                Log.e("AAA", "onActivityResult:------>");
//                deleteAllFiles(new File(path));//删除待改进
            }
        }
        if (resultCode == 102) {
            String path = data.getStringExtra("path");
            String bitmap = data.getStringExtra("bitmap");
            if (!CheckUtil.isNull(bitmap)) {
                compressWithLs(new File(bitmap), "videoPhoto");
            }
            Log.e("AAA", "video=======>" + path);
            Log.e("AAA", "video===bitmap====>" + bitmap);
            if (!CheckUtil.isNull(path)) {
                initOss("msg/msg_video_" + System.currentTimeMillis() + ".mp4", path, "video", 0);
            }
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
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
                        if (type.equals("videoPhoto")) {
                            initOss("msg/msg_video_" + System.currentTimeMillis() + ".jpg", file.getAbsolutePath(), "videoPhoto", 0);
                        } else {
                            Log.e("AAA", "onActivityResult:---hou---> " + file.getAbsolutePath());
                            initOss("msg/msg_photo_" + System.currentTimeMillis() + ".jpg", file.getAbsolutePath(), "photo", 0);
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

    private void initOss(String hou, String uploadFilePath, final String type, final int recordLength) {
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
                switch (type) {
                    case "photo":
                        Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                        String imgkey = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        sendContent("", imgkey, 0, null, 0);
                        break;
                    case "videoPhoto":
                        Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                        videoImg = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        break;
                    case "video":
                        Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                        videoPath = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        break;
                    case "record":
                        Log.e("AAA", "onSuccess:" + "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey());
                        String recordPath = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                        sendContent("", recordPath, 3, null, recordLength);
                        break;
                }

                if (type.equals("video") || type.equals("videoPhoto")) {
                    if (videoImg != null && videoPath != null) {
                        sendContent("", videoPath, 2, videoImg, 0);
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
                startActivityForResult(new Intent(DqChatActivity.this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(DqChatActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(DqChatActivity.this, CameraActivity.class), 100);
        }
    }


    /**
     * EventBus
     * 主线程运行
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventCenter eventCenter) {
        if (eventCenter != null) {
            onEventComing(eventCenter);
        }
    }

    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusConst.UPDATA_MSG) {
            if (type.equals("oneChat")) {//个聊
                initDataType(1, false);
            } else {
                Log.e("AAA", "onEventComing: ---群聊进来了哦不刷新---__>");
            }

        } else if (eventCenter.getEventCode() == EventBusConst.UPDATA_GROUPMSG) {
            if (type.equals("groupChat")) {//群聊
                initDataType(1, false);
            } else {
                Log.e("AAA", "onEventComing: ---个聊进来了哦不刷新---__>");
            }
        } else if (eventCenter.getEventCode() == EventBusConst.DELETE_GROUP) {
            finish();
        } else if (eventCenter.getEventCode() == EventBusConst.UPDATA_ROLES) {
            initDataType(1, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("AAA", "onDestroy: ------->刷新");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_UNREADMSG));
        EventBus.getDefault().unregister(this);
    }
}
