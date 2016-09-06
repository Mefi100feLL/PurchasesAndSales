package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SalePresenter extends MvpPresenter<SaleView> {

    public static final String PRESENTER_ID = "SalePresenter";

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