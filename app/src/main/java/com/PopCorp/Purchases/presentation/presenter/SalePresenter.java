package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.presentation.view.moxy.SaleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SalePresenter extends MvpPresenter<SaleView> {

    public static final String PRESENTER_ID = "SalePresenter";

    private int currentSaleId = -1;
    private boolean editMode;

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

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
