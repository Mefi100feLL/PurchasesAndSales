package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;

public interface SalesInShopView extends SalesView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showCategoriesEmpty();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showFavoriteCategoriesEmpty();

    @StateStrategyType(SkipStrategy.class)
    void showCategoriesForSelectingFavorites(ArrayList<Category> categories);

}
