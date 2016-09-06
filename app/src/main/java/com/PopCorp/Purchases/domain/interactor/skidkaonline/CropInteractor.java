package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

public class CropInteractor {

    public Observable<String> saveBitmapInFile(final Bitmap bitmap) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String uri = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", new Locale("ru")).format(Calendar.getInstance().getTime()) + ".jpg";
                try {
                    ImageLoader.getInstance().getDiskCache().save(uri, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                    return;
                }
                subscriber.onNext(uri);
                subscriber.onCompleted();
            }
        });
    }
}
