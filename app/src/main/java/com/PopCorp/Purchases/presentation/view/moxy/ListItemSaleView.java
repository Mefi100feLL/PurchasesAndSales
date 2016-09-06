package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.ListItemSale;
import com.arellomobile.mvp.MvpView;

public interface ListItemSaleView extends MvpView {
    void showInfo(ListItemSale currentSale);
}
