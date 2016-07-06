package com.PopCorp.Purchases.data.repository.db.skidkaonline;

import com.PopCorp.Purchases.data.dao.skidkaonline.SaleDAO;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.domain.repository.skidkaonline.SaleRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public class SaleDBRepository implements SaleRepository {

    private SaleDAO dao = new SaleDAO();

    @Override
    public Observable<List<Sale>> getData(int cityId, String shopUrl) {
        return Observable.just(dao.getSales(cityId, shopUrl));
    }

    @Override
    public Observable<Sale> getSale(int cityId, int saleId) {
        return Observable.just(dao.getSale(cityId, saleId));
    }

    public void addAllSales(List<Sale> sales) {
        dao.addAllSales(sales);
    }

    public void addSale(Sale sale) {
        dao.updateOrAddToDB(sale);
    }
}
