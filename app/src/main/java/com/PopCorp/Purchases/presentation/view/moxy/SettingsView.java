package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;
import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface SettingsView extends MvpView {

    void showDialogWithCategories(List<ListItemCategory> categories);

    void showDialogWithCurrencies(ArrayList<String> currencies);

    void showDialogWithUnits(ArrayList<String> units);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void showProgress();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "progress")
    void hideProgress();

    void showSnackBar(int errorResource);

    void showSelectingRegions(List<Region> regions);

    void showRegionsEmpty();

}
