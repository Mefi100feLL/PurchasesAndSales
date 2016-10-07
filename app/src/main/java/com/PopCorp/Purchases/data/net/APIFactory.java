package com.PopCorp.Purchases.data.net;

import com.PopCorp.Purchases.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIFactory {

    public static final String BASE_URL = "http://purchases.cf";

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

    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG){
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }
}
