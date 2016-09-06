package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.SaleCommentRepository;

import java.util.List;

import rx.Observable;

public class SaleCommentNetRepository implements SaleCommentRepository {

    API api = APIFactory.getAPI();

    @Override
    public Observable<List<SaleComment>> getData(int saleId, int cityId) {
        return api.getComments(saleId, cityId)
                .flatMap(UniversalDTO::getData);
    }

    public Observable<SaleComment> sendComment(String author, String whom, String text, int regionId, int saleId) {
        return api.sendComment(author, whom, text, regionId, saleId)
                .flatMap(saleCommentUniversalDTO -> {
                    if (!saleCommentUniversalDTO.isError()){
                        return Observable.just(saleCommentUniversalDTO.getResult());
                    }
                    return Observable.error(new Throwable(saleCommentUniversalDTO.getMessage()));
                });
    }
}
