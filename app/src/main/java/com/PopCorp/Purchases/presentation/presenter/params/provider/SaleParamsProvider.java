package com.PopCorp.Purchases.presentation.presenter.params.provider;

import com.PopCorp.Purchases.presentation.presenter.factory.ViewPagerPresenterFactory;
import com.arellomobile.mvp.ParamsProvider;

@ParamsProvider(ViewPagerPresenterFactory.class)
public interface SaleParamsProvider {

    String getSaleId(String presenterId);
}
