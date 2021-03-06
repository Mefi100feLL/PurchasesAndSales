package com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline;

import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SalePresenter;
import com.arellomobile.mvp.PresenterFactory;

public class SalePresenterFactory  implements PresenterFactory<SalePresenter, String> {

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