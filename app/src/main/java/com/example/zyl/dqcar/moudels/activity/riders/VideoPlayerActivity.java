package com.example.zyl.dqcar.moudels.activity.riders;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.BaseActivity;
import com.example.zyl.dqcar.utils.CheckUtil;
import com.example.zyl.dqcar.utils.GetBitmap;
import com.squareup.picasso.Picasso;

/**
 * Author: Zhaoyl
 * Date: 2017/8/21 20:56
 * Description: 视频播放
 * PackageName: VideoPlayerActivity
 * Copyright: 端趣网络
 **/

public class VideoPlayerActivity extends BaseActivity {

    MediaController mediaController;
    VideoView vvMyPlayer;
    ProgressBar pbVideo;
    ImageView ivFirstPhoto;
    RelativeLayout rlVideo;
    GetBitmap getBitmap;
    String url;
    String videoImage;

    @Override
    public int getLayout() {
        return R.layout.activity_videoplayer;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        vvMyPlayer = $(R.id.vvMyPlayer);
        ivFirstPhoto = $(R.id.ivFirstPhoto);
        pbVideo = $(R.id.pbVideo);
        rlVideo = $(R.id.rlVideo);
        if (!CheckUtil.isNull(getIntent().getStringExtra("videoPath"))) {
            url = getIntent().getStringExtra("videoPath");
            videoImage = getIntent().getStringExtra("videoImage");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vvMyPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("AAA", "onTouch: ");
                finish();
                return false;
            }
        });
        initData();
    }

    private void initData() {
        Picasso.with(VideoPlayerActivity.this).load(videoImage).into(ivFirstPhoto);
        play(url);
    }

    private void play(final String path) {
        mediaController = new MediaController(this);
        vvMyPlayer.setVideoPath(path);
        // 设置VideView与MediaController建立关联
        vvMyPlayer.setMediaController(mediaController);
//        // 设置MediaController与VideView建立关联
        mediaController.setMediaPlayer(vvMyPlayer);
        mediaController.setVisibility(View.INVISIBLE);
        // 让VideoView获取焦点
//        videoView.requestFocus();
        // 开始播放
//        vvMyPlayer.start();
        vvMyPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                rlVideo.setVisibility(View.GONE);
                ivFirstPhoto.setVisibility(View.GONE);
                mp.start();
                mp.setLooping(true);
                Log.e("AAA", "onPrepared: ------>开始了");
            }
        });

        vvMyPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vvMyPlayer.setVideoPath(path);
                vvMyPlayer.start();
            }
        });

        vvMyPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vvMyPlayer != null) {
            vvMyPlayer.suspend();  //将VideoView所占用的资源释放掉
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vvMyPlayer.isPlaying())
            vvMyPlayer.suspend();

    }

    @Override
    protected void onResume() {
        super.onResume();
        vvMyPlayer.resume();
        vvMyPlayer.start();
    }
}
