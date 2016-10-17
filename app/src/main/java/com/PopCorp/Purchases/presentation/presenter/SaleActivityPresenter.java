package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

@InjectViewState
public class SaleActivityPresenter extends MvpPresenter<SaleActivityView> {

    private int currentSaleId;
    private ArrayList<Integer> salesIds = new ArrayList<>();
    private boolean editMode;

    public void setCurrentId(int id) {
        currentSaleId = id;
    }

    public void setSalesIds(String[] ids) {
        for (String id : ids) {
            salesIds.add(Integer.valueOf(id));
        }
    }

    public int getCurrentSalePosition() {
        return salesIds.indexOf(currentSaleId);
    }

    public ArrayList<Integer> getSalesIds() {
        return salesIds;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }
}
