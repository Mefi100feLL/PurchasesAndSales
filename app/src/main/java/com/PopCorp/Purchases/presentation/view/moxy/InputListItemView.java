package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.presentation.viewstate.strategy.GroupSingleStrategy;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface InputListItemView extends MvpView {

    void setFields(Product product);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCategoriesSpinner(List<ListItemCategory> categories);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNameAdapter(List<String> products);

    @StateStrategyType(value = GroupSingleStrategy.class, tag = "name")
    void showNameEmpty();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "name")
    void showListItemWithNameExists();
    @StateStrategyType(value = GroupSingleStrategy.class, tag = "name")
    void hideNameError();


    void returnItemAndClose(ListItem item);
}
