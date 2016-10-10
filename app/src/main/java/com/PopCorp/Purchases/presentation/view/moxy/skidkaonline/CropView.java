package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import android.graphics.Bitmap;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface CropView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showImage(Bitmap bitmap);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showImage(String imageUri);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showError(Throwable e);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void showProgress();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void hideProgress();


    @StateStrategyType(value = GroupSingleStrategy.class, tag = "skips")
    void hideSkips();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "skips")
    void showSkips();


    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fab")
    void hideFab();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fab")
    void showFab();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fab")
    void showSendingButton();


    void showListsSelecting(List<ShoppingList> shoppingLists);

    void showEmptyLists();

    void openInputListItemFragment(ListItem item, long[] ids);

    void showErrorCanNotCropImage();

    void showItemAdded();

    void showErrorLoadingLists(Throwable e);

    void showTapTargetForRotateSkip();

    void showTapTargetForCrop();

    void showTapTargetForScaleSkip();
}