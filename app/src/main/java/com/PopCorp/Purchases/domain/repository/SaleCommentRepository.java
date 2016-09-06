package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.SaleComment;

import java.util.List;

import rx.Observable;

public interface SaleCommentRepository {

    Observable<List<SaleComment>> getData(int saleId, int cityId);
}
