package com.PopCorp.Purchases.domain.repository.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public interface SaleRepository {

    Observable<List<Sale>> getData(int cityId, String shopUrl);

    Observable<Sale> getSale(int cityId, int saleId);
}
