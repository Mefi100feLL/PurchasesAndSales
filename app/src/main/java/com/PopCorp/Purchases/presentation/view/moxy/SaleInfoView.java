package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface SaleInfoView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showInfo(Sale sale);

    void showEmptyLists();

    void showListsSelecting(List<ShoppingList> shoppingLists);

    void openInputListItemFragment(ListItem item, long[] listsIds);
}
