package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface SaleView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentComments(Sale sale);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentInfo(Sale sale);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showComments(List<SaleComment> comments);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showInfo(Sale sale);
}