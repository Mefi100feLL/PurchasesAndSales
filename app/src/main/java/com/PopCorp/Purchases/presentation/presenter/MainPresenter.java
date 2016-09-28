package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.presentation.view.moxy.MainView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private long selectedDrawerItem = R.string.navigation_drawer_shops;

    public MainPresenter(){
        getViewState().createDrawer();
    }

    public void setSelectedDrawerItem(long selectedDrawerItem) {
        if (selectedDrawerItem != -1) {
            this.selectedDrawerItem = selectedDrawerItem;
        }
    }

    public long getSelectedDrawerItem() {
        return selectedDrawerItem;
    }
}
