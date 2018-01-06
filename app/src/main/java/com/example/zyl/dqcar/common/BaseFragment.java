package com.example.zyl.dqcar.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyl.dqcar.utils.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 若把初始化内容放到initData实现,就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * -
 * -注1: 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * ------可以调用mViewPager.setOffscreenPageLimit(size),若设置了该属性 则viewpager会缓存指定数量的Fragment
 * -注2: 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * -注3: 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 */
public abstract class BaseFragment extends Fragment {


    protected View mView;
    protected boolean mIsVisible;

    protected abstract int getLayout();

    /**
     * 此方法先于{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}执行
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e(getClass().getSimpleName(), "setUserVisibleHint() Visible: " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser;
        onVisibleChange(isVisibleToUser);
    }

    protected void onVisibleChange(boolean isVisible) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBindEventBusHere())
            EventBus.getDefault().register(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayout(), container, false);
        return mView;
    }

    public <T extends View> T $(@IdRes int id) {
        return (T) mView.findViewById(id);
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

    //    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (isBindEventBusHere())
//            EventBus.getDefault().unregister(this);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
            Log.e("AAA", "onDestroyView: ------>销毁");
        }
    }
}
