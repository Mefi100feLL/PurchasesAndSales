package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ShoppingList;

public interface ShoppingListsView extends SampleDataView {
    void showEmptyLists();

    void showDialogForNewList();

    void openShoppingList(ShoppingList list);

    void shareListAsSMS(ShoppingList list);

    void shareListAsEmail(ShoppingList list);

    void shareListAsText(ShoppingList list);

    void showRemovedList(ShoppingList list);
}
