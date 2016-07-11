package com.PopCorp.Purchases.presentation.view.moxy.skidkaonline;

import com.PopCorp.Purchases.presentation.view.moxy.SampleDataView;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SaleCommentsView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void showCommentTextEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void hideCommentTextError();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void showCommentAuthorEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void hideCommentAuthorError();

    void showCommentsEmpty();

}
