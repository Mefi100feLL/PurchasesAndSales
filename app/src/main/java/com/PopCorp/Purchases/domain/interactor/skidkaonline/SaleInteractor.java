package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.repository.db.skidkaonline.SaleDBRepository;
import com.PopCorp.Purchases.data.repository.net.skidkaonline.SaleNetRepository;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SaleInteractor {

    private SaleNetRepository netRepository = new SaleNetRepository();
    private SaleDBRepository dbRepository = new SaleDBRepository();
    ShoppingListInteractor listInteractor = new ShoppingListInteractor();

    private List<Sale> result;

    public Observable<List<Sale>> getData(int cityId, String shopUrl) {
        return netRepository.getData(cityId, shopUrl)
                .subscribeOn(Schedulers.io())
                .map(sales -> {
                    if (sales.size() != 0) {
                        dbRepository.addAllSales(sales);
                    }
                    return sales;
                })
                .onErrorResumeNext(throwable -> {
                    return dbRepository.getData(cityId, shopUrl)
                            .doOnNext(sales -> result = sales)
                            .flatMap(sales -> Observable.error(throwable));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<Sale>>() {
                        @Override
                        public void call(Subscriber<? super List<Sale>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<Sale>>dematerialize();
                })
                .map(sales -> {
                    if (sales != null && sales.size() > 0) {
                        ShoppingList defaultList = listInteractor.getDefaultList();
                        ArrayList<Integer> saleIds = new ArrayList<>();
                        if (defaultList.getItems() != null && defaultList.getItems().size() > 0) {
                            for (ListItem item : defaultList.getItems()) {
                                if (item.getSale() != null && item.getSale().getSaleId() != 0) {
                                    saleIds.add(item.getSale().getSaleId());
                                }
                            }
                        }
                        for (Sale sale : sales) {
                            if (saleIds.contains(sale.getId())) {
                                sale.setFavorite(true);
                            }
                        }
                    }
                    return sales;
                });
    }

    public Observable<Sale> getSale(int cityId, int saleId) {
        return dbRepository.getSale(cityId, saleId)
                .flatMap(sale -> {
                    if (sale == null) {
                        return getSaleFromNet(cityId, saleId);
                    }
                    return Observable.just(sale);
                })
                .map(sale -> {
                    if (sale != null) {
                        ShoppingList defaultList = listInteractor.getDefaultList();
                        ArrayList<Integer> saleIds = new ArrayList<>();
                        if (defaultList.getItems() != null && defaultList.getItems().size() > 0) {
                            for (ListItem item : defaultList.getItems()) {
                                if (item.getSale() != null && item.getSale().getSaleId() != 0) {
                                    saleIds.add(item.getSale().getSaleId());
                                }
                            }
                        }
                        if (saleIds.contains(sale.getId())) {
                            sale.setFavorite(true);
                        }
                    }
                    return sale;
                });
    }

    private Observable<Sale> getSaleFromNet(int cityId, int saleId) {
        return netRepository.getSale(cityId, saleId)
                .flatMap(sale -> {
                    dbRepository.addSale(sale);
                    return Observable.just(sale);
                });
    }

    public Observable<Boolean> refreshFavorites(ArrayList<Sale> sales) {
        return Observable.create(subscriber -> {
            boolean edited = false;
            if (sales != null && sales.size() > 0) {
                ShoppingList defaultList = listInteractor.getDefaultList();
                ArrayList<Integer> saleIds = new ArrayList<>();
                if (defaultList.getItems() != null && defaultList.getItems().size() > 0) {
                    for (ListItem item : defaultList.getItems()) {
                        if (item.getSale() != null && item.getSale().getSaleId() != 0) {
                            saleIds.add(item.getSale().getSaleId());
                        }
                    }
                }
                for (Sale sale : sales) {
                    if (saleIds.contains(sale.getId())) {
                        if (!sale.isFavorite()){
                            sale.setFavorite(true);
                            edited = true;
                        }
                    } else {
                        if (sale.isFavorite()){
                            sale.setFavorite(false);
                            edited = true;
                        }
                    }
                }
            }
            subscriber.onNext(edited);
            subscriber.onCompleted();
        });
    }
}
