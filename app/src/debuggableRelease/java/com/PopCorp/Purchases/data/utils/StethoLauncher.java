package com.PopCorp.Purchases.data.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.Interceptor;

/**
 * Created by Apopsuenko on 07.10.2016.
 */

public class StethoLauncher {

    public static void launch(Application application) {
        Stetho.initializeWithDefaults(application);
    }

    public static Interceptor getStethoInterceptor(){
        return new StethoInterceptor();
    }
}
