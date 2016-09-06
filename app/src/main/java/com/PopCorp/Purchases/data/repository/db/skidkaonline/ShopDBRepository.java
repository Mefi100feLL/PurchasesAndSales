package com.PopCorp.Purchases.data.repository.db.skidkaonline;

import com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.repository.skidkaonline.ShopRepository;

import java.util.List;

import rx.Observable;

public class ShopDBRepository implements ShopRepository {

    private ShopDAO dao = new ShopDAO();

    @Override
    public Observable<List<Shop>> getData(int cityId) {
        return Observable.just(dao.getShopsForCity(cityId));
    }

    public void addAllShops(List<Shop> shops) {
        dao.addAllShops(shops);
    }

    public void update(Shop item) {
        dao.updateOrAddToDB(item);
    }

    public String getForUrl(String shopUrl, int cityId) {
        return dao.getWithUrl(shopUrl, cityId).getName();
    }

    public void remove(Shop shop) {
        dao.remove(shop);
    }
}
