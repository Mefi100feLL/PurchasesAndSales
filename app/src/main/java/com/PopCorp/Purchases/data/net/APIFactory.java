package com.PopCorp.Purchases.data.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIFactory {

    private static final String BASE_URL = "http://popcorp.cf";
    public static final String MESTOSKIDKI_URL = "http://mestoskidki.ru";

    private static API api;

    public static API getAPI() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
            api = retrofit.create(API.class);
        }
        return api;
    }
}
