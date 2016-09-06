package com.PopCorp.Purchases.presentation.presenter.params.provider;

import com.PopCorp.Purchases.presentation.presenter.factory.ViewPagerCommentsPresenterFactory;
import com.arellomobile.mvp.ParamsProvider;

@ParamsProvider(ViewPagerCommentsPresenterFactory.class)
public interface SaleCommentsParamsProvider {

    String getSaleId(String presenterId);
}