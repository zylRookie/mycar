package com.example.zyl.dqcar.moudels.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.common.BaseSharedPreferences;

import java.util.ArrayList;

/**
 * Author: Zhaoyl
 * Date: 2017/9/15 11:56
 * Description: 引导页
 * PackageName: GuideActivity
 * Copyright: 端趣网络
 **/

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout bottomLl;
    //    private Button passTips;
    private TextView tv_now;
    /**
     * 引导页list
     */
    private ArrayList<View> list = null;
    /**
     * 引导页点点list
     */
    private ArrayList<ImageView> dotList = null;


    @Override
    public int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        viewPager = $(R.id.viewPager);
        bottomLl = $(R.id.bottom_ll);
        tv_now = $(R.id.tv_now);
        tv_now.setOnClickListener(this);
        initData();
        setCurrentPoint(0);
    }


    public void initData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (null == list)
            list = new ArrayList<View>();
        if (null == dotList)
            dotList = new ArrayList<ImageView>();
        ImageView iv1 = new ImageView(this);
        iv1.setBackgroundResource(R.mipmap.splash1);
        list.add(iv1);
        ImageView iv2 = new ImageView(this);
        iv2.setBackgroundResource(R.mipmap.splash2);
        list.add(iv2);
        ImageView iv3 = new ImageView(this);
        iv3.setBackgroundResource(R.mipmap.splash3);
        list.add(iv3);

        for (int i = 0; i < list.size(); i++) {
            ImageView iv = new ImageView(this);
            dotList.add(iv);
            iv.setImageResource(R.mipmap.lead_link);
            bottomLl.addView(iv);
        }

        viewPager.setAdapter(new myViewPagerAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == dotList.size() - 1) {
                    tv_now.setVisibility(View.VISIBLE);
                    bottomLl.setVisibility(View.GONE);
                } else {
                    tv_now.setVisibility(View.GONE);
                    bottomLl.setVisibility(View.VISIBLE);

                }
                setCurrentPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_now://立即体验
                BaseSharedPreferences.setIsFirst(GuideActivity.this, false);
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }


    class myViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }

    /**
     * 设置引导页点的变化
     *
     * @param selectItems
     */
    private void setCurrentPoint(int selectItems) {
        for (int i = 0; i < dotList.size(); i++) {
            if (i == selectItems) {
                dotList.get(i).setImageResource(R.mipmap.lead_hornor);
            } else {
                dotList.get(i).setImageResource(R.mipmap.lead_link);
            }
        }
    }
}
