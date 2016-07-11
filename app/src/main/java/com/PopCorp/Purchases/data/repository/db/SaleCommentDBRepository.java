package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.domain.repository.SaleCommentRepository;

import java.util.List;

import rx.Observable;

public class SaleCommentDBRepository implements SaleCommentRepository {

    private SaleCommentDAO dao = new SaleCommentDAO();

    @Override
    public Observable<List<SaleComment>> getData(int saleId) {
        return Observable.just(dao.getForSale(saleId));
    }

    public void addAllComments(List<SaleComment> comments) {
        dao.addAllSaleComments(comments);
    }
}
