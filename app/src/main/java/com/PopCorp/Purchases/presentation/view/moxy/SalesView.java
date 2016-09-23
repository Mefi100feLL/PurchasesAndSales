package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface SalesView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showSalesEmpty();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filter(String filter);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "spinner")
    void showSpinner();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "spinner")
    void hideSpinner();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectSpinner(int filterPosition);

    void showSales(View v, Sale item);

    void showEmptyForSearch(String query);
}
