package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.presentation.view.moxy.SaleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SalePresenter extends MvpPresenter<SaleView> {

    private int currentSaleId = -1;

    public void setSale(int saleId) {
        if (currentSaleId == -1) {
            currentSaleId = saleId;
            showInfo();
        }
    }

    public void showComments() {
        getViewState().showFragmentComments(currentSaleId);
    }

    public void showInfo() {
        getViewState().showFragmentInfo(currentSaleId);
    }
}
