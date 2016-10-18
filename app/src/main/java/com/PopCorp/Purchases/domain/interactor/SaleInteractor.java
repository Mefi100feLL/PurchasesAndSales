package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.repository.db.SaleDBRepository;
import com.PopCorp.Purchases.data.repository.net.SaleNetRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.PopCorp.Purchases.R.menu.sales;

public class SaleInteractor {

    SaleNetRepository netRepository = new SaleNetRepository();
    SaleDBRepository dbRepository = new SaleDBRepository();
    ShoppingListInteractor listInteractor = new ShoppingListInteractor();

    ListItemSaleDAO listItemSaleDAO = new ListItemSaleDAO();

    private List<Sale> result;

    public Observable<List<Sale>> getData(int regionId, int[] shops, int[] categories, int[] categoriesTypes) {
        return netRepository.getData(regionId, shops, categories, categoriesTypes)
                .subscribeOn(Schedulers.io())
                .map(sales -> {
                    if (sales.size() != 0) {
                        dbRepository.addAllSales(sales);
                    }
                    return sales;
                })
                .onErrorResumeNext(throwable -> {
                    return dbRepository.getData(regionId, shops, categories, categoriesTypes)
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
                    sale.setCategory(new CategoryDAO().getCategory(sale.getCategoryId(), sale.getCategoryType()));
                    sale.setShop(new ShopDAO().getShop(sale));
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
