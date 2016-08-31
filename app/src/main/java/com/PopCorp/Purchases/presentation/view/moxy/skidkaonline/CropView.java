package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import android.graphics.Bitmap;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;
import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CropView extends MvpView {

    void showImage(Bitmap bitmap);
    void showProgress();
    void hideProgress();

    @StateStrategyType(SkipStrategy.class)
    void showError(int errorRes);

    void showErrorCanNotCropImage();

    void hideSkips();

    void hideFab();

    void showSkips();

    void showFab();

    void showListsSelecting(List<ShoppingList> shoppingLists);

    void showEmptyLists();

    void openInputListItemFragment(ListItem item, long[] ids);

    void showSendingButton();

    void showImage(String imageUri);
}