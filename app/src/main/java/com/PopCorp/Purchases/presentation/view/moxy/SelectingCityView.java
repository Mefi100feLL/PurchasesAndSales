package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface SelectingCityView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showCitiesEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showEmptyForSearch(String searchText);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSelectedCity(City selectedCity);

    void showEmptySelectedCity();

    void setResultAndExit();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fastScroll")
    void showFastScroll();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fastScroll")
    void hideFastScroll();

    void finish();

    void showTapTargetForSearch();

    void showTapTargetForCitySelect();
}
