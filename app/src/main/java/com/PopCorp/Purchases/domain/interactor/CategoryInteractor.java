package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.repository.db.CategoryDBRepository;
import com.PopCorp.Purchases.data.repository.net.CategoryNetRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryInteractor {

    CategoryNetRepository netRepository = new CategoryNetRepository();
    CategoryDBRepository dbRepository = new CategoryDBRepository();

    private List<Category> result = new ArrayList<>();

    public Observable<List<Category>> loadFromNet() {
        return netRepository.getData()
                .map(categories -> {
                    if (categories.size() != 0) {
                        dbRepository.addAllCategories(categories);
                    }
                    return categories;
                });
    }

    public Observable<List<Category>> loadFromDB() {
        return dbRepository.getData()
                .flatMap(categories -> {
                    result = categories;
                    return Observable.just(categories);
                });
    }

    public Observable<List<Category>> getData() {
        return Observable.concat(loadFromDB(), loadFromNet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<Category>>() {
                        @Override
                        public void call(Subscriber<? super List<Category>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<Category>>dematerialize();
                });
    }

    public void remove(Category category) {
        dbRepository.remove(category);
    }

    public void update(Category category) {
        dbRepository.update(category);
    }
}
