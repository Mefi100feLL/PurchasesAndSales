package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Sale;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SaleInfoView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showInfo(Sale sale);
}
