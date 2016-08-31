package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;

@StateStrategyType(SkipStrategy.class)
public interface ShoppingListsView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyLists();

    void showDialogForNewList();

    void openShoppingList(ShoppingList list);

    void shareListAsSMS(ShoppingList list);

    void shareListAsEmail(ShoppingList list);

    void shareListAsText(ShoppingList list);

    void showRemovedList(ShoppingList list);

    void showRemovedLists(ArrayList<ShoppingList> removedLists);
}
