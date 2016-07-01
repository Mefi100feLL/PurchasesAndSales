package com.PopCorp.Purchases.presentation.view.moxy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void createDrawer();
}
