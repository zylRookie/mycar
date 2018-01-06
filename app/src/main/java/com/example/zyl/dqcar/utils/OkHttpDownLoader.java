package com.example.zyl.dqcar.utils;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by X on 2017/3/14.
 */

public class OkHttpDownLoader implements Downloader {
    private OkHttpClient mClient = null;

    public OkHttpDownLoader(Context context) {
        //mClient = new OkHttpClient.Builder().addInterceptor(new CacheControlInterceptor()).build();
        mClient = new OkHttpClient.Builder().addInterceptor(new CacheControlInterceptor()).cache(new Cache(new File(context.getExternalCacheDir(), "cache"), 60 * 1024 * 1024)).build();
    }

    private File createCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "cache");
        if (!cache.exists())
            cache.mkdirs();
        return cache;
    }

    @Override
    public Response load(Uri uri, int networkPolicy) throws IOException {
        CacheControl.Builder builder = new CacheControl.Builder();
        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                builder.onlyIfCached();
            } else {
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.noCache();
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    builder.noStore();
                }
            }
        }

        Request request = new Request.Builder()
                .cacheControl(builder.build())
                .url(uri.toString())
                .build();
        okhttp3.Response response = mClient.newCall(request).execute();
        //return new Response(response.body().byteStream(), false, response.body().contentLength());
        return new Response(response.body().byteStream(), response.cacheResponse() != null, response.body().contentLength());
    }

    @Override
    public void shutdown() {
    }

    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            return response
                    .newBuilder()
                    .header("Cache-Control", "public,max-age=20")
                    .removeHeader("Pragma").build();
        }

    }
}