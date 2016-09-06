package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.repository.db.skidkaonline.CityDBRepository;
import com.PopCorp.Purchases.data.repository.net.skidkaonline.CityNetRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CityInteractor {
    
    private CityDBRepository dbRepository = new CityDBRepository();
    private CityNetRepository netRepository = new CityNetRepository();

    private List<City> result;

    public Observable<List<City>> getData() {
        return netRepository.getData()
                .subscribeOn(Schedulers.io())
                .map(cities -> {
                    if (cities.size() != 0) {
                        dbRepository.addAllCities(cities);
                    }
                    return cities;
                })
                .onErrorResumeNext(throwable -> {
                    return dbRepository.getData()
                            .doOnNext(cities -> result = cities)
                            .flatMap(shopList -> Observable.error(throwable));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<City>>() {
                        @Override
                        public void call(Subscriber<? super List<City>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<City>>dematerialize();
                });
    }
}
