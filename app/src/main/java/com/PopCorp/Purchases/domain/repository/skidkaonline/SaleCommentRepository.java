package com.PopCorp.Purchases.domain.repository.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public interface SaleCommentRepository {

    Observable<List<SaleComment>> getData(int saleId);

    Observable<SaleComment> addComment(String author, String text, int cityId, int saleId);
}
