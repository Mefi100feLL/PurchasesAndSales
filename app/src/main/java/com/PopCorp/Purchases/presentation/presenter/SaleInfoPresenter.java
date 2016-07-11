package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SaleInfoView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleInfoPresenter extends MvpPresenter<SaleInfoView> {

    private SaleInteractor interactor = new SaleInteractor();

    private Sale sale;

    public void setSale(int saleId) {
        if (sale == null) {
            interactor.getSale(Integer.valueOf(PreferencesManager.getInstance().getRegionId()), saleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Sale>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Sale result) {
                            sale = result;
                            getViewState().showInfo(sale);
                        }
                    });
        }
    }

    public Sale getSale() {
        return sale;
    }
}
