package com.example.zyl.dqcar.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zyl.dqcar.utils.EventCenter;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Author: Zhaoyl
 * Date: 2017/6/27 10:56
 * Description: 基类
 * PackageName: BaseVideoActivity
 * Copyright: 端趣网络
 **/

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        init(savedInstanceState);
        ImmersionBar.with(this).init();
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }


    }

    public final <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    public abstract int getLayout();

    /**
     * 初始化功能
     */
    protected abstract void init(Bundle savedInstanceState);


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

    /**
     * 子类重写该方法
     */
    protected void onEventComing(EventCenter eventCenter) {
    }

    /**
     * 需要注册EventBus的Activity重写该方法,返回true
     */
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

//    /**
//     * 覆写finish方法，覆盖默认方法，加入切换动画
//     */
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//    }

//    /**
//     * 覆写startactivity方法，加入切换动画
//     */
//    public void startActivity(Bundle bundle, Class<?> target) {
//        Intent intent = new Intent(this, target);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//    }

//    /**
//     * 覆写startactivity方法，加入切换动画
//     */
//    public void startActivity(String bundleName, Bundle bundle, Class<?> target) {
//        Intent intent = new Intent(this, target);
//        if (bundle != null) {
//            intent.putExtra(bundleName, bundle);
//        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//    }

}
