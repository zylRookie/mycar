package com.example.zyl.dqcar.moudels.activity.chat;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AudioLib
 * Created by 90Chris on 2015/12/2.
 */
public class AudioLib {
    private static final String TAG = "AudioLib";
    private static AudioLib sAudioLib = null;
    private MediaRecorder recorder;
    private String mPath;
    private int mPeriod = 0;
    private static final int MIN_LENGTH = 2;
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;


    public static AudioLib getInstance() {
        if (sAudioLib == null) {
            sAudioLib = new AudioLib();
        }
        return sAudioLib;
    }

    public AudioLib() {
        new Timer().schedule(new AudioTimerTask(), 0, 1000);
    }

    private class AudioTimerTask extends TimerTask {
        @Override
        public void run() {
            ++mPeriod;
        }
    }

    public synchronized void start(String path, OnAudioListener listener) {
        Log.e("AAA", "start recording");
        mPeriod = 0;

        mListener = listener;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setMaxDuration(MAX_LENGTH);
        recorder.setOutputFile(path);

        try {
            recorder.prepare();
            recorder.start();
            updateMicStatus();
            Log.e(TAG, "record start success");
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }

        mPath = path;
    }

    /**
     * cancel, not save the file
     *
     * @return true, cancel success, false, cancel failed
     */
    public synchronized boolean cancel() {
        Log.e(TAG, "cancel recording");
        if (recorder == null) {
            Log.e(TAG, "recorder is null ");
            return false;
        }
        try {
            stopRecord();
        } catch (IllegalStateException e) {
            Log.e(TAG, "illegal state happened when cancel", e);
        }

        File file = new File(mPath);
        return file.exists() && file.delete();
    }

    /**
     * complete the recording
     *
     * @return recording last time
     */
    public synchronized int complete() {
        Log.e(TAG, "complete recording");
        Log.e(TAG, "complete recording" + mPeriod + "");

        if (recorder == null) {
            Log.e(TAG, "recorder is null ");
            return mPeriod;
        }

        try {
            stopRecord();
        } catch (IllegalStateException e) {
            Log.e(TAG, "illegal state happened when complete", e);
            return -1;
        }

        if (mPeriod < MIN_LENGTH) {
            Log.e(TAG, "record time is too short");
            return -1;
        }

        return mPeriod;
    }

    public String generatePath() {
        boolean isSuccess = true;
        final String cachePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DqCar";
        File file = new File(cachePath);
        if (!file.exists()) {
            isSuccess = file.mkdirs();
        }
        if (isSuccess) {
            return cachePath + File.separator + "record_" + System.currentTimeMillis() + ".mp3";
        } else {
            return null;
        }
    }

    private synchronized void stopRecord() {
        //mHandler.removeCallbacks(mUpdateMicStatusTimer);

        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private void updateMicStatus() {
        if (recorder != null) {
            double ratio = (double) recorder.getMaxAmplitude();
            double db = 0;
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
            }
            if (mListener != null) {
                mListener.onDbChange(db);
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, 500);
        }
    }

    private OnAudioListener mListener = null;

    public interface OnAudioListener {
        void onDbChange(double db);
    }

    private MediaPlayer mMediaPlayer = null;
    private String mCurrentPlayingAudioPath = null;
    private OnMediaPlayComplete mPlayListener = null;

    /**
     * play the audio
     *
     * @param path path of the audio file
     */
    public synchronized void playAudio(String path, OnMediaPlayComplete listener) {
        mPlayListener = listener;
        if (mMediaPlayer != null) {
            stopPlay();
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
                if (mPlayListener != null) {
                    mPlayListener.onPlayComplete(true);
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            mPlayListener.onPlayComplete(false);
        }

        mMediaPlayer.start();

        mCurrentPlayingAudioPath = path;
    }

    public synchronized void stopPlay() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mCurrentPlayingAudioPath = null;
    }

    public boolean isPlaying(String path) {
        return (mMediaPlayer != null) && mMediaPlayer.isPlaying() && (path.equals
                (mCurrentPlayingAudioPath));
    }

    public interface OnMediaPlayComplete {
        void onPlayComplete(boolean isSuccess);
    }
}
