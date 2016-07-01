package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.Sale;

import java.util.List;

import rx.Observable;

public interface SaleRepository {

    Observable<List<Sale>> getData(int regionId, int[] shops, int[] categories, int[] categoriesTypes);

    Observable<Sale> getSale(int regionId, int cityId);
}
