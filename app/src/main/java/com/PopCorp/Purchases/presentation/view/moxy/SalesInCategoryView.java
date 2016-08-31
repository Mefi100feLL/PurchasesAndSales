package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;

@StateStrategyType(SkipStrategy.class)
public interface SalesInCategoryView extends SalesView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showShopsEmpty();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showFavoriteShopsEmpty();

    void showShopsForSelectingFavorites(ArrayList<Shop> shops);
}
