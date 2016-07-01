package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.repository.db.ShopDBRepository;
import com.PopCorp.Purchases.data.repository.net.ShopNetRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopsInteractor {

    ShopNetRepository netRepository = new ShopNetRepository();
    ShopDBRepository dbRepository = new ShopDBRepository();

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
}
