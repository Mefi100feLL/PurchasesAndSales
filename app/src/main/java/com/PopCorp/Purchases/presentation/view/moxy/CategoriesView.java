package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface CategoriesView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showRegionEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showFavoriteCategoriesEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showCategoriesEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyRegions();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectSpinner(int position);

    void showSelectingRegions(List<Region> regions);

    void showSnackBarWithNewCategories(int count, boolean isFilterFavorite);

    void showSnackBarWithNewCategory(Category category, boolean isFilterFavorite);

    void openCategory(View v, Category category);

    void showTapTargetForFilter();

    void showTapTargetForCategFavorite();
}
