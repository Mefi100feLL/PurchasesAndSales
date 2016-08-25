package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.dto.CommentResult;
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
    public Observable<List<SaleComment>> getData(int saleId) {
        return api.getComments(saleId)
                .flatMap(UniversalDTO::getData);
    }

    public Observable<CommentResult> sendComment(String author, String whom, String text, int regionId, int saleId) {
        return api.sendComment(author, whom, text, regionId, saleId);
    }
}
