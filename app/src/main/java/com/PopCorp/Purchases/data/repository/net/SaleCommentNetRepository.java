package com.PopCorp.Purchases.data.repository.net;

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
        return api.getComments(saleId);
    }
}
