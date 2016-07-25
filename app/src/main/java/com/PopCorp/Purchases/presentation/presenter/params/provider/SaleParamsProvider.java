package com.PopCorp.Purchases.presentation.presenter.params.provider;

import com.PopCorp.Purchases.presentation.presenter.factory.SaleCommentsPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.factory.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.factory.SalePresenterFactory;
import com.arellomobile.mvp.ParamsProvider;

@ParamsProvider(value = {
        SalePresenterFactory.class,
        SaleCommentsPresenterFactory.class,
        SaleInfoPresenterFactory.class,
        com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SalePresenterFactory.class,
        com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleInfoPresenterFactory.class,
        com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleCommentsPresenterFactory.class
})
public interface SaleParamsProvider {

    String getSaleId(String presenterId);
}
