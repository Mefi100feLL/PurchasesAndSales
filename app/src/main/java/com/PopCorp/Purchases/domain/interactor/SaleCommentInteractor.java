package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.dto.CommentResult;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.data.repository.db.SaleCommentDBRepository;
import com.PopCorp.Purchases.data.repository.net.SaleCommentNetRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SaleCommentInteractor {

    SaleCommentNetRepository netRepository = new SaleCommentNetRepository();
    SaleCommentDBRepository dbRepository = new SaleCommentDBRepository();

    private List<SaleComment> result;

    public Observable<List<SaleComment>> getData(int saleId) {
        return netRepository.getData(saleId)
                .subscribeOn(Schedulers.io())
                .map(comments -> {
                    if (comments.size() != 0) {
                        dbRepository.addAllComments(comments);
                    }
                    return comments;
                })
                .onErrorResumeNext(throwable -> {
                    return dbRepository.getData(saleId)
                            .doOnNext(comments -> result = comments)
                            .flatMap(comments -> Observable.error(throwable));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.create(new Observable.OnSubscribe<List<SaleComment>>() {
                        @Override
                        public void call(Subscriber<? super List<SaleComment>> subscriber) {
                            subscriber.onNext(result);
                            subscriber.onError(throwable);
                        }
                    }).materialize().observeOn(AndroidSchedulers.mainThread()).<List<SaleComment>>dematerialize();
                });
    }

    public Observable<CommentResult> sendComment(String author, String whom, String text, int regionId, int saleId){
        return netRepository.sendComment(author, whom, text, regionId, saleId);
    }

    /*public Observable<CommentResult> sendComment(String author, String whom, String text, int regionId, int saleId){
        return Observable.create(new Observable.OnSubscribe<CommentResult>() {
            @Override
            public void call(Subscriber<? super CommentResult> subscriber) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(new CommentResult(false, "Ошибка при отправке!", "", "", 0));
            }
        });
    }*/
}
