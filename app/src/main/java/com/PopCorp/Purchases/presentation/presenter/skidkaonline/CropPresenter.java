package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.graphics.Bitmap;

import com.PopCorp.Purchases.domain.interactor.skidkaonline.CropInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.CropView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class CropPresenter extends MvpPresenter<CropView> {

    private CropInteractor interactor = new CropInteractor();

    public void cropImage(Bitmap cropped, String cacheDir) {
        getViewState().showImage(cropped);
        getViewState().showProgress();
        interactor.saveBitmapInFile(cropped, cacheDir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().showErrorCanNotCropImage();
                    }

                    @Override
                    public void onNext(File file) {
                        getViewState().hideProgress();
                        if (file != null) {
                            getViewState().showActionsWithCroppedImage();
                        } else {
                            onError(null);
                        }
                    }
                });
    }

}
