package com.PopCorp.Purchases;

import android.app.Application;
import android.content.Context;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.SalesCleaner;
import com.PopCorp.Purchases.data.utils.SkidkaonlineSalesCleaner;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.utils.ImageLoaderAdapter;
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
        PreferencesManager.setInstance(this);
        ThemeManager.setInstance(this);
        DB.setInstance(this);
        initImageLoader(this);
        ImageLoaderAdapter.setInstance(this);
        PreferencesManager.getInstance().firstStart();
        new SalesCleaner().start();
        new SkidkaonlineSalesCleaner().start();
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
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
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
