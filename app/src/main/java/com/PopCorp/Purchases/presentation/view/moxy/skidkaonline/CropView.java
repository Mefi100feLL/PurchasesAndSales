package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import android.graphics.Bitmap;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CropView extends MvpView {

    void showImage(Bitmap bitmap);
    void showProgress();
    void hideProgress();

    @StateStrategyType(SkipStrategy.class)
    void showError(int errorRes);

    void showErrorCanNotCropImage();

    void showActionsWithCroppedImage();
}