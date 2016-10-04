package com.PopCorp.Purchases.data.utils;

import android.graphics.Bitmap;

import com.PopCorp.Purchases.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class UIL {

    public static DisplayImageOptions getScaleImageOptions(){
        return new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static DisplayImageOptions getImageOptions(){
        return new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_image_media)
                .showImageOnFail(R.drawable.ic_image_media_alert)
                .showImageForEmptyUri(R.drawable.ic_image_media_alert)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static DisplayImageOptions getDownloadOptions(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_image_media)
                .showImageOnFail(R.drawable.ic_image_media_alert)
                .showImageForEmptyUri(R.drawable.ic_image_media_alert)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
