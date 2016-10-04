package com.PopCorp.Purchases.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.UIL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Apopsuenko on 03.10.2016.
 */

public class ImageLoaderAdapter {

    private static ImageLoaderAdapter instance;

    private final Context context;

    private ImageLoaderAdapter(Context context){
        this.context = context;
    }

    public static void setInstance(Context context){
        if (instance == null){
            instance = new ImageLoaderAdapter(context);
        }
    }

    public static ImageLoaderAdapter getInstance(){
        return instance;
    }

    public void displayImage(String uri, ImageView image){
        ImageLoader.getInstance().loadImage(uri, UIL.getDownloadOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                loadedImage.recycle();
                Picasso.with(context)
                        .load(getFile(imageUri))
                        .placeholder(R.drawable.ic_image_media)
                        .error(R.drawable.ic_image_media_alert)
                        .into(image);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private File getFile(String uri){
        return ImageLoader.getInstance().getDiskCache().get(uri);
    }
}
