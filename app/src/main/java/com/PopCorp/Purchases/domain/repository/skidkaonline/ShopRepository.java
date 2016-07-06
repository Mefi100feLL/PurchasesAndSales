package com.PopCorp.Purchases.domain.repository.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Shop;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public interface ShopRepository {

    Observable<List<Shop>> getData(int cityId);
}
