package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.domain.repository.ShopRepository;

import java.util.List;

import rx.Observable;

public class ShopDBRepository implements ShopRepository {

    private ShopDAO dao = new ShopDAO();

    @Override
    public Observable<List<Shop>> getData(int regionId) {
        return Observable.just(dao.getShopsForRegion(regionId));
    }

    public void addAllShops(List<Shop> shops) {
        dao.addAllShops(shops);
    }

    public void remove(Shop shop) {
        dao.remove(shop);
    }

    public void update(Shop item) {
        dao.updateOrAddToDB(item);
    }
}
