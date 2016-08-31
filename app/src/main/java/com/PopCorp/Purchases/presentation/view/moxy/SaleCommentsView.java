package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface SaleCommentsView extends SampleDataView {

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void showCommentTextEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentText")
    void hideCommentTextError();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void showCommentAuthorEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "commentAuthor")
    void hideCommentAuthorError();

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "data")
    void showCommentsEmpty();

    void clearFields();

    void showAuthorSaved();
}
