package com.PopCorp.Purchases.data.repository.net.skidkaonline;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.skidkaonline.SaleCommentRepository;

import java.util.List;

import rx.Observable;

public class SaleCommentNetRepository implements SaleCommentRepository {

    private API api = APIFactory.getAPI();

    @Override
    public Observable<List<SaleComment>> getData(int saleId) {
        return api.getSkidkaonlineSaleComments(saleId)
                .flatMap(UniversalDTO::getData);
    }

    @Override
    public Observable<SaleComment> addComment(String author, String text, int cityId, int saleId) {
        return api.sendSkidkaonlineSaleComment(author, text, cityId, saleId)
                .flatMap(UniversalDTO::getData);
    }
}
