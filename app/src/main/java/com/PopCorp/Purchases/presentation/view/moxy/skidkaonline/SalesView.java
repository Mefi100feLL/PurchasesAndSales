package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.presentation.view.moxy.SampleDataView;
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

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSpinner();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectSpinner(int filterPosition);

    void showSales(View v, Sale item);

    void showTapTargetForFilter();

    void update();

    void showTapTargetForSalesFavorite();
}
