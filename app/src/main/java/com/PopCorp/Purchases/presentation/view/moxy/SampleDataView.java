package com.PopCorp.Purchases.presentation.view.moxy;

import android.view.View;

import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SampleDataView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showProgress();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showData();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showError(String text, int drawableRes, int textButtonRes, View.OnClickListener listener);
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showError(int textRes, int drawableRes, int textButtonRes, View.OnClickListener listener);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void refreshing(boolean refresh);

    @StateStrategyType(SkipStrategy.class)
    void showSnackBar(int errorRes);
}
