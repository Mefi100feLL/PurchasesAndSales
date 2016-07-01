package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.data.model.Sale;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SalesView extends SampleDataView {

    void showSalesEmpty();

    void filter(String filter);

    void showSpinner();

    void selectSpinner(int filterPosition);

    @StateStrategyType(SkipStrategy.class)
    void showSales(View v, Sale item);

    void showErrorLoadingSales(Throwable e);
}
