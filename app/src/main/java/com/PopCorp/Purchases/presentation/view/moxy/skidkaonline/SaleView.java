package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SaleView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentComments(int saleId);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentInfo(int saleId);
}