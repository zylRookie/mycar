package com.example.zyl.dqcar.moudels.activity.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.http.HttpManager;
import com.example.zyl.dqcar.http.WebAPI;
import com.example.zyl.dqcar.moudels.MainActivity;
import com.example.zyl.dqcar.moudels.bean.BaseBackBean;
import com.example.zyl.dqcar.moudels.bean.LoginBean;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Author: Zhaoyl
 * Date: 2017/7/3 11:19
 * Description:  登陆
 * PackageName: LoginActivity
 * Copyright: 端趣网络
 **/

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    EditText etPhone;
    EditText etPassword;
    ImageView iv_look;
    ImageView wxLogin;
    boolean IsLook = true;

    private ProgressDialog dialog;
    private UMShareAPI umShareAPI;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        iv_look = $(R.id.iv_look);
        wxLogin = $(R.id.wxLogin);
        etPhone = $(R.id.etPhone);
        etPassword = $(R.id.etPassword);
        wxLogin.setOnClickListener(this);
        $(R.id.rl_look).setOnClickListener(this);
        $(R.id.btnLogin).setOnClickListener(this);
        $(R.id.tvRegister).setOnClickListener(this);
        $(R.id.tvForgotPwd).setOnClickListener(this);

        if (!CheckUtil.isNull(BaseSharedPreferences.getPhoneNumber(LoginActivity.this))) {
            etPhone.setText(BaseSharedPreferences.getPhoneNumber(LoginActivity.this));
            etPassword.requestFocus();
        }

        dialog = new ProgressDialog(this);
        umShareAPI = UMShareAPI.get(this);
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        //相机、麦克风
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.CAMERA,
                        //存储空间
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_SETTINGS
                )
                .request();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //密码可见与不可见
            case R.id.rl_look:
                if (IsLook == true) {
                    iv_look.setImageResource(R.mipmap.iv_look);
                    IsLook = false;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.length());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    IsLook = true;
                    iv_look.setImageResource(R.mipmap.iv_not_look);
                    etPassword.setSelection(etPassword.length());
                }
                break;
            //登录
            case R.id.btnLogin:
                String phone = etPhone.getText() + "";
                String pwd = etPassword.getText() + "";
//                if (TextUtils.isEmpty(phone) || !CheckUtil.checkPhone(phone)) {
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.getInstance().show("账号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.getInstance().show("密码不能为空！");
                    return;
                }
                login(phone, pwd);
                break;
            //注册
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            //忘记密码
            case R.id.tvForgotPwd:
                startActivity(new Intent(LoginActivity.this, FindPwdActivity.class));
                break;
            //微信登录
            case R.id.wxLogin:
//                UMWeb web = new UMWeb("http://bbs.umeng.com/");
//                web.setTitle("This is music title");//标题
//                web.setThumb(new UMImage(LoginActivity.this, R.mipmap.ic_launcher));  //缩略图
//                web.setDescription("my description");//描述
//
//                new ShareAction(LoginActivity.this).setDisplayList(SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA)
//                        .withMedia(web)
//                        .setCallback(shareListener).open();
                umShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(LoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    private void login(String phone, String pwd) {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN + "?phone=" + phone + "&pwd=" + pwd
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {
                        ToastUtil.getInstance().show("网络！");
                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            BaseSharedPreferences.setSessionId(null, o.smUserModel.sessionId);
                            BaseSharedPreferences.setId(LoginActivity.this, o.smUserModel.id);
                            BaseSharedPreferences.setPhoneNumber(LoginActivity.this, o.smUserModel.phone);
                            BaseSharedPreferences.setUserName(LoginActivity.this, o.smUserModel.name);
                            BaseSharedPreferences.setUserHeadUrl(LoginActivity.this, o.smUserModel.imgKey);
                            BaseSharedPreferences.setNumber(LoginActivity.this, o.smUserModel.number);
                            BaseSharedPreferences.setRoleType(LoginActivity.this, o.smUserModel.roleType + "");
                            if (CheckUtil.isNull(o.smUserModel.number))
                                startActivity(new Intent(LoginActivity.this, UserDetailActivity.class));
                            else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                EventBus.getDefault().post(new EventCenter(EventBusConst.LOGIN_SUCCESS));
                                ToastUtil.getInstance().show("登录成功！");
                            }
                            loginGeTui();
                            finish();
                        } else {
                            ToastUtil.getInstance().show(o.errorMsg);
                        }
                    }
                });
    }

    private void loginGeTui() {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_SENDMSG
                + "?userTag=" + BaseSharedPreferences.getClientId()
                + "&deviceType=1"
                + "&deviceModel=" + ""
                + "&deviceVersion=" + ""
                + "&version=1.0", new HttpManager.ResponseCallback<BaseBackBean>() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(BaseBackBean o) {
                if (o.errorCode.equals("0000")) {
                } else
                    ToastUtil.getInstance().show(o.errorMsg);
            }
        });
    }

    //  Log.e("msg",
