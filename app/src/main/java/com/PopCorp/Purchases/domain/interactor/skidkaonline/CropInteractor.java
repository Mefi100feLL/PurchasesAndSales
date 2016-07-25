package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

public class CropInteractor {

    public Observable<File> saveBitmapInFile(final Bitmap bitmap, final String cacheDir) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                String fileName = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", new Locale("ru")).format(Calendar.getInstance().getTime()) + ".png";
                File file = new File(cacheDir + "/tmp/" + fileName);
                file.mkdirs();
                if (file.exists()) {
                    if (!file.delete()) {
                        subscriber.onError(null);
                    }
                }
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    subscriber.onNext(file);
                } catch (Exception e) {
                    subscriber.onError(null);
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }
}
