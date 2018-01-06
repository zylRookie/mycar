package com.example.zyl.dqcar.moudels.activity.msg;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Author: Zhaoyl
 * Date: 2017/1/9 13:58
 * Description: 图片滑动放大页面
 * PackageName: ImagePagerActivity
 * Copyright: 银点商城
 **/

public class ImagePagerActivity extends BaseActivity implements View.OnClickListener {

    String position;
    ViewPager imagePage;
    RelativeLayout llViewPager;
    FrameLayout flViewPager;
    PhotoView pv;
    TextView pageNo;
    TextView pagerSize;
    List<String> list;
    boolean llP;
    private int StartX, SlipX;
    private int currentItemNum = 0;
    Animation popshow;
    Animation pophidden;
    String type;
    String pvUrl;

    @Override
    public int getLayout() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        pv = $(R.id.pv);
        imagePage = $(R.id.comment_imagePage);
        flViewPager = $(R.id.flViewPager);
        llViewPager = $(R.id.ll_viewPager);
        pageNo = $(R.id.pageNo);
        pagerSize = $(R.id.pageSize);
        $(R.id.pager_ivBack).setOnClickListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        initListener();
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        if (type != null && type.equals("pv")) {
            flViewPager.setVisibility(View.GONE);
            pv.setVisibility(View.VISIBLE);
            pvUrl = getIntent().getStringExtra("pvUrl");
            pv.enable();
            pv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(ImagePagerActivity.this).load(pvUrl).into(pv);
            pv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            flViewPager.setVisibility(View.VISIBLE);
            pv.setVisibility(View.GONE);
            position = getIntent().getStringExtra("position");
            Bundle bundle = getIntent().getExtras();
            list = (List<String>) bundle.getSerializable("list");
            imagePage.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
            MyPagerAdapter myPagerAdapter = new MyPagerAdapter(list);
            popshow = AnimationUtils.loadAnimation(this, R.anim.popshow_anim);
            pophidden = AnimationUtils.loadAnimation(this, R.anim.pophidden_anim);
            imagePage.setAdapter(myPagerAdapter);
            imagePage.setCurrentItem(Integer.parseInt(position));
            currentItemNum = Integer.parseInt(position);
            pageNo.setText(String.valueOf(Integer.parseInt(position) + 1));
            pagerSize.setText("/" + list.size() + "");
        }
    }

    private void initListener() {
        imagePage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageNo.setText(position + 1 + "");
                currentItemNum = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagePage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        StartX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        SlipX = (int) event.getX();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        if (currentItemNum == 0 && SlipX - StartX > 100 && SlipX - StartX >= (width / 4)) {
                            finish();
                            overridePendingTransition(R.anim.pager_left_in, R.anim.pager_right_out);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_ivBack:
                finish();
                break;
        }
    }


    public class MyPagerAdapter extends PagerAdapter {

        private List<String> list;

        public MyPagerAdapter(List<String> list) {
            this.list = list;
        }

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
            PhotoView view = new PhotoView(ImagePagerActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(ImagePagerActivity.this).load(list.get(position)).into(view);
//            ImagePlayer.load(ImagePagerActivity.this, list.get(position), view);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!llP) {
                        llViewPager.setVisibility(View.VISIBLE);
                        llP = true;
                    } else {
                        llViewPager.setVisibility(View.GONE);
                        llP = false;
                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                StartX = (int) ev.getX();
                break;
        }
        return true;
    }

}
