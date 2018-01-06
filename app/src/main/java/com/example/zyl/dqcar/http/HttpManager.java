package com.example.zyl.dqcar.http;

import android.os.Handler;
import android.support.v4.util.ArrayMap;

import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http统一管理
 */
public final class HttpManager {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient mOkHttpClient;
    private final Gson gson;
    private final Handler mHandler;

    private static class Holder {
        private static final HttpManager instance = new HttpManager();
    }

    private HttpManager() {
        mHandler = new Handler();
        gson = new Gson();
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor())
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(8L, TimeUnit.SECONDS)
                .writeTimeout(8L, TimeUnit.SECONDS)
                .build();
    }

    public static HttpManager getInstance() {
        return Holder.instance;
    }

    public <T> void get(String url, final ResponseCallback<T> callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("JSESSIONID", BaseSharedPreferences.getSessionId(null))
                .build();
        exec(request, callback);
    }

    public <T> void put(String url, final ResponseCallback<T> callback) {
        Request request = new Request.Builder()
                .url(url)
                .put(null)
                .build();
        exec(request, callback);
    }

    public <T> void puts(String head, ResponseCallback<T> callback) {
        Request request = new Request.Builder()
                .url("http://192.168.1.224:6200/securityapi/v1.0/tokens")
                .put(new FormBody.Builder().build())
                .addHeader("JSESSIONID", head)
                .build();
        exec(request, callback);
    }

    public <T> void post(String url, ArrayMap<String, String> map, final ResponseCallback<T> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (int i = 0; i < map.size(); i++)
            builder.add(map.keyAt(i), map.valueAt(i));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        exec(request, callback);
    }

    public <T> void postJson(String url, String json, final ResponseCallback<T> callback) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("JSESSIONID", BaseSharedPreferences.getSessionId(null))
                .post(requestBody)
                .build();
        exec(request, callback);
    }

    private <T> void exec(Request request, final ResponseCallback<T> callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            final InnerRunnable runnable = new InnerRunnable(callback);

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mHandler.post(runnable);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 500)
                    runnable.setTag(gson.fromJson(response.body().string(), ((ParameterizedType) callback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]));
                mHandler.post(runnable);
            }
        });
    }

    private static class InnerRunnable implements Runnable {
        private WeakReference<ResponseCallback> reference;
        private Object o;

        public InnerRunnable(ResponseCallback callback) {
            reference = new WeakReference<>(callback);
        }

        public void setTag(Object o) {
            this.o = o;
        }

        @Override
        public void run() {
            ResponseCallback callback = reference.get();
            if (callback != null) {
                if (o == null)
                    callback.onFail();
                else
                    callback.onSuccess(o);

            }
        }
    }

    public interface ResponseCallback<T> {

        void onFail();

        void onSuccess(T t);

    }

}