package com.PopCorp.Purchases.presentation.presenter.factory;

import com.PopCorp.Purchases.presentation.presenter.SalePresenter;
import com.arellomobile.mvp.PresenterFactory;

public class ViewPagerPresenterFactory implements PresenterFactory<SalePresenter, String> {

    @Override
    public SalePresenter createPresenter(SalePresenter presenter, Class<SalePresenter> aClass, String s) {
        if (presenter == null)
        {
            throw new IllegalStateException("Unable to instantiate " + presenter + ": " +
                    "make sure class name exists, " +
                    "is public, and " +
                    "has an empty constructor that is public");
        }

        return presenter;
    }

    @Override
    public String createTag(Class<SalePresenter> aClass, String s) {
        return s;
    }
}
