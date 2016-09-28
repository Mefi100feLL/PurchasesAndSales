package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SalesView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observer;

@InjectViewState
public class SalesPresenter extends MvpPresenter<SalesView> implements RecyclerCallback<Sale> {

    private SaleInteractor interactor = new SaleInteractor();

    private ArrayList<Sale> objects = new ArrayList<>();
    private Shop currentShop;

    private ArrayList<String> filterCatalogs = new ArrayList<>();

    private String currentFilter = "";
    private int filterPosition = 0;

    public SalesPresenter(){
        getViewState().showProgress();
    }

    public void setCurrentShop(Shop shop) {
        if (currentShop == null){
            currentShop = shop;
            loadData();
        }
    }

    private void loadData(){
        interactor.getData(currentShop.getCityId(), currentShop.getUrl())
                .subscribe(new Observer<List<Sale>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (objects.size() == 0){
                            getViewState().showError(e);
                        } else{
                            getViewState().showSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(List<Sale> sales) {
                        getViewState().refreshing(false);
                        if (sales.size() == 0) {
                            getViewState().showSalesEmpty();
                        } else {
                            objects.clear();
                            objects.addAll(sales);
                            initFilters();
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                        }
                    }
                });
    }

    private void initFilters() {
        filterCatalogs.clear();
        boolean added = false;
        for (Sale sale : objects) {
            if (!filterCatalogs.contains(sale.getCatalog())) {
                filterCatalogs.add(sale.getCatalog());
                added = true;
            }
        }
        Collections.sort(filterCatalogs);
        if (added && filterPosition != 0){
            for (String catalog : filterCatalogs){
                if (catalog.equals(currentFilter)){
                    filterPosition = filterCatalogs.indexOf(catalog) + 1;
                }
            }
        }
        if (filterCatalogs.size() > 1) {
            getViewState().showSpinner();
            getViewState().selectSpinner(filterPosition);
        }
    }

    @Override
    public void onItemClicked(View view, Sale item) {
        getViewState().showSales(view, item);
    }

    @Override
    public void onItemLongClicked(View view, Sale item) {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    public ArrayList<String> getFilterStrings() {
        return filterCatalogs;
    }

    public void onFilter(int position) {
        filterPosition = position;
        if (position == 0) {
            currentFilter = "";
        } else {
            currentFilter = filterCatalogs.get(position);
        }
        getViewState().filter(currentFilter);
    }

    public void tryAgain() {
        getViewState().showProgress();
        loadData();
    }


    public void onRefresh() {
        if (filterCatalogs.size() != 0) {
            getViewState().refreshing(true);
            loadData();
        }
    }

    public ArrayList<Sale> getObjects() {
        return objects;
    }
}