//          "============================Map=========================");
//                        Log.e("msg", string + "::::" + map.get(string));
    String imgKey, userName, accessKoken, openId, unionId;
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            Log.e("AAA", "onComplete: " + data);
            Toast.makeText(LoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
            umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    Set<String> set = map.keySet();
                    for (String string : set) {
                        String str = map.get(string);
                        if (str != null) {
                            if (string.equals("iconurl")) {// 设置头像
                                Log.e("AAA", "str！=" + str);
                                imgKey = str;
                            }
                            if (string.equals("name")) { // 设置昵称
                                Log.e("AAA", "str！=" + str);
                                userName = str;
                            }
                            if (string.equals("accessToken")) { // accessToken
                                Log.e("AAA", "str！=" + str);
                                accessKoken = str;
                            }
                            if (string.equals("openid")) {// openId
                                Log.e("AAA", "str！=" + str);
                                openId = str;
                            }
                            if (string.equals("unionid")) {// unionid
                                Log.e("AAA", "str！=" + str);
                                unionId = str;
                            }
                        }
                    }
                    //处理获取的数据。。。。。
                    next(imgKey, userName, accessKoken, openId, unionId);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    public void next(String imgKey, String userName, String accessKoken, String openId, String unionId) {
        HttpManager.getInstance().get(WebAPI.Login.LOGIN_LOGINBYWX
                        + "?accessKoken=" + accessKoken
                        + "&openId=" + openId
                        + "&imgKey=" + imgKey
                        + "&userName=" + userName
                        + "&unionId=" + unionId
                , new HttpManager.ResponseCallback<LoginBean>() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(LoginBean o) {
                        if (o.errorCode.equals("0000")) {
                            BaseSharedPreferences.setSessionId(null, o.smUserModel.sessionId);
                            BaseSharedPreferences.setId(LoginActivity.this, o.smUserModel.id);
                            BaseSharedPreferences.setPhoneNumber(LoginActivity.this, o.smUserModel.phone);
                            BaseSharedPreferences.setNumber(LoginActivity.this, o.smUserModel.number);
                            BaseSharedPreferences.setUserName(LoginActivity.this, o.smUserModel.name);
                            BaseSharedPreferences.setUserHeadUrl(LoginActivity.this, o.smUserModel.imgKey);
                            BaseSharedPreferences.setRoleType(LoginActivity.this, o.smUserModel.roleType + "");
                            if (!CheckUtil.isNull(o.smUserModel.number)) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                EventBus.getDefault().post(new EventCenter(EventBusConst.LOGIN_SUCCESS));
                                ToastUtil.getInstance().show("登录成功！");
                            } else
                                startActivity(new Intent(LoginActivity.this, UserDetailActivity.class));
                            loginGeTui();
                            finish();
                        } else
                            ToastUtil.getInstance().show(o.errorMsg);
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }
}
