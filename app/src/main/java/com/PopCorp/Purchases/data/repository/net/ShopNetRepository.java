package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.ShopRepository;

import java.util.List;

import rx.Observable;

public class ShopNetRepository implements ShopRepository {

    API api = APIFactory.getAPI();

    @Override
    public Observable<List<Shop>> getData(int regionId) {
        return api.getShops(regionId);
    }
}
