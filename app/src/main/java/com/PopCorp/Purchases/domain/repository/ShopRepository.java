package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.Shop;

import java.util.List;

import rx.Observable;

public interface ShopRepository {

    Observable<List<Shop>> getData(int regionId);
}
