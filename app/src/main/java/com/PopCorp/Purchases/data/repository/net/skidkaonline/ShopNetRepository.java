package com.PopCorp.Purchases.data.repository.net.skidkaonline;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.skidkaonline.ShopRepository;

import java.util.List;

import rx.Observable;

public class ShopNetRepository implements ShopRepository {

    private API api = APIFactory.getAPI();

    @Override
    public Observable<List<Shop>> getData(int cityId) {
        return api.getSkidkaonlineShops(cityId)
                .flatMap(UniversalDTO::getData);
    }
}
