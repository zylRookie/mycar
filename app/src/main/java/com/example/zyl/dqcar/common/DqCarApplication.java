package com.example.zyl.dqcar.common;

import android.app.Application;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.example.zyl.dqcar.getui.DqIntentService;
import com.example.zyl.dqcar.getui.DqPushService;
import com.igexin.sdk.PushManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import cn.hadcn.keyboard.ChatKeyboardLayout;
import cn.hadcn.keyboard.EmoticonEntity;
import cn.hadcn.keyboard.utils.EmoticonBase;

/**
 * Author: Zhaoyl
 * Date: 2017/6/27 10:56
 * Description: 全局
 * PackageName: DqCarApplication
 * Copyright: 端趣网络
 **/

public class DqCarApplication extends Application {

    public static DqCarApplication context;
    public static OSS oss;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Config.DEBUG = true;
        UMShareAPI.get(this);
        initOss();
        //注册
        PushManager.getInstance().initialize(this, DqPushService.class);
        //登陆
        PushManager.getInstance().registerPushIntentService(this, DqIntentService.class);
        if (!ChatKeyboardLayout.isEmoticonsDBInitSuccess(this)) {
            List<EmoticonEntity> entities = new ArrayList<>();
            entities.add(new EmoticonEntity("emoticons/xhs", EmoticonBase.Scheme.ASSETS));
            entities.add(new EmoticonEntity("emoticons/tusiji", EmoticonBase.Scheme.ASSETS));
            ChatKeyboardLayout.initEmoticonsDB(this, true, entities);
        }

    }

    private void initOss() {
        // ACCESS_ID,ACCESS_KEY是在阿里云申请的
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI9IDjKj1YZjH3", "hrMDF6eOni4q2NnicOIToooJndkDmT");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(getApplicationContext(), "http://oss-cn-hangzhou.aliyuncs.com", credentialProvider, conf);
    }

    //[UMSocialWechatHandler setWXAppId:@"wx16ff9889c5481bbf" appSecret:@"a53ac29441c25832fc63c0f599f846d0" url:@"http://www.umeng.com/social"];

    {
        //PlatformConfig.setWeixin("wx16ff9889c5481bbf", "0bcccdaa72348ed42424224b4646ea59");
        PlatformConfig.setWeixin("wx16ff9889c5481bbf", "a53ac29441c25832fc63c0f599f846d0");
        PlatformConfig.setQQZone("1106395940", "eFs0pFCzKjd8VTpx");
        PlatformConfig.setSinaWeibo("162968088", "6309a7530b9790373f7d91d1d1bbbb76", "http://sns.whalecloud.com");
    }


}
