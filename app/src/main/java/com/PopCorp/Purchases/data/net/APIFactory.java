package com.PopCorp.Purchases.data.net;

import com.PopCorp.Purchases.data.utils.StethoLauncher;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIFactory {

    public static final String BASE_URL = "http://138.201.245.117/";

    private static API api;

    public static API getAPI() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(buildClient())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(API.class);
        }
        return api;
    }

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (StethoLauncher.getStethoInterceptor() != null) {
            builder.addNetworkInterceptor(StethoLauncher.getStethoInterceptor());
        }
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        return builder.build();
    }
}
