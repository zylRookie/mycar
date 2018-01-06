package com.example.zyl.dqcar.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.zyl.dqcar.R;
import com.example.zyl.dqcar.common.DqCarApplication;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


public final class ImageLoaderImpl {
    private final LruCache cache;
    public final Picasso picasso;

    private static final class Holder {
        private static final ImageLoaderImpl instance = new ImageLoaderImpl();
    }

    private ImageLoaderImpl() {
        cache = new LruCache((int) Runtime.getRuntime().maxMemory() / 8);
        picasso = new Picasso.Builder(DqCarApplication.context)
                .downloader(new OkHttpDownLoader(DqCarApplication.context))
                .memoryCache(cache)
                .build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }

    public static ImageLoaderImpl getInstance() {
        return Holder.instance;
    }

    public void display(int resId, ImageView v) {
        picasso.load(resId).noFade().fit().into(v);
    }

    public void display(String url, ImageView v) {
        picasso.load(url).config(Bitmap.Config.RGB_565).placeholder(R.drawable.default_image).error(R.drawable.default_image).noFade().fit().into(v);
    }

    public void display(String url, ImageView v, int resId) {
        picasso.load(url).placeholder(R.drawable.default_image).noFade().fit().placeholder(resId).into(v);
    }

    public void displayCircle(String url, ImageView v) {
        picasso.load(url).placeholder(R.drawable.default_image).transform(new CircleTransform()).noFade().fit().into(v);
    }

    public void clear() {
        try {
            cache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}