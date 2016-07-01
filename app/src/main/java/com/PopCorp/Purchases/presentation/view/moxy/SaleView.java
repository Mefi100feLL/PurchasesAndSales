package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SaleView extends MvpView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentComments(Sale sale);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "fragment")
    void showFragmentInfo(Sale sale);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void showCommentTextEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void hideCommentTextError();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void showCommentAuthorEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void hideCommentAuthorError();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showComments(Sale sale);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showInfo(Sale sale);
}
