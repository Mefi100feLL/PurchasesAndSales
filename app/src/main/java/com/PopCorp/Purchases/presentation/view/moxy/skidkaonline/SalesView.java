package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.presentation.view.moxy.SampleDataView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SalesView extends SampleDataView {

    void showSalesEmpty();

    void filter(String filter);

    void showSpinner();

    void selectSpinner(int filterPosition);

    @StateStrategyType(SkipStrategy.class)
    void showSales(View v, Sale item);

    void showErrorLoadingSales(Throwable e);
}
