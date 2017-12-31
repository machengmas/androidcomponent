package com.macheng.component.appupgrade.internal.common;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 对OkHttp进行一层封装，方便使用
 *
 * @author macheng
 * @name Component
 * @datetime 2017-12-28 16:57
 */
public class OkHttpManager {
    public static int REQUEST_SUCCESS = 1;
    public static int REQUEST_FAILED = 2;
    private static OkHttpClient client;

    public OkHttpManager() {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
    }

    /**
     * 以键值对作为参数发送请求，post方式
     *
     * @param url
     * @param params
     * @param handler
     */
    public void request(String url, Map<String, String> params, final Handler handler) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            builder.add(param.getKey(), param.getValue());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final Message message = Message.obtain();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                message.what = REQUEST_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                message.what = REQUEST_SUCCESS;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }
}
