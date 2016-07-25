package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.math.BigDecimal;
import java.util.ArrayList;

@StateStrategyType(SkipStrategy.class)
public interface ShoppingListView extends SampleDataView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTitle(String title);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void changeItemInActionMode(int count, ListItem item);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "actionMode")
    void finishActionMode();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "actionMode")
    void startActionMode();

    void showInputFragment(ListItem listItem);

    void showItemsRemoved(ArrayList<ListItem> itemsForRemove);

    void showItemRemoved(ListItem listItem);

    void showNothingRemoving();

    void finish();

    void updateCurrency(String currency);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showErrorLoadingList();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showBuyedTotals(int countBuyed, BigDecimal buyed);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTotals(int size, BigDecimal total);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "shopsFilter")
    void hideShopsFilter();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "shopsFilter")
    void showShopsFilter(ArrayList<String> shops, String filter);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyItems();

    void onItemBuyedChanged(ListItem item);
}
