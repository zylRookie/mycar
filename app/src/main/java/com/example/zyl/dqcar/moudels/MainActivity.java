package com.example.zyl.dqcar.moudels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.moudels.fragment.MailFragment;
import com.example.zyl.dqcar.moudels.fragment.MineFragment;
import com.example.zyl.dqcar.moudels.fragment.MyMsgFragment;
import com.example.zyl.dqcar.moudels.fragment.RidersFragment;
import com.example.zyl.dqcar.utils.EventCenter;
import com.example.zyl.dqcar.utils.ToastUtil;
import com.example.zyl.dqcar.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final int drawable[] = {R.mipmap.tab_msg, R.mipmap.tab_mail, R.mipmap.tab_frieds, R.mipmap.tab_mine};
    private final int drawable_pressed[] = {R.mipmap.tab_msg_pressed, R.mipmap.tab_mail_pressed, R.mipmap.tab_frieds_pressed, R.mipmap.tab_mine_pressed};
    private ImageView[] iv_tab;
    private TextView[] tv_tab;
    private NoScrollViewPager home_viewPager;


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        $(R.id.tab_home).setOnClickListener(this);
        $(R.id.tab_category).setOnClickListener(this);
        $(R.id.tab_cart).setOnClickListener(this);
        $(R.id.tab_mine).setOnClickListener(this);

        iv_tab = new ImageView[4];
        tv_tab = new TextView[4];
        iv_tab[0] = $(R.id.iv_home);
        iv_tab[1] = $(R.id.iv_category);
        iv_tab[2] = $(R.id.iv_cart);
        iv_tab[3] = $(R.id.iv_mine);

        tv_tab[0] = $(R.id.tv_home);
        tv_tab[1] = $(R.id.tv_category);
        tv_tab[2] = $(R.id.tv_cart);
        tv_tab[3] = $(R.id.tv_mine);
        home_viewPager = $(R.id.home_viewPager);
        List<Fragment> list = new ArrayList<>();
        list.add(MyMsgFragment.newInstance());
        list.add(MailFragment.newInstance());
        list.add(RidersFragment.newInstance());
        list.add(MineFragment.newInstance());

//        home_viewPager.setOffscreenPageLimit(3);
        home_viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));

        iv_tab[0].setImageResource(drawable_pressed[0]);
        tv_tab[0].setTextColor(getResources().getColor(R.color.A44b0f4));

    }


    @Override
    public void onClick(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.tab_home:
                index = 0;
                break;
            case R.id.tab_category:
                index = 1;
                break;
            case R.id.tab_cart:
                index = 2;
                break;
            case R.id.tab_mine:
                index = 3;
                break;
        }
        select(index);
    }

    private int pre;

    private void select(int index) {
        if (index != pre) {
            iv_tab[index].setImageResource(drawable_pressed[index]);
            iv_tab[pre].setImageResource(drawable[pre]);
            tv_tab[index].setTextColor(getResources().getColor(R.color.A44b0f4));
            tv_tab[pre].setTextColor(getResources().getColor(R.color.white));
            if (Math.abs(index - pre) > 2)
                home_viewPager.setCurrentItem(index, false);
            else
                home_viewPager.setCurrentItem(index);
            pre = index;
        }
    }


    private class MainPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        public MainPagerAdapter(FragmentManager fm, List<Fragment> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    private long lastTimePressed = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimePressed < 1000) {
            finishSimple();
        } else {
            lastTimePressed = System.currentTimeMillis();
            ToastUtil.getInstance().show("再按一次退出");
        }
    }

    public void finishSimple() {
        super.finish();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        super.onEventComing(eventCenter);
        if (eventCenter.getEventCode() == EventBusConst.BACK_SUCCESS) {
            finish();
        }
    }
}
