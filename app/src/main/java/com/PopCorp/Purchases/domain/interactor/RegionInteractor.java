package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.repository.db.RegionDBRepository;
import com.PopCorp.Purchases.data.repository.net.RegionNetRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegionInteractor {

    RegionNetRepository netRepository = new RegionNetRepository();
    RegionDBRepository dbRepository = new RegionDBRepository();

    private List<Region> result;

    public Observable<List<Region>> getData() {
        return netRepository.getData()
                .subscribeOn(Schedulers.io())
                .map(shops -> {
                    if (shops.size() != 0) {
                        dbRepository.addAllRegions(shops);
                    }
                    return shops;
                })
                .onErrorResumeNext(throwable -> {
                    return dbRepository.getData()
                            .doOnNext(shops -> result = shops)
                            .flatMap(shopList -> Observable.error(throwable));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<Region>>() {
                        @Override
                        public void call(Subscriber<? super List<Region>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<Region>>dematerialize();
                });
    }

    public Region getRegionWithId(String regionId) {
        return dbRepository.getWithId(regionId);
    }
}
