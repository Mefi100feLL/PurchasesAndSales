package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.repository.db.skidkaonline.ShopDBRepository;
import com.PopCorp.Purchases.data.repository.net.skidkaonline.ShopNetRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopInteractor {

    private ShopDBRepository dbRepository = new ShopDBRepository();
    private ShopNetRepository netRepository = new ShopNetRepository();

    private List<Shop> result;

    public Observable<List<Shop>> loadFromNet(int regionId) {
        return netRepository.getData(regionId)
                .map(shops -> {
                    if (shops.size() != 0) {
                        dbRepository.addAllShops(shops);
                    }
                    return shops;
                });
    }

    public Observable<List<Shop>> loadFromDB(int regionId) {
        return dbRepository.getData(regionId).flatMap(shops -> {
            result = shops;
            return Observable.just(shops);
        });
    }

    public Observable<List<Shop>> getData(int regionId) {
        return Observable.concat(loadFromDB(regionId), loadFromNet(regionId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<Shop>>() {
                        @Override
                        public void call(Subscriber<? super List<Shop>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<Shop>>dematerialize();
                });
    }

    public void update(Shop item) {
        dbRepository.update(item);
    }

    public String getForUrl(String shopUrl, int cityId) {
        return dbRepository.getForUrl(shopUrl, cityId);
    }
}
