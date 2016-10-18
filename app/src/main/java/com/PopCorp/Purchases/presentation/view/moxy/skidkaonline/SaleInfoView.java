package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.davemorrissey.labs.subscaleview.ImageSource;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface SaleInfoView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showError(Throwable e);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showSaleEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showImage(ImageSource uri);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void showProgress();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void hideProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setProgress(int progress);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showInfo(Sale sale);

    void showListsSelecting(List<ShoppingList> shoppingLists);

    void showEmptyLists();

    void openInputListItemFragment(ListItem item, long[] ids);

    void showErrorLoadingLists(Throwable e);

    void showItemAdded();

    void showTapTargetForSending();

    void showTapTargetForComments();

    void showTapTargetForCropping();

    void showTapTargetForSharing();

    void showFavorite(boolean favorite);

    void hideFavorite();

    void hideSendButton();

    void hideCropButton();

    void hideCommentsButton();

    void showCommentsMenuItem();

    void showSendButton();

    void showCropButton();

    void showCommentsButton();

    void hideCommentsMenuItem();
}
