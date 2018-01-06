package com.example.zyl.dqcar.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.DqCarApplication;

import java.lang.ref.WeakReference;

public final class ToastUtil {
    private final Toast toast;
    private final TextView tv_message;
    private final InnerRunnable runnable;
    private final Handler mHandler;
    private String msg = "";

    private static class Holder {
        private static final ToastUtil instance = new ToastUtil();
    }

    private ToastUtil() {
        toast = new Toast(DqCarApplication.context);
        LayoutInflater inflate = (LayoutInflater) DqCarApplication.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast, null);
        tv_message = (TextView) v.findViewById(R.id.tv_message);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);

        mHandler = new Handler(Looper.getMainLooper());
        runnable = new InnerRunnable(this);
    }

    public static ToastUtil getInstance() {
        return Holder.instance;
    }

    public void show(String msg) {
        show(msg, Gravity.BOTTOM, 0, 170);
    }

    public void show(String msg, int gravity, int offX, int offY) {
        this.msg = msg;
        toast.setGravity(gravity, offX, offY);
        if (Looper.getMainLooper().getThread() == Thread.currentThread())
            runnable.run();
        else
            //tv_message.post(runnable);
            mHandler.post(runnable);
    }

    private void innerShow() {
        tv_message.setText(msg);
        toast.show();
    }

    private static final class InnerRunnable implements Runnable {
        private final WeakReference<ToastUtil> reference;

        public InnerRunnable(ToastUtil toastUtil) {
            reference = new WeakReference<ToastUtil>(toastUtil);
        }

        @Override
        public void run() {
            ToastUtil toastUtil = reference.get();
            if (toastUtil != null)
                toastUtil.innerShow();
        }
    }

}