package com.PopCorp.Purchases.presentation.presenter.factory;

import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SaleCommentsPresenter;
import com.arellomobile.mvp.PresenterFactory;

public class ViewPagerCommentsPresenterFactory implements PresenterFactory<SaleCommentsPresenter, String> {

    @Override
    public SaleCommentsPresenter createPresenter(SaleCommentsPresenter presenter, Class<SaleCommentsPresenter> aClass, String s) {
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
    public String createTag(Class<SaleCommentsPresenter> aClass, String s) {
        return s;
    }
}
