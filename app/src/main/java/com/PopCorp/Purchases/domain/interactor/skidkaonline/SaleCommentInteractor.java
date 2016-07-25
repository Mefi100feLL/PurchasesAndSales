package com.PopCorp.Purchases.domain.interactor.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;
import com.PopCorp.Purchases.data.repository.net.skidkaonline.SaleCommentNetRepository;

import java.util.List;

import rx.Observable;

public class SaleCommentInteractor {

    private SaleCommentNetRepository netRepository = new SaleCommentNetRepository();

    public Observable<List<SaleComment>> getData(int saleId){
        return netRepository.getData(saleId);
    }

    public Observable<SaleComment> sendComment(String author, String text, int cityId, int saleId){
        /*return Observable.create(new Observable.OnSubscribe<SaleComment>() {
            @Override
            public void call(Subscriber<? super SaleComment> subscriber) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(new SaleComment(saleId, author, "", Calendar.getInstance().getTimeInMillis(), text));
                subscriber.onCompleted();
            }
        });*/
        return netRepository.addComment(author, text, cityId, saleId);
    }
}
