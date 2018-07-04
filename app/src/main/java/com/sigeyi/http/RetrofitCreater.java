package com.sigeyi.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/3/6 14:30
 */

public class RetrofitCreater {
    private static RetrofitCreater mInstance;
    private Retrofit mRetrofit;

    public static RetrofitCreater getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitCreater.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitCreater();
                }
            }
        }
        return mInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(GlobalHttpHandler.EMPTY, RequestInterceptor.Level.ALL))
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.NAVI_DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public void onDestory() {
        if (mRetrofit != null)
            mRetrofit = null;
    }
}
