package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.math.BigDecimal;
import java.util.ArrayList;

@StateStrategyType(SkipStrategy.class)
public interface ShoppingListView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "actionMode")
    void finishActionMode();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "actionMode")
    void startActionMode();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showErrorLoadingList();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyItems();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyNoSaleItemsForShop(String shopName);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "shopsFilter")
    void hideShopsFilter();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "shopsFilter")
    void showShopsFilter(ArrayList<String> shops, String filter);


    @StateStrategyType(AddToEndSingleStrategy.class)
    void checkFilter(int itemId);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showBuyedTotals(int countBuyed, BigDecimal buyed);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTotals(int size, BigDecimal total);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTitle(String title);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void changeItemInActionMode(int count, ListItem item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void checkShowSales(boolean checked);

    void onItemBuyedChanged(ListItem item);

    void showInputFragment(ListItem listItem);

    void showItemsRemoved(ArrayList<ListItem> itemsForRemove);

    void showItemRemoved(ListItem listItem);

    void showNothingRemoving();

    void finish();

    void updateCurrency(String currency);

    void onItemSaleClicked(View v, ListItemSale sale);

    void shareListAsSMS(ShoppingList list);

    void shareListAsEmail(ShoppingList list);

    void shareListAsText(ShoppingList list);

    void showTapTargetForCreate();

    void showTapTargetForAdd();

    void showTapTargetForItemInfo();

    void showTapTargetForItemsFilterForShop();

    void showTapTargetForItemsAddToActionMode();

    void showTapTargetForItemEditInActionMode();

    void showTapTargetForItemsRemoveInActionMode();

    void openSale(long saleId);

    void showCantRemoveDefaultList();

    void openSkidkaonlineSale(int saleId);
}
