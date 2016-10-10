package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;
import java.util.Comparator;

@StateStrategyType(SkipStrategy.class)
public interface SelectingProductsView extends SampleDataView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fastScroll")
    void showFastScroll();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fastScroll")
    void hideFastScroll();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setAdapterComparator(Comparator<Product> comparator);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void checkItem(int itemId);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showProductsEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showFavoriteProductsEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showSearchProductsEmpty(String currentFilter);

    void setResultAndExit(ArrayList<ListItem> result);

    void showTapTargetForProductsSearch();

    void showTapTargetForProductsFilter();

    void showTapTargetForProductsSorting();

    void showTapTargetForProductsReturn();
}
