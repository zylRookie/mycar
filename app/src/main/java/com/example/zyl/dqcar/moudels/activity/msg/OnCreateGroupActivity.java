package com.example.zyl.dqcar.moudels.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.VH;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.activity.chat.DqChatActivity;
import com.example.zyl.dqcar.moudels.bean.OnCreateGroupBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.CircleTransform;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.utils.permission.PermissionReq;
import com.example.zyl.dqcar.utils.permission.PermissionResult;
import com.example.zyl.dqcar.utils.permission.Permissions;
import com.example.zyl.dqcar.view.CustomPopWindow;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.zyl.dqcar.common.DqCarApplication.oss;


/**
 * Author: Zhaoyl
 * Date: 2017/9/20 16:56
 * Description: 创建群组
 * PackageName: OnCreateGroupActivity
 * Copyright: 端趣网络
 **/

public class OnCreateGroupActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout rlGroupType;
    TextView tvGroupType;
    EditText editGroupName;
    ImageView ivGroupHead;
    RecyclerView recyclerView;
    CustomPopWindow mListPopWindow;
    TextView tvRightTitle;
    String type;
    File headFile;
    int groupImage[] = {
            R.mipmap.nojoin_news,
            R.mipmap.nojoin_public,
            R.mipmap.nojoin_one,
            R.mipmap.nojoin_two,
            R.mipmap.nojoin_three,
            R.mipmap.nojoin_four,
            R.mipmap.nojoin_five,
            R.mipmap.nojoin_six,
            R.mipmap.nojoin_end};

    String groupName[] = {
            "新人报道大厅",
            "官方活动大厅",
            "华东区社群",
            "华南区社群",
            "华中区社群",
            "华北区社群",
            "西北区社群",
            "西南区社群",
            "东北区社群"
    };

    @Override
    public int getLayout() {
        return R.layout.activity_oncreategroup;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) $(R.id.tvTitle)).setText("创建群");
        tvRightTitle = $(R.id.tvRightTitle);
        tvRightTitle.setText("发布");
        ivGroupHead = $(R.id.ivGroupHead);
        rlGroupType = $(R.id.rlGroupType);
        tvGroupType = $(R.id.tvGroupType);
        editGroupName = $(R.id.editGroupName);
        $(R.id.ivBaseBack).setOnClickListener(this);
        $(R.id.rlRight).setOnClickListener(this);
        ivGroupHead.setOnClickListener(this);
        rlGroupType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBaseBack:
                finish();
                break;
            case R.id.rlRight:
                if (headFile == null) {
                    ToastUtil.getInstance().show("请选择群头像");
                    return;
                }
                if (CheckUtil.isNull(tvGroupType.getText().toString())) {
                    ToastUtil.getInstance().show("请选择群类型");
                    return;
                }
                if (CheckUtil.isNull(editGroupName.getText().toString())) {
                    ToastUtil.getInstance().show("请填写群昵称");
                    return;
                }
                initOss("create/group/header_" + System.currentTimeMillis() + ".jpg", headFile.getAbsolutePath());
                break;
            case R.id.ivGroupHead:
                PermissionReq.with(this)
                        .permissions(Permissions.CAMERA)
                        .result(new PermissionResult() {
                            @Override
                            public void onGranted() {
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setPreviewEnabled(false)
                                        .start(OnCreateGroupActivity.this);
                            }

                            @Override
                            public void onDenied() {
                                ToastUtil.getInstance().show("没有权限，无法打开摄像头！");
                            }
                        })
                        .request();
                break;
            case R.id.rlGroupType:
                initPopupWindow();
                break;
        }
    }

    private void initPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
        recyclerView = contentView.findViewById(R.id.recyclerView_GroupName);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        //处理popWindow 显示内容
        handleListView();
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setAnimationStyle(R.style.AnimBottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, getHight(OnCreateGroupActivity.this) * 2 / 3)//显示大小
                .create().showAtLocation(tvGroupType, Gravity.BOTTOM, 0, 0);
    }

    private void handleListView() {
        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class MyAdapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_mygroup_def, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.ivAvatar.setImageResource(groupImage[position]);
            viewHolder.tvGroupContent.setText(groupName[position]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvGroupType.setText(groupName[position]);
                    type = position + 1 + "";
                    mListPopWindow.dissmiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return groupImage.length;
        }

        public class ViewHolder extends VH {

            ImageView ivAvatar;
            TextView tvGroupContent;

            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = $(R.id.ivAvatar);
                tvGroupContent = $(R.id.tvGroupContent);
            }

        }


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
                        Picasso.with(OnCreateGroupActivity.this).load(file).transform(new CircleTransform()).into(ivGroupHead);
                        headFile = file;
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
                String imgkey = "http://dphuibaocar.oss-cn-hangzhou.aliyuncs.com/" + request.getObjectKey();
                if (!CheckUtil.isNull(imgkey)) {
                    commit(imgkey);
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

    private void commit(String imgkey) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("imgKey", imgkey);
        maps.put("type", type);
        maps.put("name", editGroupName.getText().toString());
        JSONObject json = new JSONObject(maps);
        String jsons = json.toJSONString();
        HttpManager.getInstance().postJson(WebAPI.MsgApi.MINE_NEWGROUP, jsons
                , new HttpManager.ResponseCallback<OnCreateGroupBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(OnCreateGroupBean o) {
                        if (o.errorCode.equals("0000")) {
                            startGroup(o.imGroupModel.id, o.imGroupModel.nameStr);
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);

                    }
                });
    }

    private void startGroup(String id, String name) {
        Intent in = new Intent(OnCreateGroupActivity.this, DqChatActivity.class);
        in.putExtra("userName", name);
        in.putExtra("type", "groupChat");
        in.putExtra("sourceUserId", id);
        startActivity(in);
        finish();
    }

    //获取屏幕高度
    public static int getHight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int hight = wm.getDefaultDisplay().getHeight();
        return hight;
    }

}
