package com.PopCorp.Purchases.presentation.presenter.factory;

import com.PopCorp.Purchases.presentation.presenter.SaleInfoPresenter;
import com.arellomobile.mvp.PresenterFactory;

public class SaleInfoPresenterFactory implements PresenterFactory<SaleInfoPresenter, String> {

    @Override
    public SaleInfoPresenter createPresenter(SaleInfoPresenter presenter, Class<SaleInfoPresenter> aClass, String s) {
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
    public String createTag(Class<SaleInfoPresenter> aClass, String s) {
        return s;
    }
}