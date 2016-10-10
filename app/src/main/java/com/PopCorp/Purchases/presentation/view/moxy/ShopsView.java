package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface ShopsView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showRegionEmpty();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showFavoriteShopsEmpty();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showShopsEmpty();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyRegions();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectSpinner(int position);

    void showSelectingRegions(List<Region> regions);

    void showSnackBarWithNewShops(int count, boolean isFilterFavorite);

    void showSnackBarWithNewShop(Shop shop, boolean isFilterFavorite);

    void openShop(View v, Shop shop);

    void showTapTargetForFilter();

    void showTapTargetForShopFavorite();
}
