package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.presentation.view.moxy.MainView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private int selectedDrawerItem = R.id.navigation_shops;

    public MainPresenter(){
        getViewState().createDrawer();
    }

    public void setSelectedDrawerItem(int selectedDrawerItem) {
        this.selectedDrawerItem = selectedDrawerItem;
    }

    public int getSelectedDrawerItem() {
        return selectedDrawerItem;
    }
}
