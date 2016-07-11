package com.PopCorp.Purchases.presentation.presenter.params.provider;

import com.PopCorp.Purchases.presentation.presenter.factory.ViewPagerSkidkaonlinePresenterFactory;
import com.arellomobile.mvp.ParamsProvider;

@ParamsProvider(ViewPagerSkidkaonlinePresenterFactory.class)
public interface SkidkaonlineSaleParamsProvider {

    String getSaleId(String presenterId);
}
