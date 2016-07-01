package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.SaleDAO;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.domain.repository.SaleRepository;

import java.util.List;

import rx.Observable;

public class SaleDBRepository implements SaleRepository {

    private SaleDAO dao = new SaleDAO();

    @Override
    public Observable<List<Sale>> getData(int regionId, int[] shops, int[] categories, int[] categoriesTypes) {
        return Observable.just(dao.getSales(regionId, shops, categories, categoriesTypes));
    }

    @Override
    public Observable<Sale> getSale(int regionId, int saleId) {
        return Observable.just(dao.getSale(regionId, saleId));
    }

    public void addAllSales(List<Sale> sales) {
        dao.addAllSales(sales);
    }

    public void addSale(Sale sale) {
        dao.updateOrAddToDB(sale);
    }
}
