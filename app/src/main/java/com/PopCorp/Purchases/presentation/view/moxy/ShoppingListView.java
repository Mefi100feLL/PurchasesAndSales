package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItem;

import java.util.ArrayList;

public interface ShoppingListView extends SampleDataView {
    void showTitle(String title);

    void filter(String filter);

    void changeItemInActionMode(int count, ListItem item);

    void finishActionMode();

    void startActionMode();

    void showInputFragment(ListItem listItem);

    void showItemsRemoved(ArrayList<ListItem> itemsForRemove);

    void showItemRemoved(ListItem listItem);
}
