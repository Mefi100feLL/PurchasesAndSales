package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface SaleInfoView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showInfo(Sale sale);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showError(Throwable e);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showSaleEmpty();

    void showEmptyLists();

    void showListsSelecting(List<ShoppingList> shoppingLists);

    void openInputListItemFragment(ListItem item, long[] listsIds);

    void showErrorLoadingLists(Throwable e);

    void showItemAdded();

    void openSameSale(View v, int saleId);

    void showTapTargetForComments();

    void showTapTargetForSharing();

    void showTapTargetForSending();
}
