package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.presentation.view.moxy.ListItemSaleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ListItemSalePresenter extends MvpPresenter<ListItemSaleView> {

    private ListItemSale currentSale;

    public void setSale(ListItemSale sale) {
        if (currentSale == null) {
            currentSale = sale;
            getViewState().showInfo(currentSale);
        }
    }
}
