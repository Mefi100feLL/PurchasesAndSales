package com.PopCorp.Purchases;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.SalesCleaner;
import com.PopCorp.Purchases.data.utils.SkidkaonlineSalesCleaner;
import com.PopCorp.Purchases.data.utils.StethoLauncher;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class PurchasesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AnalyticsTrackers.initialize(this);
        initExceptionsReporter();
        StethoLauncher.launch(this);
        PreferencesManager.setInstance(this);
        ThemeManager.setInstance(this);
        DB.setInstance(this);
        initImageLoader(this);
        PreferencesManager.getInstance().firstStart();
        new SalesCleaner().start();
        new SkidkaonlineSalesCleaner().start();
    }

    private void initExceptionsReporter() {
        ExceptionReporter myHandler = new ExceptionReporter(AnalyticsTrackers.getInstance().getDefault(), Thread.getDefaultUncaughtExceptionHandler(), this);

        StandardExceptionParser exceptionParser = new StandardExceptionParser(getApplicationContext(), null) {
            @Override
            public String getDescription(String threadName, Throwable t) {
                return "{" + threadName + "} " + Log.getStackTraceString(t);
            }
        };

        myHandler.setExceptionParser(exceptionParser);

        // Make myHandler the new default uncaught exception handler.
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }

    public static void initImageLoader(Context context) {
        FileNameGenerator generator = s -> {
            String[] split = s.split("/");
            return split[split.length - 1];
        };
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        DiskCache diskCache = new UnlimitedDiskCache(cacheDir, context.getCacheDir(), generator);

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.threadPriority(Thread.MIN_PRIORITY);
        builder.denyCacheImageMultipleSizesInMemory();
        builder.diskCache(diskCache);
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        if (BuildConfig.DEBUG) {
            builder.writeDebugLogs();
        }
        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);
    }
}
